package com.tzaranthony.genshingatcha.core.world.gen;

import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;

public class GGEntityGen {
    public static void addSlimesToBiomes(BiomeLoadingEvent event) {
        addToElementalBiome(event, ElementalBiomesDictionary.Element.CRYO, GGEntities.CRYO_SLIME.get(), 100, 4, 4);
        addToElementalBiome(event, ElementalBiomesDictionary.Element.PYRO, GGEntities.PYRO_SLIME.get(), 100, 4, 4);
        addInverseElementalBiome(event, ElementalBiomesDictionary.Element.ELECTRO, GGEntities.ELECTRO_SLIME.get(), 100, 4, 4);
        addToElementalBiome(event, ElementalBiomesDictionary.Element.GEO, GGEntities.GEO_SLIME.get(), 100, 4, 4);
        addToElementalBiome(event, ElementalBiomesDictionary.Element.HYDRO, GGEntities.HYDRO_SLIME.get(), 100, 4, 4);
        addToElementalBiome(event, ElementalBiomesDictionary.Element.DENDRO, GGEntities.DENDRO_SLIME.get(), 100, 4, 4);
        addToElementalBiome(event, ElementalBiomesDictionary.Element.ANEMO, GGEntities.ANEMO_SLIME.get(), 100, 4, 4);

//        addToOverworld(event, GGEntities.MELEE_HILICHRUL.get(), 100, 4, 4);
    }

    private static void addToElementalBiome(BiomeLoadingEvent event, ElementalBiomesDictionary.Element element, EntityType<?> type, int weight, int minCount, int maxCount) {
        if (ElementalBiomesDictionary.checkBiome(element, event.getName())) {
            add(event, type, weight, minCount, maxCount);
        }
    }

    private static void addInverseElementalBiome(BiomeLoadingEvent event, ElementalBiomesDictionary.Element element, EntityType<?> type, int weight, int minCount, int maxCount) {
        if (!ElementalBiomesDictionary.checkBiome(element, event.getName())) {
            add(event, type, weight, minCount, maxCount);
        }
    }

    private static void addToOverworld(BiomeLoadingEvent event, EntityType<?> type, int weight, int minCount, int maxCount) {
        Biome.BiomeCategory cat = event.getCategory();
        if (cat != Biome.BiomeCategory.THEEND && cat != Biome.BiomeCategory.NETHER) {
            add(event, type, weight, minCount, maxCount);
        }
    }

    private static void add(BiomeLoadingEvent event, EntityType<?> type, int weight, int minCount, int maxCount) {
        List<MobSpawnSettings.SpawnerData> base = event.getSpawns().getSpawner(type.getCategory());
        base.add(new MobSpawnSettings.SpawnerData(type, weight, minCount, maxCount));
    }
}