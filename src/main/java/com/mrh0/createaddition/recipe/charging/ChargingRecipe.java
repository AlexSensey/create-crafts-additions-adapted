package com.mrh0.createaddition.recipe.charging;

import com.mojang.serialization.MapCodec;
import com.mrh0.createaddition.index.CARecipes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import com.simibubi.create.compat.neoforge263.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

public class ChargingRecipe extends ProcessingRecipe<RecipeWrapper, ChargingRecipeParams> {
    public static final IRecipeTypeInfo TYPE_INFO = new IRecipeTypeInfo() {
        @Override
        public Identifier getId() {
            return CARecipes.CHARGING.getId();
        }

        @Override
        public <T extends RecipeSerializer<?>> T getSerializer() {
            return (T) CARecipes.CHARGING.get();
        }

        @Override
        public <V extends RecipeInput, R extends Recipe<V>> RecipeType<R> getType() {
            return (RecipeType<R>) CARecipes.CHARGING_TYPE.get();
        }
    };

    public int energy;
    public int maxChargeRate;

    public ChargingRecipe(ChargingRecipeParams params) {
        super(TYPE_INFO, params);
        this.energy = params.getEnergy();
        this.maxChargeRate = params.getMaxChargeRate();
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper wrapper, @NotNull Level world) {
        if (ingredients.get(0) == null) return false;
        return ingredients.get(0).test(wrapper.getItem(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 1;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxChargeRate() {
        return maxChargeRate;
    }

    public ItemStack getResultStack() {
        return  getRollableResults().getFirst().getStack();
    }

    @FunctionalInterface
    public interface Factory<R extends ChargingRecipe> extends ProcessingRecipe.Factory<ChargingRecipeParams, R> {
        R create(ChargingRecipeParams params);
    }

    public static class Builder<R extends ChargingRecipe> extends ProcessingRecipeBuilder<ChargingRecipeParams, R, ChargingRecipe.Builder<R>> {
        public Builder(ChargingRecipe.Factory<R> factory, Identifier recipeId) {
            super(factory, recipeId);
        }

        @Override
        protected ChargingRecipeParams createParams() {
            return new ChargingRecipeParams();
        }

        @Override
        public ChargingRecipe.Builder<R> self() {
            return this;
        }

        public ChargingRecipe.Builder<R> enchantedOutput(ItemStack item, ResourceKey<Enchantment> enchantmentKey, HolderLookup.Provider provider) {
            item.enchant(provider.holderOrThrow(enchantmentKey),1);
            return output(item);
        }

        public ChargingRecipe.Builder<R> energy(int energy) {
            params.energy = energy;
            return this;
        }

        public ChargingRecipe.Builder<R> maxChargeRate(int maxChargeRate) {
            params.maxChargeRate = maxChargeRate;
            return this;
        }
    }

    public static class Serializer<R extends ChargingRecipe> {
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(ProcessingRecipe.Factory<ChargingRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, ChargingRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, ChargingRecipeParams.STREAM_CODEC);
        }

        public MapCodec<R> codec() {
            return codec;
        }

        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
            return streamCodec;
        }

        public RecipeSerializer<R> asRecipeSerializer() {
            return new RecipeSerializer<>(codec, streamCodec);
        }
    }

}
