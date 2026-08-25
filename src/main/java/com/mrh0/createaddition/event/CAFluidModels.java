package com.mrh0.createaddition.event;

import com.mrh0.createaddition.CreateAddition;
import com.mrh0.createaddition.index.CAFluids;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

/** Registers the explicit fluid render models required by Minecraft 26.2. */
public final class CAFluidModels {
    private CAFluidModels() {}

    public static void register(RegisterFluidModelsEvent event) {
        register(event, CAFluids.SEED_OIL.getSource(), "seed_oil");
        register(event, CAFluids.BIOETHANOL.getSource(), "bioethanol");
    }

    private static void register(RegisterFluidModelsEvent event, BaseFlowingFluid source, String name) {
        FluidModel.Unbaked model = new FluidModel.Unbaked(
                new Material(CreateAddition.asResource("fluid/" + name + "_still")),
                new Material(CreateAddition.asResource("fluid/" + name + "_flow")),
                null,
                state -> 0xffffffff
        );
        event.register(model, source, source.getFlowing());
    }
}
