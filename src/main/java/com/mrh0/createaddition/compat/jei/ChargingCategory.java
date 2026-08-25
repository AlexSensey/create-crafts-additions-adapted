package com.mrh0.createaddition.compat.jei;

import com.mrh0.createaddition.CreateAddition;
import com.mrh0.createaddition.index.CABlocks;
import com.mrh0.createaddition.recipe.charging.ChargingRecipe;
import com.mrh0.createaddition.util.Util;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ChargingCategory extends CARecipeCategory<ChargingRecipe> {
	private final AnimatedTeslaCoil teslaCoil = new AnimatedTeslaCoil();

	public ChargingCategory(IGuiHelper guiHelper) {
		super(CreateAddition.asResource("charging"), guiHelper, CABlocks.TESLA_COIL.get(), 177, 53);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ChargingRecipe> holder, IFocusGroup focuses) {
		ChargingRecipe recipe = holder.value();
		builder.addSlot(RecipeIngredientRole.INPUT, 15, 9).setBackground(getRenderedSlot(), -1, -1)
			.add(recipe.getIngredients().getFirst());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 140, 28).setBackground(getRenderedSlot(), -1, -1)
			.add(recipe.getResultStack());
	}

	@Override
	public void draw(RecipeHolder<ChargingRecipe> holder, IRecipeSlotsView slots, GuiGraphicsExtractor graphics,
			double mouseX, double mouseY) {
		AllGuiTextures.JEI_ARROW.render(graphics, 85, 32);
		AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 43, 4);
		AllGuiTextures.JEI_SHADOW.render(graphics, 32, 40);
		teslaCoil.draw(graphics, 48, 27);
		graphics.text(Minecraft.getInstance().font, Util.format(holder.value().energy) + " FE", 86, 9, 0xffffffff);
	}
}
