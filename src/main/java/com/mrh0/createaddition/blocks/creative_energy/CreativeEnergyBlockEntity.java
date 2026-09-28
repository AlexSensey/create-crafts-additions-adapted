package com.mrh0.createaddition.blocks.creative_energy;

import com.mrh0.createaddition.energy.CreativeEnergyStorage;

import com.mrh0.createaddition.index.CABlockEntities;
import com.simibubi.create.content.logistics.crate.CrateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import com.mrh0.createaddition.energy.legacy.IEnergyStorage;

import java.util.EnumMap;
import java.util.EnumSet;

public class CreativeEnergyBlockEntity extends CrateBlockEntity {

	protected final CreativeEnergyStorage capability;

	private final EnumSet<Direction> invalidSides = EnumSet.allOf(Direction.class);
	private final EnumMap<Direction, BlockCapabilityCache<net.neoforged.neoforge.transfer.energy.EnergyHandler, Direction>> cache = new EnumMap<>(Direction.class);
	
	public CreativeEnergyBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
		super(tileEntityTypeIn, pos, state);
		capability = new CreativeEnergyStorage();
	}

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
				Capabilities.Energy.BLOCK,
				CABlockEntities.CREATIVE_ENERGY.get(),
				(be, context) -> com.mrh0.createaddition.energy.EnergyCompat.expose(be.capability)
		);
	}
	
	private boolean firstTickState = true;
	
	@Override
	public void tick() {
		super.tick();
		if (level == null) return;
		if (level.isClientSide()) return;
		if (firstTickState) firstTick();
		firstTickState = false;
		
		for (Direction d : Direction.values()) {
			IEnergyStorage ies = com.mrh0.createaddition.energy.EnergyCompat.legacy(cache.get(d).getCapability());
			if (ies == null) continue;
			ies.receiveEnergy(Integer.MAX_VALUE, false);
		}
	}
	
	public void firstTick() {
		updateCache();
	}
	
	public void updateCache() {
		if (level == null) return;
		if (level.isClientSide()) return;
		for (Direction side : Direction.values()) {
			cache.put(side, BlockCapabilityCache.create(
				Capabilities.Energy.BLOCK,
				(ServerLevel) level,
				getBlockPos().relative(side),
				side.getOpposite(),
				() -> !this.isRemoved(),
				() -> invalidSides.add(side)
			));
		}
	}
}
