package com.tzaranthony.genshingatcha.core.entities.mobs;

import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.biome.Biome;

import java.util.Random;

public interface ElementalMob {
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
        boolean isMountains = cat == Biome.BiomeCategory.EXTREME_HILLS || cat == Biome.BiomeCategory.MESA || cat == Biome.BiomeCategory.MOUNTAIN;
        boolean isForest = cat == Biome.BiomeCategory.JUNGLE || cat == Biome.BiomeCategory.FOREST || cat == Biome.BiomeCategory.TAIGA;
        boolean isPlains = cat == Biome.BiomeCategory.PLAINS || cat == Biome.BiomeCategory.SAVANNA;
        boolean isWetlands = (biome.isHumid() && biome.getPrecipitation() != Biome.Precipitation.SNOW) || cat == Biome.BiomeCategory.BEACH || cat == Biome.BiomeCategory.RIVER || cat == Biome.BiomeCategory.OCEAN;
        return this.getElementFromBiome(random, tmpId, isMountains, isForest, isPlains, isWetlands);
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