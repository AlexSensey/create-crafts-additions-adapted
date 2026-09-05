package com.mrh0.createaddition.mixin;

import com.mrh0.createaddition.energy.movement.CreateWireTethers;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.nbt.CompoundTag;
import java.util.Map;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Contraption.class, remap = false)
public abstract class ContraptionMixin {
	@Shadow protected Map<BlockPos, CompoundTag> updateTags;
    @Inject(method = "removeBlocksFromWorld", at = @At("HEAD"))
    private void createaddition$captureWires(Level world, BlockPos offset, CallbackInfo ci) {
        CreateWireTethers.capture((Contraption) (Object) this, world, offset, updateTags);
    }
}
