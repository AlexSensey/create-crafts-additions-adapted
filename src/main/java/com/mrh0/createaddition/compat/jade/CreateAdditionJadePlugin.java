package com.mrh0.createaddition.compat.jade;

import com.mrh0.createaddition.CreateAddition;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ContraptionHandlerClient;
import com.simibubi.create.content.contraptions.ContraptionHandlerClient.ContraptionHit;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

/** Jade support for block entities stored in a moving Create contraption. */
@WailaPlugin(CreateAddition.MODID + ":moving_contraptions")
public class CreateAdditionJadePlugin implements IWailaPlugin {
    private static final MovingContraptionProvider PROVIDER = new MovingContraptionProvider();

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(PROVIDER, AbstractContraptionEntity.class);
        registration.addRayTraceCallback((hitResult, currentAccessor, originalAccessor) -> {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            if (player == null || minecraft.level == null)
                return currentAccessor;

            ContraptionHit hit = ContraptionHandlerClient.findContraptionHit(player);
            if (hit == null)
                return currentAccessor;

            Vec3 globalLocation = hit.entity().toGlobalVector(hit.hitResult().getLocation(), 1);
            return registration.entityAccessor()
                    .level(minecraft.level)
                    .player(player)
                    .serverConnected(registration.isServerConnected())
                    .showDetails(registration.isShowDetailsPressed())
                    .entity(hit.entity())
                    .hit(new EntityHitResult(hit.entity(), globalLocation))
                    .build();
        });
    }

    private static class MovingContraptionProvider implements IEntityComponentProvider {
        private static final Identifier UID = Identifier.fromNamespaceAndPath(
                CreateAddition.MODID, "moving_contraption_block");

        @Override
        public Identifier getUid() {
            return UID;
        }

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
            if (!(accessor.getEntity() instanceof AbstractContraptionEntity entity))
                return;
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null)
                return;

            ContraptionHit hit = ContraptionHandlerClient.findContraptionHit(player);
            if (hit == null || hit.entity() != entity)
                return;

            StructureBlockInfo info = entity.getContraption().getBlocks().get(hit.hitResult().getBlockPos());
            if (info == null)
                return;

            tooltip.clear();
            tooltip.add(info.state().getBlock().getName(), UID);

            BlockEntity blockEntity = entity.getContraption().getOrCreateClientContraptionLazy()
                    .getBlockEntity(hit.hitResult().getBlockPos());
            if (blockEntity instanceof IHaveGoggleInformation goggleInformation) {
                List<Component> details = new ArrayList<>();
                goggleInformation.addToGoggleTooltip(details, player.isShiftKeyDown());
                tooltip.addAll(details);
            }
        }
    }
}
