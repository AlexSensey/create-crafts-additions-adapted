package com.mrh0.createaddition.compat.jei;

import com.mrh0.createaddition.CreateAddition;
import com.mrh0.createaddition.index.CABlocks;
import com.mrh0.createaddition.recipe.rolling.RollingRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.crafting.RecipeHolder;

public class RollingMillCategory extends CARecipeCategory<RollingRecipe> {
	private final AnimatedRollingMill rollingMill = new AnimatedRollingMill();

	public RollingMillCategory(IGuiHelper guiHelper) {
		super(CreateAddition.asResource("rolling"), guiHelper, CABlocks.ROLLING_MILL.get(), 177, 53);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RollingRecipe> holder, IFocusGroup focuses) {
		RollingRecipe recipe = holder.value();
		builder.addSlot(RecipeIngredientRole.INPUT, 15, 9).setBackground(getRenderedSlot(), -1, -1)
			.add(recipe.getIngredient());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 140, 28).setBackground(getRenderedSlot(), -1, -1)
			.add(recipe.getResultStack());
	}

	@Override
	public void draw(RecipeHolder<RollingRecipe> holder, IRecipeSlotsView slots, GuiGraphicsExtractor graphics,
			double mouseX, double mouseY) {
		AllGuiTextures.JEI_ARROW.render(graphics, 85, 32);
		AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 43, 4);
		AllGuiTextures.JEI_SHADOW.render(graphics, 32, 40);
		rollingMill.draw(graphics, 48, 27);
	}
}
