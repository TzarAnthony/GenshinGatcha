package com.tzaranthony.genshingatcha.core.world.features;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;

public class GGFeaturePlacements {
    public static final Holder<PlacedFeature> CHALLENGE_CHEST = PlacementUtils.register("challenge_chest",
            GGBlockFeatureConfig.CHALLENGE_CHEST, List.of(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(16), BiomeFilter.biome()));
}