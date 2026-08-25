package com.mrh0.createaddition.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/** Keeps optional compatibility mixins out of Mixin's target list when their mod is absent. */
public class CreateAdditionMixinPlugin implements IMixinConfigPlugin {
	@Override public void onLoad(String mixinPackage) {}
	@Override public String getRefMapperConfig() { return null; }

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.endsWith("DockingConnectorBEMixin") || mixinClassName.endsWith("VanillaSubLevelBERenderMixin"))
			return classExists(targetClassName);
		return true;
	}

	private static boolean classExists(String className) {
		try {
			Class.forName(className, false, CreateAdditionMixinPlugin.class.getClassLoader());
			return true;
		} catch (ClassNotFoundException | LinkageError ignored) {
			return false;
		}
	}

	@Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
	@Override public List<String> getMixins() { return null; }
	@Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
	@Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
