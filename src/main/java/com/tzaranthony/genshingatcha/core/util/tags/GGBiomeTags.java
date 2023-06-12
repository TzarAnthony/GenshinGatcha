package com.tzaranthony.genshingatcha.core.util.tags;

import com.tzaranthony.genshingatcha.GenshinGacha;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class GGBiomeTags {
    public static final TagKey<Biome> IS_CRYO = registerBiomeTag("is_cryo_spawnable");
    public static final TagKey<Biome> IS_PYRO = registerBiomeTag("is_pyro_spawnable");
    public static final TagKey<Biome> IS_NOT_ELECTRO = registerBiomeTag("not_electro_spawnable");
    public static final TagKey<Biome> IS_GEO = registerBiomeTag("is_geo_spawnable");
    public static final TagKey<Biome> IS_HYDRO = registerBiomeTag("is_hydro_spawnable");
    public static final TagKey<Biome> IS_DENDRO = registerBiomeTag("is_dendro_spawnable");
    public static final TagKey<Biome> IS_ANEMO = registerBiomeTag("is_anemo_spawnable");

    private static TagKey<Biome> registerBiomeTag(String name) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(GenshinGacha.MOD_ID, name));
    }
}