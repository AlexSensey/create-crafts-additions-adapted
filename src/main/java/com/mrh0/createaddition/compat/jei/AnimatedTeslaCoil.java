package com.mrh0.createaddition.compat.jei;

import com.mrh0.createaddition.blocks.tesla_coil.TeslaCoilBlock;
import com.mrh0.createaddition.index.CABlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Direction;

public class AnimatedTeslaCoil extends AnimatedKinetics {

	@Override
	protected void drawAnimation(GuiGraphicsExtractor graphics, int xOffset, int yOffset) {
		blockElement(CABlocks.TESLA_COIL.getDefaultState()
				.setValue(TeslaCoilBlock.FACING, Direction.DOWN)
				.setValue(TeslaCoilBlock.POWERED, true))
			.scale(22)
			.at(xOffset - 2, yOffset + 18)
			.submit(graphics);
	}
	@Override protected float getGlobalXRotation() { return 22.5f; }
	@Override protected float getGlobalYRotation() { return 22.5f; }
}
