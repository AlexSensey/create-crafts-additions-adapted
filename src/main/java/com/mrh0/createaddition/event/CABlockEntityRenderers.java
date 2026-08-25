package com.mrh0.createaddition.event;

import com.mrh0.createaddition.blocks.alternator.AlternatorRenderer;
import com.mrh0.createaddition.blocks.connector.base.ConnectorRenderer;
import com.mrh0.createaddition.blocks.electric_motor.ElectricMotorRenderer;
import com.mrh0.createaddition.blocks.liquid_blaze_burner.LiquidBlazeBurnerRenderer;
import com.mrh0.createaddition.blocks.modular_accumulator.ModularAccumulatorRenderer;
import com.mrh0.createaddition.blocks.portable_energy_interface.PortableEnergyInterfaceRenderer;
import com.mrh0.createaddition.blocks.redstone_relay.RedstoneRelayRenderer;
import com.mrh0.createaddition.blocks.rolling_mill.RollingMillRenderer;
import com.mrh0.createaddition.blocks.servo_motor.ServoMotorRenderer;
import com.mrh0.createaddition.index.CABlockEntities;
import com.mrh0.createaddition.CreateAddition;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItemRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

/** Client-only renderer registration for Minecraft 26.2's render-state API. */
public final class CABlockEntityRenderers {
    private CABlockEntityRenderers() {}

    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CABlockEntities.ELECTRIC_MOTOR.get(), ElectricMotorRenderer::new);
        event.registerBlockEntityRenderer(CABlockEntities.SERVO_MOTOR.get(), ServoMotorRenderer::new);
        event.registerBlockEntityRenderer(CABlockEntities.ALTERNATOR.get(), AlternatorRenderer::new);
        event.registerBlockEntityRenderer(CABlockEntities.ROLLING_MILL.get(), RollingMillRenderer::new);
        event.registerBlockEntityRenderer(CABlockEntities.SMALL_CONNECTOR.get(), ConnectorRenderer::new);
        event.registerBlockEntityRenderer(CABlockEntities.SMALL_LIGHT_CONNECTOR.get(), ConnectorRenderer::new);
        event.registerBlockEntityRenderer(CABlockEntities.LARGE_CONNECTOR.get(), ConnectorRenderer::new);
        event.registerBlockEntityRenderer(CABlockEntities.REDSTONE_RELAY.get(), RedstoneRelayRenderer::new);
        event.registerBlockEntityRenderer(CABlockEntities.LIQUID_BLAZE_BURNER.get(), LiquidBlazeBurnerRenderer::new);
        event.registerBlockEntityRenderer(CABlockEntities.MODULAR_ACCUMULATOR.get(), ModularAccumulatorRenderer::new);
        event.registerBlockEntityRenderer(CABlockEntities.PORTABLE_ENERGY_INTERFACE.get(), PortableEnergyInterfaceRenderer::new);
    }

    public static void replaceItemModels(ModelEvent.ModifyBakingResult event) {
        var itemModels = event.getBakingResult().itemStackModels();
        var id = CreateAddition.asResource("diamond_grit_sandpaper");
        itemModels.computeIfPresent(id, (key, model) -> new SandPaperItemRenderer(model));
    }
}
