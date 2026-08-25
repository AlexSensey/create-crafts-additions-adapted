package com.mrh0.createaddition.compat.jei;

import com.mrh0.createaddition.CreateAddition;
import com.mrh0.createaddition.index.CAItems;
import com.mrh0.createaddition.recipe.liquid_burning.LiquidBurningRecipe;
import com.mrh0.createaddition.util.Util;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import java.util.List;

public class LiquidBurningCategory extends CARecipeCategory<LiquidBurningRecipe> {
	private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();

	public LiquidBurningCategory(IGuiHelper guiHelper) {
		super(CreateAddition.asResource("liquid_burning"), guiHelper, AllBlocks.BLAZE_BURNER.get(), 177, 53);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<LiquidBurningRecipe> holder, IFocusGroup focuses) {
		LiquidBurningRecipe recipe = holder.value();
		builder.addSlot(RecipeIngredientRole.INPUT, 36, 18).setBackground(getRenderedSlot(), -1, -1)
			.add(new ItemStack(CAItems.STRAW.get()));
		List<FluidStack> fluids = recipe.getFluidInput().ingredient().fluids().stream()
			.map(fluid -> new FluidStack(fluid, recipe.getFluidInput().amount())).toList();
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 18).setBackground(getRenderedSlot(), -1, -1)
			.addIngredients(NeoForgeTypes.FLUID_STACK, fluids)
			.setFluidRenderer(recipe.getFluidInput().amount(), false, 16, 16);
	}

	@Override
	public void draw(RecipeHolder<LiquidBurningRecipe> holder, IRecipeSlotsView slots, GuiGraphicsExtractor graphics,
			double mouseX, double mouseY) {
		LiquidBurningRecipe recipe = holder.value();
		HeatCondition heat = recipe.isSuperheated() ? HeatCondition.SUPERHEATED : HeatCondition.HEATED;
		AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 88, 3);
		AllGuiTextures.JEI_LIGHT.render(graphics, 81, 38);
		heater.withHeat(heat.visualizeAsBlazeBurner()).draw(graphics, 91, 35);
		graphics.text(Minecraft.getInstance().font, Util.formatTime(recipe.getBurnTime()), 118, 22, 0xffffffff);
	}
}
