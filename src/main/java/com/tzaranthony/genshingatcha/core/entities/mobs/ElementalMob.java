package com.tzaranthony.genshingatcha.core.entities.mobs;

import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;

import java.util.Random;

public interface ElementalMob {
    void setElement(int elementId);

    int getElement();

    default int getElementFromBiome(Holder<Biome> holder, Random random) {
        Biome biome = holder.value();
        Biome.BiomeCategory cat = Biome.getBiomeCategory(holder);
        int tmpId = 0;
        if (biome.getBaseTemperature() < 0.15F) {
            tmpId = -1;
        } else if (biome.getBaseTemperature() > 1.0F) {
            tmpId = 1;
        }
        boolean isMountains = getIsMountains(holder, cat);
        boolean isForest = getIsForest(holder, cat) || biome == OverworldBiomes.lushCaves();
        boolean isPlains = getIsPlains(holder, cat);
        boolean isWetlands = getIsWetlands(holder, cat) || (biome.getDownfall() >= 0.8F && biome.getPrecipitation() != Biome.Precipitation.SNOW);
        return this.getElementFromBiome(random, tmpId, isMountains, isForest, isPlains, isWetlands);
    }

    default boolean getIsMountains(Holder<Biome> holder, Biome.BiomeCategory cat) {
        return holder.is(BiomeTags.IS_HILL) || holder.is(BiomeTags.IS_BADLANDS) || holder.is(BiomeTags.IS_MOUNTAIN) ||
                cat == Biome.BiomeCategory.EXTREME_HILLS || cat == Biome.BiomeCategory.MESA || cat == Biome.BiomeCategory.MOUNTAIN || cat == Biome.BiomeCategory.UNDERGROUND;
    }

    default boolean getIsForest(Holder<Biome> holder, Biome.BiomeCategory cat) {
        return holder.is(BiomeTags.IS_JUNGLE) || holder.is(BiomeTags.IS_FOREST) || cat == Biome.BiomeCategory.SWAMP || holder.is(BiomeTags.IS_TAIGA) ||
                cat == Biome.BiomeCategory.JUNGLE || cat == Biome.BiomeCategory.FOREST || cat == Biome.BiomeCategory.SWAMP || cat == Biome.BiomeCategory.TAIGA;
    }

    default boolean getIsPlains(Holder<Biome> holder, Biome.BiomeCategory cat) {
        return cat == Biome.BiomeCategory.PLAINS || cat == Biome.BiomeCategory.SAVANNA || cat == Biome.BiomeCategory.DESERT;
    }

    default boolean getIsWetlands(Holder<Biome> holder, Biome.BiomeCategory cat) {
        return holder.is(BiomeTags.IS_BEACH) || holder.is(BiomeTags.IS_RIVER) || holder.is(BiomeTags.IS_OCEAN) ||
                cat == Biome.BiomeCategory.BEACH || cat == Biome.BiomeCategory.RIVER || cat == Biome.BiomeCategory.OCEAN;
    }

    default int getElementFromBiome(Random random, int tmpId, boolean isMountains, boolean isForest, boolean isPlains, boolean isWetlands) {
        NonNullList<Integer> ElementList = NonNullList.create();
        switch (tmpId) {
            case -1 -> ElementList.add(Element.E.CRYO.getId());
            case 0 -> ElementList.add(Element.E.ANEMO.getId());
            case 1 -> ElementList.add(Element.E.PYRO.getId());
        }
        if (isMountains) {
            ElementList.add(Element.E.GEO.getId());
        }
        if (isForest) {
            ElementList.add(Element.E.DENDRO.getId());
        }
        if (isPlains) {
            ElementList.add(Element.E.ELECTRO.getId());
        }
        if (isWetlands) {
            ElementList.add(Element.E.HYDRO.getId());
        }
        return ElementList.size() > 0 ? ElementList.get(random.nextInt(ElementList.size())) : random.nextInt(7);
    };
}