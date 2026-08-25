package com.mrh0.createaddition.blocks.electric_motor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueLabelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.block.state.BlockState;

/** The shaft is rendered by Flywheel; this renderer also submits the RPM overlay. */
public class ElectricMotorRenderer extends KineticBlockEntityRenderer<ElectricMotorBlockEntity> {
    public ElectricMotorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected BlockState getRenderedBlockState(ElectricMotorBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState cameraRenderState) {
        if (state instanceof KineticRenderState kineticState
                && kineticState.blockEntity instanceof ElectricMotorBlockEntity be
                && be.generatedSpeed != null) {
            ScrollValueLabelRenderer.submitEjector(be.generatedSpeed, state, poseStack, collector, cameraRenderState);
        }

        // This keeps the non-Flywheel fallback, while the normal Flywheel path exits in super.submit().
        super.submit(state, poseStack, collector, cameraRenderState);
    }
}
