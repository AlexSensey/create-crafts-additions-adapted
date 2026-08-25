package com.mrh0.createaddition.datagen.RecipeGen;

import com.mrh0.createaddition.recipe.liquid_burning.LiquidBurningRecipe;
import com.mrh0.createaddition.recipe.liquid_burning.LiquidBurningRecipeParams;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;

import static com.mrh0.createaddition.recipe.liquid_burning.LiquidBurningRecipe.TYPE_INFO;


public class LiquidBurningRecipeGen extends ProcessingRecipeGen<LiquidBurningRecipeParams, LiquidBurningRecipe, LiquidBurningRecipe.Builder<LiquidBurningRecipe>> {
    public LiquidBurningRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return TYPE_INFO;
    }

    @Override
    protected LiquidBurningRecipe.Builder<LiquidBurningRecipe> getBuilder(Identifier id) {
        return new LiquidBurningRecipe.Builder<>(LiquidBurningRecipe::new, id);
    }
}
