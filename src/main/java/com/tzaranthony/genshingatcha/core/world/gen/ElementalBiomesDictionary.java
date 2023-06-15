package com.tzaranthony.genshingatcha.core.world.gen;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ElementalBiomesDictionary {
    public static final class Element {
        private final String name;
        private final List<BiomeDictionary.Type> types;
        private final Set<ResourceKey<Biome>> biomes = ConcurrentHashMap.newKeySet();
        private final Set<ResourceKey<Biome>> biomesUn = Collections.unmodifiableSet(biomes);

        public static final ElementalBiomesDictionary.Element CRYO = new Element("CRYO"
                , BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.SNOWY);
        public static final ElementalBiomesDictionary.Element PYRO = new Element("PYRO"
                , BiomeDictionary.Type.DRY, BiomeDictionary.Type.HOT, BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.SAVANNA);
        public static final ElementalBiomesDictionary.Element ELECTRO = new Element("ELECTRO"
                , BiomeDictionary.Type.DRY, BiomeDictionary.Type.UNDERGROUND, BiomeDictionary.Type.SNOWY);
        public static final ElementalBiomesDictionary.Element GEO = new Element("GEO"
                , BiomeDictionary.Type.DRY, BiomeDictionary.Type.MESA, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.PEAK, BiomeDictionary.Type.PLATEAU, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.SLOPE, BiomeDictionary.Type.UNDERGROUND);
        public static final ElementalBiomesDictionary.Element HYDRO = (new Element("HYDRO"
                , BiomeDictionary.Type.BEACH, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.RIVER, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WET))
                .addBiomes(Biomes.DRIPSTONE_CAVES);
        public static final ElementalBiomesDictionary.Element DENDRO = new Element("DENDRO"
                , BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.SWAMP);
        public static final ElementalBiomesDictionary.Element ANEMO = new Element("ANEMO"
                , BiomeDictionary.Type.MESA, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.PEAK, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.PLATEAU, BiomeDictionary.Type.SLOPE, BiomeDictionary.Type.SPARSE);

        private Element (String name, BiomeDictionary.Type... subTypes) {
            this.name = name;
            this.types = ImmutableList.copyOf(subTypes);
            for (BiomeDictionary.Type type : this.types) {
                this.biomes.addAll(BiomeDictionary.getBiomes(type));
            }
        }

        private Element addBiomes(ResourceKey<Biome>... nBiomes) {
            this.biomes.addAll(ImmutableList.copyOf(nBiomes));
            return this;
        }
    }

    @Nonnull
    public static boolean checkBiome(Element element, ResourceLocation name) {
        String checkName = name.toString();
        for (ResourceKey<Biome> biome : element.biomesUn) {
            String biomeName = biome.location().toString();
            if (checkName.equals(biomeName)) {
                return true;
            }
        }
        return false;
    }
}