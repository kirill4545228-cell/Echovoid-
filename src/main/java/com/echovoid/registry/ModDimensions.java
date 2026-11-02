package com.echovoid.registry;

import com.echovoid.EchoVoidMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ModDimensions {
    // Dimension JSON lives in data/echovoid/dimension/echoing_abyss.json
    public static final ResourceKey<Level> ABYSS = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(EchoVoidMod.MODID, "echoing_abyss"));
}
