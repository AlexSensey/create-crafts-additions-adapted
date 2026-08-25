package com.mrh0.createaddition.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

/** Bridges Create's CompoundTag persistence hooks to Minecraft 26.2 Value I/O. */
public final class CAValueIO {
    private CAValueIO() {}

    public static CompoundTag write(ValueIOSerializable value, HolderLookup.Provider registries) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        value.serialize(output);
        return output.buildResult();
    }

    public static void read(ValueIOSerializable value, HolderLookup.Provider registries, CompoundTag tag) {
        value.deserialize(TagValueInput.create(ProblemReporter.DISCARDING, registries, tag));
    }
}
