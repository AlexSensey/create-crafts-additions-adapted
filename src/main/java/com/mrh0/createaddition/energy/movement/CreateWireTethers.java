package com.mrh0.createaddition.energy.movement;

import com.mrh0.createaddition.blocks.connector.base.AbstractConnectorBlock;
import com.mrh0.createaddition.blocks.connector.base.AbstractConnectorBlockEntity;
import com.mrh0.createaddition.blocks.connector.base.ConnectorMode;
import com.mrh0.createaddition.blocks.modular_accumulator.ModularAccumulatorBlock;
import com.mrh0.createaddition.blocks.portable_energy_interface.PortableEnergyManager;
import com.mrh0.createaddition.energy.LocalNode;
import com.mrh0.createaddition.energy.WireType;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.Map;

/** Independent Create contraption tethers. The stationary node owns the wire and its item drop.
 * A saved token, not an entity id or old block coordinate, identifies each connection.
 * Only pre-assembled stationary-to-moving links are supported; no Sable implementation is used.
 */
public final class CreateWireTethers {
    public static final String KEY = "CreateTethers";

    private CreateWireTethers() {}

    public static void capture(Contraption contraption, Level world, BlockPos offset, Map<BlockPos, CompoundTag> updateTags) {
        if (world.isClientSide()) return;
        BlockPos origin = contraption.anchor.offset(offset);
        for (var info : contraption.getBlocks().values()) {
            if (info.nbt() == null || !(world.getBlockEntity(info.pos().offset(origin))
                    instanceof AbstractConnectorBlockEntity moving)) continue;
            ListTag links = info.nbt().getList(KEY).orElseGet(ListTag::new);
            // Reassembly while a remote chunk was unloaded: keep the token, rebase the endpoint.
            for (var entry : links) {
                if (entry instanceof CompoundTag link) {
                    int slot = link.getInt("MovingSlot").orElse(-1);
                    if (validSlot(moving, slot)) writeEndpoint(link, Vec3.atCenterOf(info.pos()).add(moving.getNodeOffset(slot)));
                }
            }
            for (int slot = 0; slot < moving.getNodeCount(); slot++) {
                LocalNode node = moving.getLocalNode(slot);
                if (node == null) continue;
                if (node.createTether != null) {
                    // Both ends moving is outside this implementation. Break once at the owner.
                    Containers.dropItemStack(world, moving.getPos().getX() + .5,
                            moving.getPos().getY() + .5, moving.getPos().getZ() + .5, node.getType().getDrop());
                    moving.removeNode(slot, false);
                    removeSavedNode(info.nbt(), slot);
                    continue;
                }
                if (contraption.getBlocks().containsKey(node.getPos().subtract(origin))) continue;
                if (!(world.getBlockEntity(node.getPos()) instanceof AbstractConnectorBlockEntity stationary)) continue;
                int remoteSlot = node.getOtherIndex();
                if (!validSlot(stationary, remoteSlot)) continue;
                LocalNode reverse = stationary.getLocalNode(remoteSlot);
                if (reverse == null || reverse.createTether != null || reverse.getOtherIndex() != slot
                        || !reverse.getPos().equals(moving.getPos())) continue;
                CompoundTag link = new CompoundTag();
                link.putString("Token", UUID.randomUUID().toString());
                link.putLong("Stationary", stationary.getPos().asLong());
                link.putInt("StationarySlot", remoteSlot);
                link.putInt("MovingSlot", slot);
                link.putInt("Wire", node.getType().getIndex());
                link.putInt("MaxLength", Math.min(moving.getMaxWireLength(), stationary.getMaxWireLength()));
                link.putInt("MaxTransfer", Math.min(node.getType().transfer(), Math.min(moving.getMaxIn(), moving.getMaxOut())));
                writeEndpoint(link, Vec3.atCenterOf(info.pos()).add(moving.getNodeOffset(slot)));
                reverse.createTether = link.copy();
                stationary.invalidateNodeCache();
                if (stationary.getNetwork(remoteSlot) != null) stationary.getNetwork(remoteSlot).invalidate();
                stationary.notifyUpdate();
                links.add(link);
                moving.removeNode(slot, false);
                removeSavedNode(info.nbt(), slot);
            }
            if (!links.isEmpty()) info.nbt().put(KEY, links);
            // Create keeps a separate client tag. Do not render stale world-space links there.
            CompoundTag clientTag = updateTags.get(info.pos());
            if (clientTag != null) clientTag.put(LocalNode.NODES,
                    info.nbt().getList(LocalNode.NODES).orElseGet(ListTag::new).copy());
        }
    }

    static void removeSavedNode(CompoundTag nbt, int slot) {
        nbt.getList(LocalNode.NODES).ifPresent(nodes -> nodes.removeIf(tag ->
                tag instanceof CompoundTag node && node.getInt(LocalNode.ID).orElse(-1) == slot));
    }

    private static boolean validSlot(AbstractConnectorBlockEntity be, int slot) {
        return slot >= 0 && slot < be.getNodeCount();
    }

    private static boolean matches(AbstractConnectorBlockEntity stationary, CompoundTag link) {
        int slot = link.getInt("StationarySlot").orElse(-1);
        if (!validSlot(stationary, slot)) return false;
        LocalNode node = stationary.getLocalNode(slot);
        String token = link.getString("Token").orElse("");
        return !token.isEmpty() && node != null && node.createTether != null
                && token.equals(node.createTether.getString("Token").orElse(""));
    }

    private static void writeEndpoint(CompoundTag link, Vec3 end) {
        link.putDouble("X", end.x); link.putDouble("Y", end.y); link.putDouble("Z", end.z);
    }

    public static Vec3 localEndpoint(CompoundTag link) {
        return new Vec3(link.getDouble("X").orElse(0d), link.getDouble("Y").orElse(0d), link.getDouble("Z").orElse(0d));
    }

    /** The entity's normal tracking supplies movement to every observing client. */
    public static Vec3 renderEndpoint(Level world, CompoundTag link, float partialTicks) {
        var entity = world.getEntity(link.getInt("EntityId").orElse(-1));
        if (!(entity instanceof AbstractContraptionEntity moving) || moving.getContraption() == null
                || !entity.getUUID().toString().equals(link.getString("EntityUUID").orElse(""))) return null;
        return moving.toGlobalVector(localEndpoint(link), partialTicks);
    }

    public static void tick(MovementContext context) {
        if (context.world.isClientSide() || context.contraption.entity == null || context.blockEntityData == null) return;
        ListTag links = context.blockEntityData.getList(KEY).orElseGet(ListTag::new);
        var iterator = links.iterator();
        while (iterator.hasNext()) {
            if (!(iterator.next() instanceof CompoundTag link)) { iterator.remove(); continue; }
            BlockPos pos = BlockPos.of(link.getLong("Stationary").orElse(0L));
            if (!context.world.isLoaded(pos)) continue; // Do not force-load chunks or break unloaded wires.
            if (!(context.world.getBlockEntity(pos) instanceof AbstractConnectorBlockEntity stationary)
                    || !matches(stationary, link)) { iterator.remove(); continue; }
            int slot = link.getInt("StationarySlot").orElse(-1);
            Vec3 movingEnd = context.contraption.entity.toGlobalVector(localEndpoint(link), 1);
            Vec3 fixedEnd = Vec3.atCenterOf(pos).add(stationary.getNodeOffset(slot));
            int maxLength = Math.min(link.getInt("MaxLength").orElse(0), stationary.getMaxWireLength());
            if (movingEnd.distanceToSqr(fixedEnd) > (double) maxLength * maxLength) {
                stationary.removeNode(slot, true);
                iterator.remove();
                continue;
            }
            CompoundTag reservation = stationary.getLocalNode(slot).createTether;
            int entityId = context.contraption.entity.getId();
            String uuid = context.contraption.entity.getUUID().toString();
            if (reservation.getInt("EntityId").orElse(-1) != entityId
                    || !reservation.getString("EntityUUID").orElse("").equals(uuid)
                    || !localEndpoint(reservation).equals(localEndpoint(link))) {
                reservation.putInt("EntityId", entityId);
                reservation.putString("EntityUUID", uuid);
                writeEndpoint(reservation, localEndpoint(link));
                stationary.notifyUpdate();
            }
            if (context.disabled) continue;
            var attached = context.contraption.getBlocks().get(context.localPos.relative(context.state.getValue(AbstractConnectorBlock.FACING)));
            if (attached == null || !(attached.state().getBlock() instanceof ModularAccumulatorBlock)) continue;
            var storage = PortableEnergyManager.get(context.contraption);
            if (storage == null) continue;
            stationary.awakeNetwork(context.world);
            var network = stationary.getNetwork(slot);
            if (network == null || !network.isValid()) continue;
            int limit = Math.max(0, Math.min(link.getInt("MaxTransfer").orElse(0), Math.min(stationary.getMaxIn(), stationary.getMaxOut())));
            var mode = context.state.getValue(AbstractConnectorBlock.MODE);
            if (mode == ConnectorMode.Push) {
                int wanted = storage.receiveEnergy(limit, true);
                int received = network.pull(network.demand(wanted));
                if (received > 0) storage.receiveEnergy(received, false);
            } else if (mode == ConnectorMode.Pull) {
                int accepted = network.push(Math.min(limit, storage.extractEnergy(limit, true)), true);
                if (accepted > 0) network.push(storage.extractEnergy(accepted, false), false);
            }
        }
    }

    /** Reattach after placement, retrying if the stationary chunk has not loaded yet. */
    public static void restore(AbstractConnectorBlockEntity moving) {
        var world = moving.getLevel();
        if (!(world instanceof net.minecraft.server.level.ServerLevel server)) return;
        for (int slot = 0; slot < moving.getNodeCount(); slot++) {
            LocalNode node = moving.getLocalNode(slot);
            if (node != null && node.createTether != null
                    && TetherCleanupData.get(server).consume(node.createTether.getString("Token").orElse("")))
                moving.removeNode(slot, true);
        }
        if (moving.pendingCreateTethers.isEmpty()) return;
        var iterator = moving.pendingCreateTethers.iterator();
        while (iterator.hasNext()) {
            if (!(iterator.next() instanceof CompoundTag link)) { iterator.remove(); moving.setChanged(); continue; }
            BlockPos pos = BlockPos.of(link.getLong("Stationary").orElse(0L));
            if (!world.isLoaded(pos)) continue;
            if (world.getBlockEntity(pos) instanceof AbstractConnectorBlockEntity stationary && matches(stationary, link)) {
                int slot = link.getInt("MovingSlot").orElse(-1);
                int remote = link.getInt("StationarySlot").orElse(-1);
                WireType type = WireType.fromIndex(link.getInt("Wire").orElse(-1));
                int length = Math.min(stationary.getMaxWireLength(), moving.getMaxWireLength());
                if (type != null && validSlot(moving, slot) && moving.getLocalNode(slot) == null
                        && Vec3.atCenterOf(pos).add(stationary.getNodeOffset(remote)).distanceToSqr(
                                Vec3.atCenterOf(moving.getPos()).add(moving.getNodeOffset(slot))) <= (double) length * length) {
                    stationary.setNode(remote, slot, moving.getPos(), type);
                    moving.setNode(slot, remote, pos, type);
                } else stationary.removeNode(remote, true);
            }
            iterator.remove();
            moving.setChanged();
        }
    }

    public static void destroyed(AbstractContraptionEntity entity) {
        if (!(entity.level() instanceof net.minecraft.server.level.ServerLevel world) || entity.getContraption() == null) return;
        for (var info : entity.getContraption().getBlocks().values()) {
            if (info.nbt() == null) continue;
            for (var entry : info.nbt().getList(KEY).orElseGet(ListTag::new)) {
                if (!(entry instanceof CompoundTag link)) continue;
                BlockPos pos = BlockPos.of(link.getLong("Stationary").orElse(0L));
                if (!world.isLoaded(pos)) {
                    TetherCleanupData.get(world).mark(link.getString("Token").orElse(""));
                } else if (world.getBlockEntity(pos) instanceof AbstractConnectorBlockEntity stationary && matches(stationary, link)) {
                    stationary.removeNode(link.getInt("StationarySlot").orElse(-1), true);
                }
            }
        }
    }
}
