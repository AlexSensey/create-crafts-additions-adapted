package com.mrh0.createaddition.ponder;

import com.mrh0.createaddition.CreateAddition;
import com.mrh0.createaddition.index.CAPonders;
import com.simibubi.create.foundation.ponder.PonderWorldBlockEntityFix;

import net.createmod.ponder.api.client.level.PonderLevel;
import net.createmod.ponder.api.client.registration.IndexExclusionHelper;
import net.createmod.ponder.api.client.registration.PonderPlugin;
import net.createmod.ponder.api.client.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.client.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.api.client.registration.SharedTextRegistrationHelper;
import net.minecraft.resources.Identifier;

public class CAPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return CreateAddition.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<Identifier> helper) {
        CAPonders.registerScenes(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<Identifier> helper) {
        CAPonders.registerTags(helper);
    }

    @Override
    public void registerSharedText(SharedTextRegistrationHelper helper) {
        // helper.registerSharedText("rpm8", "8 RPM");
    }

    @Override
    public void indexExclusions(IndexExclusionHelper helper) {
        // helper.excludeBlockVariants(ValveHandleBlock.class, AllBlocks.COPPER_VALVE_HANDLE.get());
    }
}
