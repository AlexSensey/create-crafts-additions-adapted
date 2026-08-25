package com.mrh0.createaddition.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;

public abstract class CARecipeCategory<T extends Recipe<?>> implements IRecipeCategory<RecipeHolder<T>> {
	private final RecipeType<RecipeHolder<T>> recipeType;
	private final Component title;
	private final IDrawable icon;
	private final IDrawable slot;
	private final int width;
	private final int height;

	protected CARecipeCategory(Identifier id, IGuiHelper guiHelper, ItemLike icon, int width, int height) {
		this.recipeType = RecipeType.createRecipeHolderType(id);
		this.title = Component.translatable(id.getNamespace() + ".recipe." + id.getPath());
		this.icon = guiHelper.createDrawableItemStack(new ItemStack(icon));
		this.slot = guiHelper.getSlotDrawable();
		this.width = width;
		this.height = height;
	}

	@Override public RecipeType<RecipeHolder<T>> getRecipeType() { return recipeType; }
	@Override public Component getTitle() { return title; }
	@Override public IDrawable getIcon() { return icon; }
	@Override public int getWidth() { return width; }
	@Override public int getHeight() { return height; }
	protected IDrawable getRenderedSlot() { return slot; }
	@Override public Identifier getIdentifier(RecipeHolder<T> holder) { return holder.id().identifier(); }
	@Override public abstract void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<T> holder, IFocusGroup focuses);
	@Override public void draw(RecipeHolder<T> holder, IRecipeSlotsView slots, GuiGraphicsExtractor graphics,
			double mouseX, double mouseY) {}
}
