package com.mrh0.createaddition.blocks.liquid_blaze_burner;

import net.minecraft.world.item.ItemStack;

/** Result of inserting one fuel container into a liquid blaze burner. */
public record LiquidBlazeBurnerInsertResult(boolean success, ItemStack remainder) {
    public static LiquidBlazeBurnerInsertResult fail() {
        return new LiquidBlazeBurnerInsertResult(false, ItemStack.EMPTY);
    }

    public static LiquidBlazeBurnerInsertResult success(ItemStack remainder) {
        return new LiquidBlazeBurnerInsertResult(true, remainder);
    }
}
