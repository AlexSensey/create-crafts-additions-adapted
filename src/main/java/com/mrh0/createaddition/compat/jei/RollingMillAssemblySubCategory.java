package com.mrh0.createaddition.compat.jei;

import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class RollingMillAssemblySubCategory extends SequencedAssemblySubCategory {

    AnimatedRollingMill mill;

    public RollingMillAssemblySubCategory() {
        super(20);
        mill = new AnimatedRollingMill();
    }

    // Upstream 1.7.0 RollingRecipe is not an IAssemblyRecipe. This retained
    // compatibility helper does not introduce a new assembly operation.
    public void draw(SequencedRecipe<?> sequencedRecipe, GuiGraphicsExtractor gg, double mouseX, double mouseY, int index) {
        var ms = gg.pose();
        ms.pushMatrix();
        try {
            ms.translate(0, 51.5f);
            ms.scale(.6f, .6f);
            mill.draw(gg, getWidth() / 2, 30);
        } finally {
            ms.popMatrix();
        }
    }
}
