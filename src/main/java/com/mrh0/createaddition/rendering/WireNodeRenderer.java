package com.mrh0.createaddition.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrh0.createaddition.config.CommonConfig;
import com.mrh0.createaddition.energy.IWireNode;
import com.mrh0.createaddition.energy.WireType;
import com.mrh0.createaddition.event.ClientEventHandler;
import com.mrh0.createaddition.index.CAPartials;
import com.mrh0.createaddition.compat.sable.SableUtil;
import com.mrh0.createaddition.util.ClientMinecraftWrapper;
import com.mrh0.createaddition.util.Util;
import net.createmod.catnip.api.client.render.CachedBuffers;
import net.createmod.catnip.api.theme.Color;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.LightCoordsUtil;
import net.createmod.catnip.impl.client.render.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import java.util.List;

public class WireNodeRenderer<T extends BlockEntity> extends SafeBlockEntityRenderer<T> {
	public WireNodeRenderer(BlockEntityRendererProvider.Context context) {
		super();
	}

	private static final float HANG = 0.5f;
	private float time = 0f;

	@Override
	public BlockEntityRenderState createRenderState() {
		return new WireRenderState();
	}

	@Override
	public void extractRenderState(T be, BlockEntityRenderState state, float partialTicks, Vec3 cameraPos,
			ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderState.extractBase(be, state, crumblingOverlay);
		if (state instanceof WireRenderState wireState) {
			wireState.blockEntity = be;
			wireState.partialTicks = partialTicks;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void submit(BlockEntityRenderState state, PoseStack stack, SubmitNodeCollector collector,
			CameraRenderState cameraRenderState) {
		if (!(state instanceof WireRenderState wireState) || wireState.blockEntity == null)
			return;
		T be = (T) wireState.blockEntity;
		if (isInvalid(be))
			return;
		submitWires(be, wireState.partialTicks, stack, collector);
	}

	private void submitWires(T be, float partialTicks, PoseStack stack, SubmitNodeCollector collector) {
		IWireNode te = (IWireNode) be;
		Level level = be.getLevel();
		if (level == null)
			return;

		for (int index = 0; index < te.getNodeCount(); index++) {
			if (!te.hasConnection(index))
				continue;
			var saved = te.getLocalNode(index);
			if (saved != null && saved.createTether != null) {
				Vec3 end = com.mrh0.createaddition.energy.movement.CreateWireTethers.renderEndpoint(level, saved.createTether, partialTicks);
				if (end == null) continue;
				Vec3 translation = end.subtract(Vec3.atLowerCornerOf(te.getPos()));
				Vec3 delta = Vec3.atCenterOf(te.getPos()).add(te.getNodeOffset(index)).subtract(end);
				float distance = (float) delta.length();
				stack.pushPose();
				stack.translate(translation.x, translation.y, translation.z);
				submitWire(be, BlockPos.containing(end), stack, collector, (float) delta.x, (float) delta.y, (float) delta.z, saved.getType(), distance);
				if (saved.getType().isFestive()) submitFestiveLights(stack, collector, (float) delta.x, (float) delta.y, (float) delta.z, distance);
				stack.popPose();
				continue;
			}
			IWireNode otherNode = te.getWireNode(index);
			BlockPos otherPos = te.getNodePos(index);
			WireType wireType = te.getNodeType(index);
			if (otherNode == null || otherPos == null || wireType == null)
				continue;
			int otherIndex = te.getOtherNodeIndex(index);
			if (!shouldRenderConnection(te, index, otherNode, otherIndex, level))
				continue;

			Vec3 localOffset = te.getNodeOffset(index);
			Vec3 otherOffset = otherNode.getNodeOffset(otherIndex);
			float tx = otherPos.getX() - te.getPos().getX();
			float ty = otherPos.getY() - te.getPos().getY();
			float tz = otherPos.getZ() - te.getPos().getZ();

			stack.pushPose();
			stack.translate(tx + .5f + otherOffset.x, ty + .5f + otherOffset.y, tz + .5f + otherOffset.z);
			submitWire(be, otherPos, stack, collector,
					-tx - (float) otherOffset.x + (float) localOffset.x,
					-ty - (float) otherOffset.y + (float) localOffset.y,
					-tz - (float) otherOffset.z + (float) localOffset.z,
					wireType, distanceFromZero(tx, ty, tz));
			if (wireType.isFestive())
				submitFestiveLights(stack, collector,
						-tx - (float) otherOffset.x + (float) localOffset.x,
						-ty - (float) otherOffset.y + (float) localOffset.y,
						-tz - (float) otherOffset.z + (float) localOffset.z,
						distanceFromZero(tx, ty, tz));
			stack.popPose();
		}

		if (!ClientEventHandler.clientRenderHeldWire)
			return;
		LocalPlayer player = ClientMinecraftWrapper.getPlayer();
		if (player == null)
			return;
		Util.Triple<BlockPos, Integer, WireType> held = Util.getWireNodeOfSpools(player.getInventory().getSelectedItem());
		if (held == null || !held.a.equals(te.getPos()))
			return;

		Vec3 localOffset = te.getNodeOffset(held.b);
		Vec3 playerPos = player.getPosition(partialTicks);
		float tx = (float) playerPos.x - te.getPos().getX();
		float ty = (float) playerPos.y - te.getPos().getY();
		float tz = (float) playerPos.z - te.getPos().getZ();
		stack.pushPose();
		stack.translate(tx + .5f, ty + .5f, tz + .5f);
		submitWire(be, player.blockPosition(), stack, collector,
				-tx + (float) localOffset.x, -ty + (float) localOffset.y, -tz + (float) localOffset.z,
				held.c, distanceFromZero(tx, ty, tz));
		stack.popPose();
	}

	private static void submitFestiveLights(PoseStack stack, SubmitNodeCollector collector,
			float x, float y, float z, float distance) {
		BlockStateModelPart lightModel = CAPartials.SMALL_LIGHT.get();
		if (lightModel == null)
			return;
		boolean main = x + y + z > 0;
		for (int index = 3; index < 24; index += 3) {
			float part = index / 24f;
			float vertical = y;
			float curve = vertical > 0
					? vertical * part * part
					: vertical - vertical * (1 - part) * (1 - part);
			float sag = hang(part, distance) + LIGHT_Y_OFFSET;
			float fx = x * part;
			float fy = curve + sag;
			float fz = z * part;
			int colorIndex = main ? 2 - (index / 3) % 3 : (index / 3) % 3;
			int color = 0xff000000 | colors[colorIndex].getRGB();

			stack.pushPose();
			stack.translate(fx, fy, fz);
			collector.submitBlockModel(stack, RenderTypes.cutoutMovingBlock(), List.of(lightModel),
					new int[] { color }, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
			stack.popPose();
		}
	}

	private static void submitWire(BlockEntity be, BlockPos other, PoseStack stack,
			SubmitNodeCollector collector, float x, float y, float z, WireType type, float distance) {
		Level level = be.getLevel();
		if (level == null)
			return;
		BlockPos local = be.getBlockPos();
		int block1 = level.getBrightness(LightLayer.BLOCK, local);
		int block2 = level.getBrightness(LightLayer.BLOCK, other);
		int sky1 = level.getBrightness(LightLayer.SKY, local);
		int sky2 = level.getBrightness(LightLayer.SKY, other);
		float inverse = (float) (Mth.fastInvSqrt(Math.max(1.0e-6f, x * x + z * z)) * 0.025F / 2.0F);
		float o1 = z * inverse;
		float o2 = x * inverse;
		BlockState blockState = be.getBlockState();

		collector.submitCustomGeometry(stack, CARenderType.WIRE, (pose, consumer) -> {
			Matrix4f matrix = pose.pose();
			wirePart(consumer, matrix, x, y, z, block2, block1, sky2, sky1,
					0.025F, 0.025F, o1, o2, type, distance, blockState, stack, 0, 1f, 0f, -1f, 0f);
			wirePart(consumer, matrix, x, y, z, block2, block1, sky2, sky1,
					0.025F, 0.0F, o1, o2, type, distance, blockState, stack, 1, 1f, 0f, -1f, 0f);
		});
	}

	@Override
	protected void renderSafe(T be, float partialTicks, PoseStack stack, MultiBufferSource bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		// Rendering is submitted through Minecraft 26.2's render-state API.
		/*
		IWireNode te = (IWireNode) be;
		Level level = be.getLevel();
		if (level == null) return;

		time += partialTicks;

		if (com.mrh0.createaddition.CreateAddition.SABLE_ACTIVE) {
			renderSubLevelAware(te, be, level, partialTicks, stack, bufferIn);
			return;
		}

		for (int i = 0; i < te.getNodeCount(); i++) {
			if (!te.hasConnection(i)) continue;
			Vec3 d1 = te.getNodeOffset(i);
			float ox1 = ((float) d1.x());
			float oy1 = ((float) d1.y());
			float oz1 = ((float) d1.z());

			IWireNode wn = te.getWireNode(i);
			if (wn == null) return;

			Vec3 d2 = wn.getNodeOffset(te.getOtherNodeIndex(i)); // get other
			float ox2 = ((float) d2.x());
			float oy2 = ((float) d2.y());
			float oz2 = ((float) d2.z());
			BlockPos other = te.getNodePos(i);

			float tx = other.getX() - te.getPos().getX();
			float ty = other.getY() - te.getPos().getY();
			float tz = other.getZ() - te.getPos().getZ();
			stack.pushPose();

			float dis = distanceFromZero(tx, ty, tz);

			stack.translate(tx + .5f + ox2, ty + .5f + oy2, tz + .5f + oz2);
			wireRender(
					be,
					other,
					stack,
					bufferIn,
					-tx - ox2 + ox1,
					-ty - oy2 + oy1,
					-tz - oz2 + oz1,
					te.getNodeType(i),
					dis
			);
			stack.popPose();
		}

		if(ClientEventHandler.clientRenderHeldWire) {
			LocalPlayer player = ClientMinecraftWrapper.getPlayer();
			Util.Triple<BlockPos, Integer, WireType> wireNode = Util.getWireNodeOfSpools(player.getInventory().getSelectedItem());
			if(wireNode == null) return;

			BlockPos nodePos = wireNode.a;
			int nodeIndex = wireNode.b;
			WireType wireType = wireNode.c;
			if(!nodePos.equals(te.getPos())) return;

			Vec3 d1 = te.getNodeOffset(nodeIndex);
			float ox1 = ((float) d1.x());
			float oy1 = ((float) d1.y());
			float oz1 = ((float) d1.z());

			Vec3 playerPos = player.getPosition(partialTicks);
			float tx = (float)playerPos.x - te.getPos().getX();
			float ty = (float)playerPos.y - te.getPos().getY();
			float tz = (float)playerPos.z - te.getPos().getZ();
			stack.pushPose();

			float dis = distanceFromZero(tx, ty, tz);

			stack.translate(tx + .5f, ty + .5f, tz + .5f);
			wireRender(
					be,
					player.blockPosition(),
					stack,
					bufferIn,
					-tx + ox1,
					-ty + oy1,
					-tz + oz1,
					wireType,
					dis
			);
			stack.popPose();
		}
		*/
	}

	private void renderSubLevelAware(IWireNode te, BlockEntity be, Level level,
			float partialTicks, PoseStack stack, MultiBufferSource bufferIn) {
		Vec3 localOrigin = Vec3.atLowerCornerOf(te.getPos());
		Vec3 grav = SableUtil.localDown(level, te.getPos(), partialTicks);
		float gdx = (float) grav.x, gdy = (float) grav.y, gdz = (float) grav.z;

		for (int i = 0; i < te.getNodeCount(); i++) {
			if (!te.hasConnection(i)) continue;

			IWireNode remoteNode = te.getWireNode(i);
			BlockPos remotePos = te.getNodePos(i);
			WireType wireType = te.getNodeType(i);
			if (remoteNode == null || remotePos == null || wireType == null) continue;

			int otherIdx = te.getOtherNodeIndex(i);
			if (!shouldRenderConnection(te, i, remoteNode, otherIdx, level)) continue;

			Vec3 localNodePos = Vec3.atCenterOf(te.getPos()).add(te.getNodeOffset(i));
			Vec3 remoteNodePos = SableUtil
					.nodeWireOffsetRelativeTo(level, te.getPos(), remotePos,
							remoteNode.getNodeOffset(otherIdx), partialTicks);

			Vec3 translateTo = remoteNodePos.subtract(localOrigin);
			Vec3 wireDir = localNodePos.subtract(remoteNodePos);
			float dis = (float) localNodePos.distanceTo(remoteNodePos);

			stack.pushPose();
			stack.translate(translateTo.x, translateTo.y, translateTo.z);
			wireRender(be, remotePos, stack, bufferIn,
					(float) wireDir.x, (float) wireDir.y, (float) wireDir.z,
					wireType, dis, gdx, gdy, gdz);
			stack.popPose();
		}

		if (ClientEventHandler.clientRenderHeldWire) {
			LocalPlayer player = ClientMinecraftWrapper.getPlayer();
			if (player == null) return;
			Util.Triple<BlockPos, Integer, WireType> wireNode = Util.getWireNodeOfSpools(player.getInventory().getSelectedItem());
			if (wireNode == null) return;
			if (!wireNode.a.equals(te.getPos())) return;

			int nodeIndex = wireNode.b;
			Vec3 localNodePos = Vec3.atCenterOf(te.getPos()).add(te.getNodeOffset(nodeIndex));
			Vec3 projectedPlayer = SableUtil
					.transformRelativeTo(level, te.getPos(),
							player.getPosition(partialTicks).add(0.5, 0.5, 0.5), partialTicks);

			Vec3 translateTo = projectedPlayer.subtract(localOrigin);
			Vec3 wireDir = localNodePos.subtract(projectedPlayer);
			float dis = (float) localNodePos.distanceTo(projectedPlayer);

			stack.pushPose();
			stack.translate(translateTo.x, translateTo.y, translateTo.z);
			wireRender(be, player.blockPosition(), stack, bufferIn,
					(float) wireDir.x, (float) wireDir.y, (float) wireDir.z,
					wireNode.c, dis, gdx, gdy, gdz);
			stack.popPose();
		}
	}

	private static boolean shouldRenderConnection(IWireNode node, int nodeIdx, IWireNode remoteNode, int remoteIdx, Level level) {
		if (remoteNode instanceof BlockEntity remoteEntity) {
			Level remoteLevel = remoteEntity.getLevel();
			if (remoteLevel != null && remoteLevel != level) return true;
		}
		int cmp = node.getPos().compareTo(remoteNode.getPos());
		if (cmp != 0) return cmp < 0;
		if (nodeIdx != remoteIdx) return nodeIdx < remoteIdx;
		return System.identityHashCode(node) < System.identityHashCode(remoteNode);
	}



	private static float divf(int a, int b) {
		return (float) a / (float) b;
	}

	private static float hang(float f, float dis) {
		return (float) Math.sin(-f * (float) Math.PI) * (HANG * dis / (float) CommonConfig.SMALL_CONNECTOR_MAX_LENGTH.get());
	}

	public static float distanceFromZero(float x, float y, float z) {
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}

	public static void wireRender(BlockEntity tileEntityIn, BlockPos other, PoseStack stack, MultiBufferSource buffer, float x, float y, float z,
			WireType type, float dis) {
		wireRender(tileEntityIn, other, stack, buffer, x, y, z, type, dis, 0f, -1f, 0f);
	}

	public static void wireRender(BlockEntity tileEntityIn, BlockPos other, PoseStack stack, MultiBufferSource buffer, float x, float y, float z,
			WireType type, float dis, float gdx, float gdy, float gdz) {
		VertexConsumer ivertexbuilder = buffer.getBuffer(CARenderType.WIRE);
		Matrix4f matrix4f = stack.last().pose();
		float f = (float) (Mth.fastInvSqrt(x * x + z * z) * 0.025F / 2.0F);
		float o1 = z * f;
		float o2 = x * f;
		BlockPos blockpos1 = tileEntityIn.getBlockPos();
		BlockPos blockpos2 = other;

		int i = tileEntityIn.getLevel().getBrightness(LightLayer.BLOCK, blockpos1);
		int j = tileEntityIn.getLevel().getBrightness(LightLayer.BLOCK, blockpos2);
		int k = tileEntityIn.getLevel().getBrightness(LightLayer.SKY, blockpos1);
		int l = tileEntityIn.getLevel().getBrightness(LightLayer.SKY, blockpos2);
		wirePart(ivertexbuilder, matrix4f, x, y, z, j, i, l, k, 0.025F, 0.025F, o1, o2, type, dis, tileEntityIn.getBlockState(), stack, 0, 1f, gdx, gdy, gdz);
		wirePart(ivertexbuilder, matrix4f, x, y, z, j, i, l, k, 0.025F, 0.0F, o1, o2, type, dis, tileEntityIn.getBlockState(), stack, 1, 1f, gdx, gdy, gdz);
	}

	public static void wirePart(VertexConsumer vertBuilder, Matrix4f matrix, float x, float y, float z, int l1, int l2,
			int l3, int l4, float a, float b, float o1, float o2, WireType type, float dis, BlockState state, PoseStack stack, int lightOffset, float hangFactor,
			float gdx, float gdy, float gdz) {
		for (int j = 0; j < 24; ++j) {
			float f = (float) j / 23.0F;
			int k = (int) Mth.lerp(f, (float) l1, (float) l2);
			int l = (int) Mth.lerp(f, (float) l3, (float) l4);
			int light = LightCoordsUtil.pack(k, l);

			wireVert(vertBuilder, matrix, light, x, y, z, a, b, 24, j, false, o1, o2, type, dis, state, stack, lightOffset, hangFactor, gdx, gdy, gdz);
			wireVert(vertBuilder, matrix, light, x, y, z, a, b, 24, j + 1, true, o1, o2, type, dis, state, stack, lightOffset+1, hangFactor, gdx, gdy, gdz);
		}

		if (type.isFestive()) {
			stack.pushPose();
			boolean main = x + y + z > 0;
			for (int j = 0; j < 24; ++j) {
				lights(vertBuilder, x, y, z, a, b, 24, j + 1, o1, o2, type, dis, state, stack, lightOffset, main, gdx, gdy, gdz);
			}
			stack.popPose();
		}
	}

	static Color[] colors = {Color.RED, Color.GREEN, new Color(0f, 0f, 1f, 1f)};
	static float LIGHT_Y_OFFSET = -0.03f;

	public static void lights(VertexConsumer vertBuilder, float x, float y, float z,
			float a, float b, int count, int index, float o1, float o2, WireType type, float dis, BlockState state, PoseStack stack, int lightOffset, boolean main,
			float gdx, float gdy, float gdz) {
		float part = (float) index / (float) count;
		float v = x * (-gdx) + y * (-gdy) + z * (-gdz);
		float catenaryScalar = (v > 0.0F ? v * part * part : v - v * (1.0F - part) * (1.0F - part));
		float hangScalar = hang(divf(index, count), dis) + LIGHT_Y_OFFSET;
		float fx = (catenaryScalar + hangScalar) * (-gdx) + (x - v * (-gdx)) * part;
		float fy = (catenaryScalar + hangScalar) * (-gdy) + (y - v * (-gdy)) * part;
		float fz = (catenaryScalar + hangScalar) * (-gdz) + (z - v * (-gdz)) * part;

		// Festive bulbs are submitted separately by the 26.2 renderer path.
	}

	public static void supports(VertexConsumer vertBuilder,float x, float y, float z,
			float a, float b, int count, int index, float o1, float o2, WireType type, float dis, BlockState state, PoseStack stack, int lightOffset, boolean main) {
		float part = (float) index / (float) count;
		float fx = x * part;
		float fyh = (y > 0.0F ? y * part * part : y - y * (1.0F - part) * (1.0F - part)) + 2*hang(divf(index, count), dis);
		float fy = (y > 0.0F ? y * part * part : y - y * (1.0F - part) * (1.0F - part));
		float fz = z * part;

		// Festive supports are submitted separately by the 26.2 renderer path.
	}

	public static void wireVert(VertexConsumer vertBuilder, Matrix4f matrix, int light, float x, float y, float z,
			float a, float b, int count, int index, boolean sw, float o1, float o2, WireType type, float dis, BlockState state, PoseStack stack, int lightOffset, float hangFactor,
			float gdx, float gdy, float gdz) {
		int cr = type.getRed();
		int cg = type.getGreen();
		int cb = type.getBlue();
		if (index % 2 == 0) {
			cr *= 0.7F;
			cg *= 0.7F;
			cb *= 0.7F;
		}

		float part = (float) index / (float) count;
		// Component of wire direction along "up" (-gravity), used for catenary shaping
		float v = x * (-gdx) + y * (-gdy) + z * (-gdz);
		float catenaryScalar = (v > 0.0F ? v * part * part : v - v * (1.0F - part) * (1.0F - part));
		float hangScalar = hangFactor * hang(divf(index, count), dis);
		float fx = (catenaryScalar + hangScalar) * (-gdx) + (x - v * (-gdx)) * part;
		float fy = (catenaryScalar + hangScalar) * (-gdy) + (y - v * (-gdy)) * part;
		float fz = (catenaryScalar + hangScalar) * (-gdz) + (z - v * (-gdz)) * part;

		//System.out.println((fx + o1) +":"+ (fy + n1 - n2) +":"+ (fz - o2));


		if(Math.abs(x) + Math.abs(z) < Math.abs(y)) {
			boolean p = b > 0;
			float c = 0.015f;

			if (!sw) {
				vertBuilder.addVertex(matrix, fx -c, fy, fz + (p?-c:c)).setColor(cr, cg, cb, 255).setLight(light);
			}

			vertBuilder.addVertex(matrix, fx + c, fy, fz + (p?c:-c)).setColor(cr, cg, cb, 255).setLight(light);
			if (sw) {
				vertBuilder.addVertex(matrix, fx -c, fy, fz + (p?-c:c)).setColor(cr, cg, cb, 255).setLight(light);
			}
		}
		else {
			if (!sw) {
				vertBuilder.addVertex(matrix, fx + o1, fy + a - b, fz - o2).setColor(cr, cg, cb, 255).setLight(light);
			}

			vertBuilder.addVertex(matrix, fx - o1, fy + b, fz + o2).setColor(cr, cg, cb, 255).setLight(light);
			if (sw) {
				vertBuilder.addVertex(matrix, fx + o1, fy + a - b, fz - o2).setColor(cr, cg, cb, 255).setLight(light);
			}
		}
	}

	private static class WireRenderState extends BlockEntityRenderState {
		private BlockEntity blockEntity;
		private float partialTicks;
	}
}
