package com.tzaranthony.genshingatcha.core.world.features;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class GGBlockFeatureConfig {
    public static final Holder<ConfiguredFeature<ProbabilityFeatureConfiguration, ?>> CHALLENGE_CHEST = FeatureUtils.register("challenge_chest", GGFeatures.CHALLENGE_CHEST.get()
            , new ProbabilityFeatureConfiguration(0.5F));
}