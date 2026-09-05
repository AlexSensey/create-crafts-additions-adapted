package com.mrh0.createaddition.energy.movement;

import com.mrh0.createaddition.blocks.portable_energy_interface.PortableEnergyManager.EnergyData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import java.io.*;

/** Standalone offline regression checks; not a JUnit suite or an in-game movement/render test. */
public final class CreateWireTetherDataTest {
    private static int checks;

    public static void main(String[] args) throws Exception {
        CompoundTag link = new CompoundTag();
        link.putString("Token", "test-token");
        link.putLong("Stationary", new BlockPos(-123, 72, 432).asLong());
        link.putDouble("X", -3.25); link.putDouble("Y", 1.125); link.putDouble("Z", 17.75);
        link.putInt("MovingSlot", 2);
        CompoundTag restored = roundTrip(link);
        check(CreateWireTethers.localEndpoint(restored).equals(new Vec3(-3.25, 1.125, 17.75)), "endpoint round trip");
        check(BlockPos.of(restored.getLong("Stationary").orElseThrow()).equals(new BlockPos(-123, 72, 432)), "negative world coordinates");
        check(restored.getString("Token").orElseThrow().equals("test-token"), "identity round trip");
        CompoundTag block = new CompoundTag();
        ListTag nodes = new ListTag();
        for (int i = 0; i < 3; i++) { CompoundTag node = new CompoundTag(); node.putInt("id", i); nodes.add(node); }
        block.put("nodes", nodes);
        ListTag links = new ListTag(); links.add(restored); block.put(CreateWireTethers.KEY, links);
        CreateWireTethers.removeSavedNode(block, 1);
        check(nodes.size() == 2 && nodes.getCompoundOrEmpty(1).getInt("id").orElseThrow() == 2, "remove only captured slot");
        CreateWireTethers.removeSavedNode(block, 1);
        check(nodes.size() == 2, "repeat capture removal is idempotent");
        check(roundTrip(block).getList(CreateWireTethers.KEY).orElseThrow().size() == 1, "link survives block serialization");

        CompoundTag accumulator = new CompoundTag();
        CompoundTag energy = new CompoundTag(); energy.putInt("energy", 90);
        accumulator.put("EnergyContent", energy); accumulator.putInt("EnergyCapacity", 100);
        EnergyData data = new EnergyData(accumulator);
        check(data.receiveEnergy(256) == 10, "charge limited by free capacity");
        check(energy.getInt("energy").orElseThrow() == 100, "charge written into contraption NBT");
        check(data.extractEnergy(40) == 40, "discharge quantity");
        EnergyData reloaded = new EnergyData(roundTrip(accumulator));
        check(reloaded.extractEnergy(256) == 60, "energy conserved across reload");
        check(reloaded.extractEnergy(1) == 0, "empty accumulator cannot generate energy");

        CompoundTag ledger = new CompoundTag();
        TetherCleanupData cleanup = new TetherCleanupData(ledger);
        cleanup.mark("test-token");
        TetherCleanupData reloadedCleanup = new TetherCleanupData(roundTrip(ledger));
        check(reloadedCleanup.consume("test-token"), "deferred break survives reload");
        check(!reloadedCleanup.consume("test-token"), "deferred break consumed only once");
        check(!reloadedCleanup.consume("different-token"), "replacement wire is not broken");

        ClassNode contraption = readClass("com/simibubi/create/content/contraptions/Contraption.class");
        check(contraption.methods.stream().anyMatch(m -> m.name.equals("removeBlocksFromWorld") &&
                m.desc.equals("(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V")), "capture mixin target signature");
        check(contraption.fields.stream().anyMatch(f -> f.name.equals("updateTags") && f.desc.equals("Ljava/util/Map;")), "client tag shadow target");
        ClassNode entity = readClass("com/simibubi/create/content/contraptions/AbstractContraptionEntity.class");
        check(entity.methods.stream().anyMatch(m -> m.name.equals("disassemble") && m.desc.equals("()V")), "placement mixin target");
        check(entity.methods.stream().anyMatch(m -> m.name.equals("remove") && m.desc.equals("(Lnet/minecraft/world/entity/Entity$RemovalReason;)V")), "removal mixin target");
        System.out.println("PASS: " + checks + " Create tether data/API regression checks");
    }

    private static CompoundTag roundTrip(CompoundTag tag) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        NbtIo.write(tag, new DataOutputStream(bytes));
        return NbtIo.read(new DataInputStream(new ByteArrayInputStream(bytes.toByteArray())));
    }

    private static ClassNode readClass(String name) throws IOException {
        try (InputStream stream = CreateWireTetherDataTest.class.getClassLoader().getResourceAsStream(name)) {
            if (stream == null) throw new AssertionError("Missing dependency " + name);
            ClassNode node = new ClassNode();
            new ClassReader(stream).accept(node, ClassReader.SKIP_CODE);
            return node;
        }
    }

    private static void check(boolean condition, String name) {
        if (!condition) throw new AssertionError(name);
        checks++;
    }
}
