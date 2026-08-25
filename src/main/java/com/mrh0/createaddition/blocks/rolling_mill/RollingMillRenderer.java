package com.mrh0.createaddition.blocks.rolling_mill;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.impl.client.render.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

/** Both rollers are rendered by {@link RollingMillVisual}. */
public class RollingMillRenderer extends SafeBlockEntityRenderer<RollingMillBlockEntity> {
    public RollingMillRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(RollingMillBlockEntity be, float partialTicks, PoseStack poseStack,
                              MultiBufferSource buffers, int light, int overlay) {
        // Native Flywheel visual; intentionally no duplicate immediate rendering.
    }
}
