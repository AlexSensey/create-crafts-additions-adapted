package com.mrh0.createaddition.energy;

import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;

public class NodeMovementBehaviour implements MovementBehaviour {
	@Override
	public void tick(MovementContext context) {
		com.mrh0.createaddition.energy.movement.CreateWireTethers.tick(context);
	}

	@Override
	public boolean mustTickWhileDisabled() { return true; }
	
	@Override
	public void startMoving(MovementContext context) {
		// Mark this tileentity as a contraption.
		context.blockEntityData.putBoolean("contraption", true);
	}
}
