package com.mrh0.createaddition.energy;

import net.minecraft.core.Direction;
import com.mrh0.createaddition.energy.legacy.IEnergyStorage;

import javax.annotation.Nullable;

public interface IEnergyProvider {
    IEnergyStorage getEnergyStorage(@Nullable Direction direction);
}
