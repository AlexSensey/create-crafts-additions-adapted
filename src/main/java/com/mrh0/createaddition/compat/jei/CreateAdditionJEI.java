package com.mrh0.createaddition.compat.jei;

import com.mrh0.createaddition.CreateAddition;
import com.mrh0.createaddition.index.CABlocks;
import com.mrh0.createaddition.index.CAItems;
import com.mrh0.createaddition.index.CARecipes;
import com.mrh0.createaddition.recipe.charging.ChargingRecipe;
import com.mrh0.createaddition.recipe.liquid_burning.LiquidBurningRecipe;
import com.mrh0.createaddition.recipe.rolling.RollingRecipe;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.recipe.CreateRecipeClientCache;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JeiPlugin
public class CreateAdditionJEI implements IModPlugin {
	private static final Identifier ID = CreateAddition.asResource("jei_plugin");
	private ChargingCategory charging;
	private RollingMillCategory rolling;
	private LiquidBurningCategory liquidBurning;

	@Override @Nonnull public Identifier getPluginUid() { return ID; }

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		var guiHelper = registration.getJeiHelpers().getGuiHelper();
		charging = new ChargingCategory(guiHelper);
		rolling = new RollingMillCategory(guiHelper);
		liquidBurning = new LiquidBurningCategory(guiHelper);
		registration.addRecipeCategories(charging, rolling, liquidBurning);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		Collection<RecipeHolder<?>> loaded = getLoadedRecipes();
		registration.addRecipes(charging.getRecipeType(), collect(loaded, CARecipes.CHARGING_TYPE.get(), ChargingRecipe.class));
		registration.addRecipes(rolling.getRecipeType(), collect(loaded, CARecipes.ROLLING_TYPE.get(), RollingRecipe.class));
		registration.addRecipes(liquidBurning.getRecipeType(), collect(loaded, CARecipes.LIQUID_BURNING_TYPE.get(), LiquidBurningRecipe.class));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addCraftingStation(charging.getRecipeType(), CABlocks.TESLA_COIL.get());
		registration.addCraftingStation(rolling.getRecipeType(), CABlocks.ROLLING_MILL.get());
		registration.addCraftingStation(liquidBurning.getRecipeType(), AllBlocks.BLAZE_BURNER.get());
		registration.getJeiHelpers().getRecipeType(Identifier.fromNamespaceAndPath("create", "sandpaper_polishing"))
			.ifPresent(type -> registration.addCraftingStation(type, CAItems.DIAMOND_GRIT_SANDPAPER.get()));
	}

	private static Collection<RecipeHolder<?>> getLoadedRecipes() {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.hasSingleplayerServer() && minecraft.getSingleplayerServer() != null)
			return minecraft.getSingleplayerServer().getRecipeManager().getRecipes();
		return CreateRecipeClientCache.getRecipes();
	}

	private static <T extends Recipe<?>> List<RecipeHolder<T>> collect(Collection<RecipeHolder<?>> loaded,
			net.minecraft.world.item.crafting.RecipeType<T> type, Class<T> recipeClass) {
		List<RecipeHolder<T>> result = new ArrayList<>();
		for (RecipeHolder<?> holder : loaded) {
			Recipe<?> recipe = holder.value();
			if (recipe.getType() == type && recipeClass.isInstance(recipe))
				result.add(new RecipeHolder<>(holder.id(), recipeClass.cast(recipe)));
		}
		return result;
	}
}
