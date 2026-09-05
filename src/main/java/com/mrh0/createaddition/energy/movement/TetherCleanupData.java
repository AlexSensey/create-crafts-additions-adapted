package com.mrh0.createaddition.energy.movement;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

/** Deferred breaks for stationary endpoints in unloaded chunks. No forced chunk loads. */
public final class TetherCleanupData extends SavedData {
    private final CompoundTag tokens;
    private static final SavedDataType<TetherCleanupData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath("createaddition", "tether_cleanup"),
            () -> new TetherCleanupData(new CompoundTag()),
            CompoundTag.CODEC.xmap(TetherCleanupData::new, data -> data.tokens));

    TetherCleanupData(CompoundTag tokens) { this.tokens = tokens; }

    static TetherCleanupData get(ServerLevel world) { return world.getDataStorage().computeIfAbsent(TYPE); }

    void mark(String token) {
        if (token.isEmpty()) return;
        tokens.putBoolean(token, true);
        setDirty();
    }

    boolean consume(String token) {
        if (token.isEmpty() || !tokens.contains(token)) return false;
        tokens.remove(token);
        setDirty();
        return true;
    }
}
