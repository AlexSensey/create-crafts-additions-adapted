package com.mrh0.createaddition.mixin;

import com.mrh0.createaddition.energy.movement.CreateWireTethers;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractContraptionEntity.class, remap = false)
public abstract class ContraptionEntityTetherMixin {
    @Unique private boolean createaddition$placingBlocks;

    @Inject(method = "disassemble", at = @At("HEAD"))
    private void createaddition$beginPlacement(CallbackInfo ci) { createaddition$placingBlocks = true; }

    @Inject(method = "disassemble", at = @At("RETURN"))
    private void createaddition$endPlacement(CallbackInfo ci) { createaddition$placingBlocks = false; }

    @Inject(method = "remove", at = @At("HEAD"))
    private void createaddition$destroyTethers(Entity.RemovalReason reason, CallbackInfo ci) {
        var entity = (AbstractContraptionEntity) (Object) this;
        if (!entity.isRemoved() && reason.shouldDestroy() && !createaddition$placingBlocks)
            CreateWireTethers.destroyed(entity);
    }
}
