package com.mrh0.createaddition.blocks.servo_motor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.bearing.BearingRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueLabelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

/** The bearing top is rendered by Flywheel; BearingRenderer retains its setting overlay. */
public class ServoMotorRenderer extends BearingRenderer<ServoMotorBlockEntity> {
    public ServoMotorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState cameraRenderState) {
        if (state instanceof KineticRenderState kineticState
                && kineticState.blockEntity instanceof ServoMotorBlockEntity be
                && be.generatedSpeed != null) {
            ScrollValueLabelRenderer.submitEjector(be.generatedSpeed, state, poseStack, collector, cameraRenderState);
        }

        // BearingRenderer submits the movement-mode panel and skips duplicate geometry under Flywheel.
        super.submit(state, poseStack, collector, cameraRenderState);
    }
}
