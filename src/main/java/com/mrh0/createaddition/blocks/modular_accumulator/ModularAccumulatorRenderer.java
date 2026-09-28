package com.mrh0.createaddition.blocks.modular_accumulator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrh0.createaddition.index.CAPartials;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.api.data.Iterate;
import net.createmod.catnip.impl.client.render.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ModularAccumulatorRenderer extends SafeBlockEntityRenderer<ModularAccumulatorBlockEntity> {
    public ModularAccumulatorRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public BlockEntityRenderState createRenderState() {
        return new AccumulatorRenderState();
    }

    @Override
    public void extractRenderState(ModularAccumulatorBlockEntity be, BlockEntityRenderState state,
                                   float partialTicks, Vec3 cameraPos,
                                   ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderState.extractBase(be, state, crumblingOverlay);
        if (state instanceof AccumulatorRenderState accumulatorState) {
            accumulatorState.blockEntity = be;
            accumulatorState.progress = be.gauge.getValue(partialTicks);
        }
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState cameraRenderState) {
        if (!(state instanceof AccumulatorRenderState accumulatorState))
            return;
        ModularAccumulatorBlockEntity be = accumulatorState.blockEntity;
        if (be == null || isInvalid(be) || !be.isController())
            return;

        BlockStateModelPart gauge = CAPartials.ACCUMULATOR_GUAGE.get();
        BlockStateModelPart dial = CAPartials.ACCUMULATOR_DIAL.get();
        if (gauge == null || dial == null)
            return;

        be.observe();
        poseStack.pushPose();
        poseStack.translate(be.width / 2f, be.height - .5f, be.width / 2f);
        for (Direction direction : Iterate.horizontalDirections) {
            poseStack.pushPose();
            poseStack.rotate(Axis.YP.rotationDegrees(direction.toYRot()));
            poseStack.translate(-.5f, -.5f, -.5f);
            poseStack.translate(be.width / 2f - 6f / 16f, 0, 0);
            collector.submitBlockModel(poseStack, RenderTypes.solidMovingBlock(), List.of(gauge),
                    BlockModelRenderState.EMPTY_TINTS, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

            poseStack.translate(0, 6f / 16f, 8f / 16f);
            poseStack.rotate(Axis.XP.rotationDegrees(-180 * accumulatorState.progress));
            poseStack.translate(0, -6f / 16f, -8f / 16f);
            collector.submitBlockModel(poseStack, RenderTypes.solidMovingBlock(), List.of(dial),
                    BlockModelRenderState.EMPTY_TINTS, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    @Override
    protected void renderSafe(ModularAccumulatorBlockEntity be, float partialTicks, PoseStack poseStack,
                              MultiBufferSource buffers, int light, int overlay) {
        // Rendering is submitted through Minecraft 26.2's render-state API.
    }

    private static class AccumulatorRenderState extends BlockEntityRenderState {
        private ModularAccumulatorBlockEntity blockEntity;
        private float progress;
    }
}
