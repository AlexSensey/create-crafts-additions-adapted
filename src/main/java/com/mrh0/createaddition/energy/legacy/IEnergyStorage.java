package com.mrh0.createaddition.energy.legacy;

/** Internal integer energy contract; external capabilities use EnergyCompat. */
public interface IEnergyStorage {
    int receiveEnergy(int amount, boolean simulate);
    int extractEnergy(int amount, boolean simulate);
    int getEnergyStored();
    int getMaxEnergyStored();
    boolean canExtract();
    boolean canReceive();
}
