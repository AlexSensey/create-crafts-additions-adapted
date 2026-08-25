package com.mrh0.createaddition.network;

import com.mrh0.createaddition.blocks.modular_accumulator.ModularAccumulatorBlockEntity;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleObservePayload(final ObservePacketPayload pkt, final IPayloadContext ctx) {

    }

    public static void handleEnergyNetworkPayload(final EnergyNetworkPacketPayload pkt, final IPayloadContext ctx) {
        EnergyNetworkPacketPayload.updateClientCache(pkt.pos(), pkt.demand(), pkt.buff());
    }

    public static void handleTimeRemainingPayload(final TimeRemainingPacketPayload pkt, final IPayloadContext ctx) {
        TimeRemainingPacketPayload.updateClientCache(pkt.timeRemaining());
    }

    public static void handleMovingAccumulatorPayload(final MovingAccumulatorPacketPayload pkt,
                                                       final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level == null)
                return;
            Entity entity = minecraft.level.getEntity(pkt.entityId());
            if (!(entity instanceof AbstractContraptionEntity contraptionEntity)
                    || contraptionEntity.getContraption() == null)
                return;

            StructureBlockInfo info = contraptionEntity.getContraption().getBlocks().get(pkt.localPos());
            if (info != null && info.nbt() != null) {
                CompoundTag energyTag = info.nbt().getCompoundOrEmpty("EnergyContent");
                energyTag.putInt("energy", pkt.energy());
                info.nbt().put("EnergyContent", energyTag);
                info.nbt().putInt("EnergyCapacity", pkt.capacity());
            }

            BlockEntity blockEntity = contraptionEntity.getContraption()
                    .getOrCreateClientContraptionLazy().getBlockEntity(pkt.localPos());
            if (blockEntity instanceof ModularAccumulatorBlockEntity accumulator)
                accumulator.applyMovingEnergy(pkt.energy(), pkt.capacity());
        });
    }
}
