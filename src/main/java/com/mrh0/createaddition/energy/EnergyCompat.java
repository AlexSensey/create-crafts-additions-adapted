package com.mrh0.createaddition.energy;

import com.mrh0.createaddition.energy.legacy.IEnergyStorage;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/** Bridges the legacy Forge Energy API used by CC&A to NeoForge's transactional 26.2 API. */
public final class EnergyCompat {
	private EnergyCompat() {}

	public static EnergyHandler expose(IEnergyStorage storage) {
		return storage == null ? null : new LegacyEnergyHandler(storage);
	}

	public static IEnergyStorage legacy(EnergyHandler handler) {
		return handler == null ? null : new LegacyEnergyView(handler);
	}

	private static final class LegacyEnergyHandler extends SnapshotJournal<Long> implements EnergyHandler {
		private final IEnergyStorage storage;
		private long pendingDelta;

		private LegacyEnergyHandler(IEnergyStorage storage) {
			this.storage = storage;
		}

		@Override
		public long getAmountAsLong() {
			return Math.max(0L, (long) storage.getEnergyStored() + pendingDelta);
		}

		@Override
		public long getCapacityAsLong() {
			return storage.getMaxEnergyStored();
		}

		@Override
		public int insert(int amount, TransactionContext transaction) {
			if (amount <= 0)
				return 0;
			long space = Math.max(0L, getCapacityAsLong() - getAmountAsLong());
			int accepted = (int) Math.min(space, storage.receiveEnergy(amount, true));
			if (accepted > 0) {
				updateSnapshots(transaction);
				pendingDelta += accepted;
			}
			return accepted;
		}

		@Override
		public int extract(int amount, TransactionContext transaction) {
			if (amount <= 0)
				return 0;
			int available = (int) Math.min(getAmountAsLong(), storage.extractEnergy(amount, true));
			if (available > 0) {
				updateSnapshots(transaction);
				pendingDelta -= available;
			}
			return available;
		}

		@Override
		protected Long createSnapshot() {
			return pendingDelta;
		}

		@Override
		protected void revertToSnapshot(Long snapshot) {
			pendingDelta = snapshot;
		}

		@Override
		protected void onRootCommit(Long originalDelta) {
			long committedDelta = pendingDelta - originalDelta;
			if (committedDelta > 0) {
				storage.receiveEnergy((int) Math.min(Integer.MAX_VALUE, committedDelta), false);
			} else if (committedDelta < 0) {
				storage.extractEnergy((int) Math.min(Integer.MAX_VALUE, -committedDelta), false);
			}
			pendingDelta = originalDelta;
		}
	}

	private static final class LegacyEnergyView implements IEnergyStorage {
		private final EnergyHandler handler;

		private LegacyEnergyView(EnergyHandler handler) {
			this.handler = handler;
		}

		@Override
		public int receiveEnergy(int amount, boolean simulate) {
			try (Transaction transaction = Transaction.openRoot()) {
				int inserted = handler.insert(amount, transaction);
				if (!simulate)
					transaction.commit();
				return inserted;
			}
		}

		@Override
		public int extractEnergy(int amount, boolean simulate) {
			try (Transaction transaction = Transaction.openRoot()) {
				int extracted = handler.extract(amount, transaction);
				if (!simulate)
					transaction.commit();
				return extracted;
			}
		}

		@Override public int getEnergyStored() { return handler.getAmountAsInt(); }
		@Override public int getMaxEnergyStored() { return handler.getCapacityAsInt(); }
		@Override public boolean canExtract() { return handler.getCapacityAsLong() > 0; }
		@Override public boolean canReceive() { return handler.getCapacityAsLong() > 0; }
	}
}
