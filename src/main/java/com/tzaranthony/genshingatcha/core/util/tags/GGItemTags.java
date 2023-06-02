package com.tzaranthony.genshingatcha.core.util.tags;

import com.tzaranthony.genshingatcha.GenshinGacha;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class GGItemTags {
    public static final TagKey<Item> CHARACTERS = registerItemTag("characters");
    public static final TagKey<Item> CLAYMORES = registerItemTag("claymores");
    public static final TagKey<Item> SPEARS = registerItemTag("spears");

    private static TagKey<Item> registerItemTag(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(GenshinGacha.MOD_ID, name));
    }
}