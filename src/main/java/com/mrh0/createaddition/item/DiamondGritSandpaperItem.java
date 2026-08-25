package com.mrh0.createaddition.item;

import com.mrh0.createaddition.config.CommonConfig;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;

import net.minecraft.world.item.ItemStack;


public class DiamondGritSandpaperItem extends SandPaperItem {
	public DiamondGritSandpaperItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		return CommonConfig.DIAMOND_GRIT_SANDPAPER_USES.get();
	}

}
