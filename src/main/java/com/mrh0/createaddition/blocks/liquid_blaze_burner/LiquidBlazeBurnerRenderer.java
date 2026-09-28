package com.mrh0.createaddition.blocks.liquid_blaze_burner;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import dev.engine_room.create_flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.api.animation.LerpedFloat;
import net.createmod.catnip.impl.client.render.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/** Normal-world rendering is owned by {@link LiquidBlazeBurnerVisual}. */
public class LiquidBlazeBurnerRenderer extends SafeBlockEntityRenderer<LiquidBlazeBurnerBlockEntity> {
    public LiquidBlazeBurnerRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(LiquidBlazeBurnerBlockEntity be, float partialTicks, PoseStack poseStack,
                              MultiBufferSource buffers, int light, int overlay) {
        // Native Flywheel visual; intentionally no duplicate immediate rendering.
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
                                           ContraptionMatrices matrices, MultiBufferSource buffers,
                                           LerpedFloat headAngle, boolean conductor) {
        // Contraption rendering is supplied by Flywheel when visualization is active.
    }

    static void renderShared(PoseStack poseStack, @Nullable PoseStack modelTransform, MultiBufferSource buffers,
                             Level level, BlockState state, BlazeBurnerBlock.HeatLevel heatLevel,
                             float animation, float horizontalAngle, boolean canDrawFlame,
                             boolean drawGoggles, int hashCode) {
        // Retained as a compatibility entry point for optional sub-level integrations.
    }

    public static PartialModel getBlazeModel(BlazeBurnerBlock.HeatLevel heatLevel, boolean blockAbove) {
        if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.SEETHING))
            return blockAbove ? AllPartialModels.BLAZE_SUPER_ACTIVE : AllPartialModels.BLAZE_SUPER;
        if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING))
            return blockAbove && heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.KINDLED)
                    ? AllPartialModels.BLAZE_ACTIVE : AllPartialModels.BLAZE_IDLE;
        return AllPartialModels.BLAZE_INERT;
    }
}
