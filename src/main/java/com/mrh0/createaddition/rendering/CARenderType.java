package com.mrh0.createaddition.rendering;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mrh0.createaddition.CreateAddition;
import net.createmod.catnip.api.client.render.RenderPipelineRegistry;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

public final class CARenderType {
	private CARenderType() {}
	public static void init() {}

	private static final RenderPipeline WIRE_PIPELINE = createWirePipeline();

	/**
	 * The legacy mesh emits four independent vertices for every wire segment.
	 * The leash pipeline has the right shader and format, but TRIANGLE_STRIP
	 * joins those segments into a twisted ribbon. The original renderer used
	 * QUADS, so preserve that topology here.
	 */
	private static RenderPipeline createWirePipeline() {
		RenderPipeline base = RenderPipelines.LEASH;
		RenderPipeline.Builder builder = base.toBuilder()
                .withLocation(CreateAddition.asResource("pipeline/wire"))
                .withCull(false)
                .withVertexFormat(base.getVertexFormat(), VertexFormat.Mode.QUADS);

		RenderPipeline pipeline = builder.build();
		RenderPipelineRegistry.INSTANCE.register(pipeline);
		return pipeline;
	}

	public static final RenderType WIRE = RenderType.create(
			"createaddition_wire",
			RenderSetup.builder(WIRE_PIPELINE).useLightmap().createRenderSetup());
}
