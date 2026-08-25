package com.mrh0.createaddition.recipe.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;

public record HasFluidTagCondition(TagKey<Fluid> tag) implements ICondition {
	public static final MapCodec<HasFluidTagCondition> CODEC = RecordCodecBuilder.mapCodec((builder) -> {
		return builder.group(Identifier.CODEC.xmap((loc) -> {
			return TagKey.create(Registries.FLUID, loc);
		}, TagKey::location).fieldOf("fluidTag").forGetter(HasFluidTagCondition::tag)).apply(builder, HasFluidTagCondition::new);//
	});

	public HasFluidTagCondition(String location) {
		this(Identifier.parse(location));
	}

	public HasFluidTagCondition(String namespace, String path) {
		this(Identifier.fromNamespaceAndPath(namespace, path));
	}

	public HasFluidTagCondition(Identifier tag) {
		this(TagKey.create(Registries.FLUID, tag));
	}

	public HasFluidTagCondition(TagKey<Fluid> tag) {
		this.tag = tag;
	}

	public boolean test(ICondition.IContext context) {
		return !context.getTag(this.tag).isEmpty();
	}

	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	public String toString() {
		return "has_fluid_tag(\"" + String.valueOf(this.tag.location()) + "\")";
	}

	public TagKey<Fluid> tag() {
		return this.tag;
	}
}
