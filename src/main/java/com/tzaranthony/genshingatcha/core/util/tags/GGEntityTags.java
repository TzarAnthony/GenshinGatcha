package com.tzaranthony.genshingatcha.core.util.tags;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class GGEntityTags {
    public static final TagKey<EntityType<?>> NO_CRYO = registerForgeEntityTag("cryo_immune");
    public static final TagKey<EntityType<?>> NO_PYRO = registerForgeEntityTag("pyro_immune");
    public static final TagKey<EntityType<?>> NO_ELECTRO = registerForgeEntityTag("electro_immune");
    public static final TagKey<EntityType<?>> NO_GEO = registerForgeEntityTag("geo_immune");
    public static final TagKey<EntityType<?>> NO_HYDRO = registerForgeEntityTag("hydro_immune");
    public static final TagKey<EntityType<?>> NO_DENDRO = registerForgeEntityTag("dendro_immune");
    public static final TagKey<EntityType<?>> NO_ANEMO = registerForgeEntityTag("anemo_immune");

    private static @NotNull TagKey<EntityType<?>> registerForgeEntityTag(String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("forge", name));
    }
}