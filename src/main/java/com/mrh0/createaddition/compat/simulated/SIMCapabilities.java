package com.mrh0.createaddition.compat.simulated;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class SIMCapabilities {
    public static void registerBECapabilities(RegisterCapabilitiesEvent event) {

        var dockHolder = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(
                ResourceKey.create(
                        Registries.BLOCK_ENTITY_TYPE,
                        Identifier.fromNamespaceAndPath("simulated", "docking_connector")
                )
        );

        if (dockHolder.isEmpty()) {
            return;
        }
        var dockType = dockHolder.get().value();
        event.registerBlockEntity(
                Capabilities.Energy.BLOCK,
                dockType,
                (be, side) -> com.mrh0.createaddition.energy.EnergyCompat.expose(
                        ((DockingConnectorBEAccess) be).getEnergyStorage())
        );
    }
}
