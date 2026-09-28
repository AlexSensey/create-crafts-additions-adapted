package com.mrh0.createaddition.blocks.rolling_mill;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.create_flywheel.api.instance.Instance;
import dev.engine_room.create_flywheel.api.visualization.VisualizationContext;
import dev.engine_room.create_flywheel.lib.model.Models;
import java.util.function.Consumer;

public class RollingMillVisual extends KineticBlockEntityVisual<RollingMillBlockEntity> {
    private static final float UPPER_ROLLER_OFFSET = 4f / 16f;

    protected final RotatingInstance lowerRoller;
    protected final RotatingInstance upperRoller;

    public RollingMillVisual(VisualizationContext context, RollingMillBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        var axis = rotationAxis();

        var instancer = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT));

        lowerRoller = instancer.createInstance()
                .setup(blockEntity, axis, blockEntity.getSpeed())
                .setPosition(getVisualPosition())
                .rotateToFace(axis);
        lowerRoller.setChanged();

        upperRoller = instancer.createInstance()
                .setup(blockEntity, axis, -blockEntity.getSpeed())
                .setPosition(getVisualPosition())
                .nudge(0, UPPER_ROLLER_OFFSET, 0)
                .rotateToFace(axis);
        upperRoller.setChanged();
    }

    @Override
    public void update(float partialTick) {
        var axis = rotationAxis();
        lowerRoller.setup(blockEntity, axis, blockEntity.getSpeed()).setChanged();
        upperRoller.setup(blockEntity, axis, -blockEntity.getSpeed()).setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(lowerRoller, upperRoller);
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(lowerRoller);
        consumer.accept(upperRoller);
    }

    @Override
    protected void _delete() {
        lowerRoller.delete();
        upperRoller.delete();
    }
}
