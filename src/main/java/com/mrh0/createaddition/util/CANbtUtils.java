package com.mrh0.createaddition.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/** NBT helpers for the compound-based BlockPos format used by Minecraft 26.2. */
public final class CANbtUtils {
    private CANbtUtils() {}

    public static CompoundTag writeBlockPos(BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("X", pos.getX());
        tag.putInt("Y", pos.getY());
        tag.putInt("Z", pos.getZ());
        return tag;
    }

    public static BlockPos readBlockPos(CompoundTag tag) {
        return new BlockPos(tag.getIntOr("X", 0), tag.getIntOr("Y", 0), tag.getIntOr("Z", 0));
    }

    public static BlockPos readBlockPos(CompoundTag parent, String key) {
        Tag tag = parent.get(key);
        return tag instanceof CompoundTag posTag ? readBlockPos(posTag) : BlockPos.ZERO;
    }
}
