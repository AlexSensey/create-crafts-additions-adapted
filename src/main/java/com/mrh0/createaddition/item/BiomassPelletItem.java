package com.mrh0.createaddition.item;

import net.minecraft.world.item.Item;

public class BiomassPelletItem extends Item {

	public BiomassPelletItem(Properties props) {
		super(props.component(net.minecraft.core.component.DataComponents.COOKING_FUEL,
                new net.minecraft.world.item.component.CookingFuel(
                        new net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt.Constant(6400),
                        new net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat.Constant(1f))));
	}

}
