package com.tzaranthony.genshingatcha.core.world.features;

import com.tzaranthony.genshingatcha.GenshinGacha;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GGFeatures {
    public static final DeferredRegister<Feature<?>> reg = DeferredRegister.create(ForgeRegistries.FEATURES, GenshinGacha.MOD_ID);

    public static final RegistryObject<Feature<ProbabilityFeatureConfiguration>> CHALLENGE_CHEST = reg.register("challenge_chest", () -> new ChallengeChestFeature(ProbabilityFeatureConfiguration.CODEC));
}