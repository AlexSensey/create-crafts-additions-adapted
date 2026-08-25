package com.mrh0.createaddition.network;

import com.mrh0.createaddition.CreateAddition;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/** Synchronizes energy stored in an accumulator controller mounted on a contraption. */
public record MovingAccumulatorPacketPayload(int entityId, BlockPos localPos, int energy, int capacity)
		implements CustomPacketPayload {
	public static final Type<MovingAccumulatorPacketPayload> TYPE =
		new Type<>(CreateAddition.asResource("moving_accumulator"));

	public static final StreamCodec<ByteBuf, MovingAccumulatorPacketPayload> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, MovingAccumulatorPacketPayload::entityId,
		BlockPos.STREAM_CODEC, MovingAccumulatorPacketPayload::localPos,
		ByteBufCodecs.VAR_INT, MovingAccumulatorPacketPayload::energy,
		ByteBufCodecs.VAR_INT, MovingAccumulatorPacketPayload::capacity,
		MovingAccumulatorPacketPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
