package com.mrh0.createaddition.blocks.alternator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.impl.client.render.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

/** The rotating shafts are owned by {@link AlternatorVisual} when Flywheel is enabled. */
public class AlternatorRenderer extends SafeBlockEntityRenderer<AlternatorBlockEntity> {
    public AlternatorRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(AlternatorBlockEntity be, float partialTicks, PoseStack poseStack,
                              MultiBufferSource buffers, int light, int overlay) {
        // Native Flywheel visual; intentionally no duplicate immediate rendering.
    }
}
