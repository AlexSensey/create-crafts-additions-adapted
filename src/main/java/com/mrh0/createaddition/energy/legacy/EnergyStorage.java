package com.mrh0.createaddition.energy.legacy;

/** Integer storage backing the mod's existing machines and transaction adapter. */
public class EnergyStorage implements IEnergyStorage {
    protected int energy, capacity, maxReceive, maxExtract;
    public EnergyStorage(int capacity) { this(capacity, capacity, capacity, 0); }
    public EnergyStorage(int capacity, int maxTransfer) { this(capacity, maxTransfer, maxTransfer, 0); }
    public EnergyStorage(int capacity, int maxReceive, int maxExtract) { this(capacity, maxReceive, maxExtract, 0); }
    public EnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.clamp(energy, 0, capacity);
    }
    public int receiveEnergy(int amount, boolean simulate) {
        int accepted = canReceive() ? Math.max(0, Math.min(capacity - energy, Math.min(maxReceive, amount))) : 0;
        if (!simulate) energy += accepted;
        return accepted;
    }
    public int extractEnergy(int amount, boolean simulate) {
        int extracted = canExtract() ? Math.max(0, Math.min(energy, Math.min(maxExtract, amount))) : 0;
        if (!simulate) energy -= extracted;
        return extracted;
    }
    public int getEnergyStored() { return energy; }
    public int getMaxEnergyStored() { return capacity; }
    public boolean canExtract() { return maxExtract > 0; }
    public boolean canReceive() { return maxReceive > 0; }
}
