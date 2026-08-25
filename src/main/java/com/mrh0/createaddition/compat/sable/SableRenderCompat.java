package com.mrh0.createaddition.compat.sable;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.impl.client.render.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class SableRenderCompat {
    public static boolean renderConnectorBlockEntity(BlockEntity be, PoseStack poseStack,
            float partialTick, double cameraX, double cameraY, double cameraZ,
            BlockEntityRenderDispatcher dispatcher) {
        // Sable is optional and disabled in this build; the 26.2 renderer submits through render states.
        return false;
    }

    private static boolean needsCustomRender(BlockEntity be) {
        Level level = be.getLevel();
        if (level == null || !(be instanceof ISableRender)) return false;
        return SableUtil.isInSubLevel(level, be.getBlockPos());
    }
}
