package com.mrh0.createaddition.index;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

import net.minecraft.resources.Identifier;

import static com.simibubi.create.foundation.block.connected.CTSpriteShifter.getCT;
import static com.simibubi.create.foundation.block.connected.AllCTTypes.OMNIDIRECTIONAL;
import static com.simibubi.create.foundation.block.connected.AllCTTypes.RECTANGLE;

import com.mrh0.createaddition.CreateAddition;

public class CASpriteShifts {
	//public static final CTSpriteShiftEntry OVERCHARGED_CASING = getCT(OMNIDIRECTIONAL,  new Identifier(CreateAddition.MODID, "block/overcharged_casing/overcharged_casing"), new Identifier(CreateAddition.MODID, "block/overcharged_casing/overcharged_casing_connected"));
	public static final CTSpriteShiftEntry
		ACCUMULATOR = getCT(
				RECTANGLE,
				Identifier.fromNamespaceAndPath(CreateAddition.MODID, "block/modular_accumulator/block"),
				Identifier.fromNamespaceAndPath(CreateAddition.MODID, "block/modular_accumulator/block_connected")
			),
		ACCUMULATOR_TOP = getCT(
				RECTANGLE,
				Identifier.fromNamespaceAndPath(CreateAddition.MODID, "block/modular_accumulator/block_top"),
				Identifier.fromNamespaceAndPath(CreateAddition.MODID, "block/modular_accumulator/block_top_connected")
			),

		COPPER_WIRE_CASING = getCT(
				OMNIDIRECTIONAL,
			Identifier.fromNamespaceAndPath(CreateAddition.MODID, "block/copper_wire_casing/block"),
			Identifier.fromNamespaceAndPath(CreateAddition.MODID, "block/copper_wire_casing/block_connected")
		);
}
