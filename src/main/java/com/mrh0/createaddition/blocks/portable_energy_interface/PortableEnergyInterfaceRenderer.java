package com.mrh0.createaddition.blocks.portable_energy_interface;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrh0.createaddition.index.CAPartials;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.impl.client.render.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

/** Static and contraption instances are rendered by {@link PEIVisual} and {@link PEIActorVisual}. */
public class PortableEnergyInterfaceRenderer extends SafeBlockEntityRenderer<PortableEnergyInterfaceBlockEntity> {
    public PortableEnergyInterfaceRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(PortableEnergyInterfaceBlockEntity be, float partialTicks, PoseStack poseStack,
                              MultiBufferSource buffers, int light, int overlay) {
        // Native Flywheel visual; intentionally no duplicate immediate rendering.
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
                                           ContraptionMatrices matrices, MultiBufferSource buffers) {
        // Native actor visual handles this path when Flywheel is active.
    }

    static PartialModel getMiddleForState(BlockState state, boolean lit) {
        return lit ? CAPartials.PORTABLE_ENERGY_INTERFACE_MIDDLE_POWERED
                : CAPartials.PORTABLE_ENERGY_INTERFACE_MIDDLE;
    }

    static PartialModel getTopForState(BlockState state) {
        return CAPartials.PORTABLE_ENERGY_INTERFACE_TOP;
    }
}
