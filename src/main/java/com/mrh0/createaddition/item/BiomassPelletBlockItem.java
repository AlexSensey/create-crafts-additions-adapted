package com.mrh0.createaddition.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class BiomassPelletBlockItem extends BlockItem {

	public BiomassPelletBlockItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties.component(net.minecraft.core.component.DataComponents.COOKING_FUEL,
                new net.minecraft.world.item.component.CookingFuel(
                        new net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt.Constant(57600),
                        new net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat.Constant(1f))));
	}

}
