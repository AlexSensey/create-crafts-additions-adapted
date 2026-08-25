package com.mrh0.createaddition.blocks.cake;

import com.mrh0.createaddition.CreateAddition;
import com.mrh0.createaddition.datagen.Models.BlockGenHelper;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;

public class CACakeBlock extends CakeBlock {

	public CACakeBlock(Properties props) {
		super(props);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide()) {
			return InteractionResult.CONSUME;
		}
		return eat(level, pos, state, player);
	}

	protected static InteractionResult eat(LevelAccessor world, BlockPos pos, BlockState state, Player player) {
		if (!player.canEat(false))
			return InteractionResult.PASS;
		else {
			player.awardStat(Stats.EAT_CAKE_SLICE);
			player.getFoodData().eat(3, 0.3F);
			int i = state.getValue(BITES);
			if (i < 6)
				world.setBlock(pos, state.setValue(BITES, Integer.valueOf(i + 1)), 3);
			else
				world.removeBlock(pos, false);
			return InteractionResult.SUCCESS;
		}
	}


	public static <T extends Block> void makeBlockState(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider provider) {
		/* Pre-generated JSON is shipped in resources; NeoForge 26.2 removed these legacy builders.
		BlockModelProvider models = provider.models();
		String basePath = "block/" + BlockGenHelper.getBlockName(ctx.get()) + "/";
		MultiPartBlockStateBuilder builder = provider.getMultipartBuilder(ctx.get());

		for(Integer bytes: BITES.getPossibleValues()) {
			if(bytes == 0) {
				builder.part()
						.modelFile(models.getExistingFile(Identifier.fromNamespaceAndPath(CreateAddition.MODID, basePath + "cake")))
						.addModel()
						.condition(BITES, 0)
						.end();
			}
			else {
				builder.part()
						.modelFile(models.getExistingFile(Identifier.fromNamespaceAndPath(CreateAddition.MODID, basePath + "slice" + bytes)))
						.addModel()
						.condition(BITES, bytes)
						.end();
			}

		}

		*/
	}
}
