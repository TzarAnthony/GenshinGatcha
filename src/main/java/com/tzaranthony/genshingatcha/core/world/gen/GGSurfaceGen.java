package com.tzaranthony.genshingatcha.core.world.gen;

import com.tzaranthony.genshingatcha.core.world.features.GGFeaturePlacements;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Set;

public class GGSurfaceGen {
    public static void generateSurface(final BiomeLoadingEvent event) {
        ResourceKey<Biome> name = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(name);

        if (types.contains(BiomeDictionary.Type.OVERWORLD)) {
            List<Holder<PlacedFeature>> base = event.getGeneration().getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES);
            base.add(GGFeaturePlacements.CHALLENGE_CHEST);
        }
    }
}