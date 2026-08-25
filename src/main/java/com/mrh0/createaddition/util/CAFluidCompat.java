package com.mrh0.createaddition.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import com.simibubi.create.foundation.fluid.ResourceHandlerFluidAdapter;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/** Transactional 26.2 capability bridge for the add-on's legacy fluid tanks. */
public final class CAFluidCompat {
    private CAFluidCompat() {}
    public static ResourceHandler<FluidResource> expose(IFluidHandler handler) { return expose(() -> handler); }
    public static ResourceHandler<FluidResource> expose(Supplier<IFluidHandler> handler) { return new LegacyResourceHandler(handler); }
    public static IFluidHandler legacy(ResourceHandler<FluidResource> handler) { return handler == null ? null : new ResourceHandlerFluidAdapter(handler); }

    private static final class LegacyResourceHandler implements ResourceHandler<FluidResource> {
        private final Supplier<IFluidHandler> supplier;
        private final Journal journal = new Journal();
        private LegacyResourceHandler(Supplier<IFluidHandler> supplier) { this.supplier = supplier; }
        private IFluidHandler handler() { return supplier.get(); }
        public int size() { return handler().getTanks(); }
        public FluidResource getResource(int index) { return FluidResource.of(handler().getFluidInTank(index)); }
        public long getAmountAsLong(int index) { return handler().getFluidInTank(index).getAmount(); }
        public long getCapacityAsLong(int index, FluidResource resource) { return !resource.isEmpty() && !isValid(index, resource) ? 0 : handler().getTankCapacity(index); }
        public boolean isValid(int index, FluidResource resource) { return resource.isEmpty() || handler().isFluidValid(index, resource.toStack(1)); }
        public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
            if (resource.isEmpty() || amount <= 0 || !isValid(index, resource)) return 0;
            int inserted = handler().fill(resource.toStack(amount), FluidAction.SIMULATE);
            if (inserted <= 0) return 0;
            journal.updateSnapshots(transaction);
            return handler().fill(resource.toStack(inserted), FluidAction.EXECUTE);
        }
        public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
            if (resource.isEmpty() || amount <= 0) return 0;
            FluidStack extracted = handler().drain(resource.toStack(amount), FluidAction.SIMULATE);
            if (extracted.isEmpty()) return 0;
            journal.updateSnapshots(transaction);
            return handler().drain(extracted, FluidAction.EXECUTE).getAmount();
        }
        private List<FluidStack> snapshot() {
            List<FluidStack> result = new ArrayList<>();
            for (int i = 0; i < handler().getTanks(); i++) result.add(handler().getFluidInTank(i).copy());
            return result;
        }
        private final class Journal extends SnapshotJournal<List<FluidStack>> {
            protected List<FluidStack> createSnapshot() { return snapshot(); }
            protected void revertToSnapshot(List<FluidStack> snapshot) {
                if (handler() instanceof FluidTank tank && !snapshot.isEmpty()) { tank.setFluid(snapshot.getFirst()); return; }
                for (int i = 0; i < handler().getTanks(); i++) {
                    FluidStack current = handler().getFluidInTank(i);
                    if (!current.isEmpty()) handler().drain(current.copyWithAmount(current.getAmount()), FluidAction.EXECUTE);
                }
                for (FluidStack fluid : snapshot) if (!fluid.isEmpty()) handler().fill(fluid.copy(), FluidAction.EXECUTE);
            }
        }
    }
}
