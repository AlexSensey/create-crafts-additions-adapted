package com.mrh0.createaddition.compat.jei;

import com.mrh0.createaddition.index.CABlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Direction;

public class AnimatedRollingMill extends AnimatedKinetics {

	@Override
	protected void drawAnimation(GuiGraphicsExtractor graphics, int xOffset, int yOffset) {
		int scale = 22;
		blockElement(shaft(Direction.Axis.Z))
			.rotateBlock(0, 0, -getCurrentAngle())
			.scale(scale)
			.at(xOffset - 2, yOffset + 18)
			.submit(graphics);
		blockElement(shaft(Direction.Axis.Z))
			.atLocal(0f, -4f/16F, 0f)
			.rotateBlock(0, 0, getCurrentAngle())
			.scale(scale)
			.at(xOffset - 2, yOffset + 18)
			.submit(graphics);
		blockElement(CABlocks.ROLLING_MILL.getDefaultState())
			.scale(scale)
			.at(xOffset - 2, yOffset + 18)
			.submit(graphics);
	}

	@Override protected float getGlobalXRotation() { return -22.5f; }
	@Override protected float getGlobalYRotation() { return 22.5f; }
}
