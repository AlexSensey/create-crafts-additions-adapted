package com.mrh0.createaddition.datagen;

import com.mrh0.createaddition.CreateAddition;
import com.mrh0.createaddition.index.CABlocks;
import com.simibubi.create.api.equipment.potatoCannon.PotatoCannonProjectileType;
import com.simibubi.create.api.registry.CreateRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

public class CAPotatoProjectileTypesDatagen {
    public static void bootstrap(BootstrapContext<PotatoCannonProjectileType> bs) {
        register(bs, "chocolate_cake", new PotatoCannonProjectileType.Builder()
                .damage(8)
                .addItems(CABlocks.CHOCOLATE_CAKE.asItem())
                .knockback(0.1f)
                .reloadTicks(15)
                .renderTumbling()
                .sticky()
                .velocity(1.1f)
                .soundPitch(0.8f)
                .build()
        );
        register(bs, "honey_cake", new PotatoCannonProjectileType.Builder()
                .damage(8)
                .addItems(CABlocks.HONEY_CAKE.asItem())
                .knockback(0.1f)
                .reloadTicks(15)
                .renderTumbling()
                .sticky()
                .velocity(1.0f)
                .soundPitch(0.8f)
                .build()
        );
    }

    private static void register(BootstrapContext<PotatoCannonProjectileType> bs, String name, PotatoCannonProjectileType type) {
        bs.register(ResourceKey.create(CreateRegistries.POTATO_PROJECTILE_TYPE, Identifier.fromNamespaceAndPath(CreateAddition.MODID,name)), type);
    }
}
