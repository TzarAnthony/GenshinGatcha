package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.items.*;
import com.tzaranthony.genshingatcha.core.items.util.GGArmorMaterial;
import com.tzaranthony.genshingatcha.core.items.util.GGBowMaterial;
import com.tzaranthony.genshingatcha.core.items.util.GGToolMaterial;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GGItems {
    public static final DeferredRegister<Item> reg = DeferredRegister.create(ForgeRegistries.ITEMS, GenshinGacha.MOD_ID);

    public static final RegistryObject<Item> PRIMOGEM = reg.register("primogem", () -> new Item(Standard().fireResistant()));
    public static final RegistryObject<Item> PRIMO_CARD = reg.register("primogem_card", () -> new PrimoCard(StandardRarity(Rarity.EPIC).stacksTo(1).fireResistant()));

    // weapons
    public static final RegistryObject<Item> SWORD_ONE = reg.register("sword_one", () -> new ElementalSword(GGToolMaterial.ONE, 0.0F, Standard()));
    public static final RegistryObject<Item> SWORD_TWO = reg.register("sword_two", () -> new ElementalSword(GGToolMaterial.TWO, 0.2F, Standard()));
    public static final RegistryObject<Item> SWORD_THREE = reg.register("sword_three", () -> new ElementalSword(GGToolMaterial.THREE, 0.6F, StandardRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> SWORD_FOUR = reg.register("sword_four", () -> new ElementalSword(GGToolMaterial.FOUR, 1.0F, StandardRarity(Rarity.RARE)));
    public static final RegistryObject<Item> SWORD_FIVE = reg.register("sword_five", () -> new ElementalSword(GGToolMaterial.FIVE, 1.4F, StandardRarity(Rarity.EPIC)));

    public static final RegistryObject<Item> CLAYMORE_ONE = reg.register("claymore_one", () -> new Claymore(GGToolMaterial.ONE, 5.1F, Standard()));
    public static final RegistryObject<Item> CLAYMORE_TWO = reg.register("claymore_two", () -> new Claymore(GGToolMaterial.TWO, 5.2F, Standard()));
    public static final RegistryObject<Item> CLAYMORE_THREE = reg.register("claymore_three", () -> new Claymore(GGToolMaterial.THREE, 5.3F, StandardRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CLAYMORE_FOUR = reg.register("claymore_four", () -> new Claymore(GGToolMaterial.FOUR, 5.4F, StandardRarity(Rarity.RARE)));
    public static final RegistryObject<Item> CLAYMORE_FIVE = reg.register("claymore_five", () -> new Claymore(GGToolMaterial.FIVE, 5.5F, StandardRarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CLAYMORE_FISH = reg.register("claymore_fish", () -> new FishClaymore(8.0F, StandardRarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SPEAR_ONE = reg.register("spear_one", () -> new Spear(GGToolMaterial.ONE, 6.0F, Standard()));
    public static final RegistryObject<Item> SPEAR_TWO = reg.register("spear_two", () -> new Spear(GGToolMaterial.TWO, 6.25F, Standard()));
    public static final RegistryObject<Item> SPEAR_THREE = reg.register("spear_three", () -> new Spear(GGToolMaterial.THREE, 6.5F, StandardRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> SPEAR_FOUR = reg.register("spear_four", () -> new Spear(GGToolMaterial.FOUR, 6.75F, StandardRarity(Rarity.RARE)));
    public static final RegistryObject<Item> SPEAR_FIVE = reg.register("spear_five", () -> new Spear(GGToolMaterial.FIVE, 7.0F, StandardRarity(Rarity.EPIC)));

    public static final RegistryObject<Item> BOW_ONE = reg.register("bow_one", () -> new ElementalBow(GGBowMaterial.ONE, Standard()));
    public static final RegistryObject<Item> BOW_TWO = reg.register("bow_two", () -> new ElementalBow(GGBowMaterial.TWO, Standard()));
    public static final RegistryObject<Item> BOW_THREE = reg.register("bow_three", () -> new ElementalBow(GGBowMaterial.THREE, StandardRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BOW_FOUR = reg.register("bow_four", () -> new ElementalBow(GGBowMaterial.FOUR, StandardRarity(Rarity.RARE)));
    public static final RegistryObject<Item> BOW_FIVE = reg.register("bow_five", () -> new ElementalBow(GGBowMaterial.FIVE, StandardRarity(Rarity.EPIC)));

    // artifacts
    public static final RegistryObject<Item> ARTIFACT_FRAGMENT_1 = reg.register("artifact_fragment_1", () -> new Item(Standard()));
    public static final RegistryObject<Item> ARTIFACT_FRAGMENT_2 = reg.register("artifact_fragment_2", () -> new Item(Standard()));
    public static final RegistryObject<Item> ARTIFACT_FRAGMENT_3 = reg.register("artifact_fragment_3", () -> new Item(StandardRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ARTIFACT_FRAGMENT_4 = reg.register("artifact_fragment_4", () -> new Item(StandardRarity(Rarity.RARE)));
    public static final RegistryObject<Item> ARTIFACT_FRAGMENT_5 = reg.register("artifact_fragment_5", () -> new Item(StandardRarity(Rarity.EPIC)));

    public static final RegistryObject<Item> HELMET_ONE = reg.register("helmet_one", () -> new ArmorItem(GGArmorMaterial.ONE, EquipmentSlot.HEAD, Standard()));
    public static final RegistryObject<Item> CHESTPLATE_ONE = reg.register("chestplate_one", () -> new ArmorItem(GGArmorMaterial.ONE, EquipmentSlot.CHEST, Standard()));
    public static final RegistryObject<Item> LEGGINGS_ONE = reg.register("leggings_one", () -> new ArmorItem(GGArmorMaterial.ONE, EquipmentSlot.LEGS, Standard()));
    public static final RegistryObject<Item> BOOTS_ONE = reg.register("boots_one", () -> new ArmorItem(GGArmorMaterial.ONE, EquipmentSlot.FEET, Standard()));

    public static final RegistryObject<Item> HELMET_TWO = reg.register("helmet_two", () -> new ArmorItem(GGArmorMaterial.TWO, EquipmentSlot.HEAD, Standard()));
    public static final RegistryObject<Item> CHESTPLATE_TWO = reg.register("chestplate_two", () -> new ArmorItem(GGArmorMaterial.TWO, EquipmentSlot.CHEST, Standard()));
    public static final RegistryObject<Item> LEGGINGS_TWO = reg.register("leggings_two", () -> new ArmorItem(GGArmorMaterial.TWO, EquipmentSlot.LEGS, Standard()));
    public static final RegistryObject<Item> BOOTS_TWO = reg.register("boots_two", () -> new ArmorItem(GGArmorMaterial.TWO, EquipmentSlot.FEET, Standard()));

    public static final RegistryObject<Item> HELMET_THREE = reg.register("helmet_three", () -> new ArmorItem(GGArmorMaterial.THREE, EquipmentSlot.HEAD, StandardRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CHESTPLATE_THREE = reg.register("chestplate_three", () -> new ArmorItem(GGArmorMaterial.THREE, EquipmentSlot.CHEST, StandardRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> LEGGINGS_THREE = reg.register("leggings_three", () -> new ArmorItem(GGArmorMaterial.THREE, EquipmentSlot.LEGS, StandardRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BOOTS_THREE = reg.register("boots_three", () -> new ArmorItem(GGArmorMaterial.THREE, EquipmentSlot.FEET, StandardRarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> HELMET_FOUR = reg.register("helmet_four", () -> new ArmorItem(GGArmorMaterial.FOUR, EquipmentSlot.HEAD, StandardRarity(Rarity.RARE)));
    public static final RegistryObject<Item> CHESTPLATE_FOUR = reg.register("chestplate_four", () -> new ArmorItem(GGArmorMaterial.FOUR, EquipmentSlot.CHEST, StandardRarity(Rarity.RARE)));
    public static final RegistryObject<Item> LEGGINGS_FOUR = reg.register("leggings_four", () -> new ArmorItem(GGArmorMaterial.FOUR, EquipmentSlot.LEGS, StandardRarity(Rarity.RARE)));
    public static final RegistryObject<Item> BOOTS_FOUR = reg.register("boots_four", () -> new ArmorItem(GGArmorMaterial.FOUR, EquipmentSlot.FEET, StandardRarity(Rarity.RARE)));

    public static final RegistryObject<Item> HELMET_FIVE = reg.register("helmet_five", () -> new ArmorItem(GGArmorMaterial.FIVE, EquipmentSlot.HEAD, StandardRarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CHESTPLATE_FIVE = reg.register("chestplate_five", () -> new ArmorItem(GGArmorMaterial.FIVE, EquipmentSlot.CHEST, StandardRarity(Rarity.EPIC)));
    public static final RegistryObject<Item> LEGGINGS_FIVE = reg.register("leggings_five", () -> new ArmorItem(GGArmorMaterial.FIVE, EquipmentSlot.LEGS, StandardRarity(Rarity.EPIC)));
    public static final RegistryObject<Item> BOOTS_FIVE = reg.register("boots_five", () -> new ArmorItem(GGArmorMaterial.FIVE, EquipmentSlot.FEET, StandardRarity(Rarity.EPIC)));

    // character cards
    public static final RegistryObject<Item> DILUC_C0 = reg.register("diluc_c0", () -> new CharacterCard(GGCharacters.DILUC.getCharacterID(), 0, Character()));
    public static final RegistryObject<Item> DILUC_C1 = reg.register("diluc_c1", () -> new CharacterCard(GGCharacters.DILUC.getCharacterID(), 1, Character()));
    public static final RegistryObject<Item> DILUC_C2 = reg.register("diluc_c2", () -> new CharacterCard(GGCharacters.DILUC.getCharacterID(), 2, Character()));
    public static final RegistryObject<Item> DILUC_C3 = reg.register("diluc_c3", () -> new CharacterCard(GGCharacters.DILUC.getCharacterID(), 3, CharacterRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DILUC_C4 = reg.register("diluc_c4", () -> new CharacterCard(GGCharacters.DILUC.getCharacterID(), 4, CharacterRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DILUC_C5 = reg.register("diluc_c5", () -> new CharacterCard(GGCharacters.DILUC.getCharacterID(), 5, CharacterRarity(Rarity.RARE)));
    public static final RegistryObject<Item> DILUC_C6 = reg.register("diluc_c6", () -> new CharacterCard(GGCharacters.DILUC.getCharacterID(), 6, CharacterRarity(Rarity.EPIC)));

    public static final RegistryObject<Item> FISCHL_C0 = reg.register("fischl_c0", () -> new CharacterCard(GGCharacters.FISCHL.getCharacterID(), 0, Character()));
    public static final RegistryObject<Item> FISCHL_C1 = reg.register("fischl_c1", () -> new CharacterCard(GGCharacters.FISCHL.getCharacterID(), 1, Character()));
    public static final RegistryObject<Item> FISCHL_C2 = reg.register("fischl_c2", () -> new CharacterCard(GGCharacters.FISCHL.getCharacterID(), 2, Character()));
    public static final RegistryObject<Item> FISCHL_C3 = reg.register("fischl_c3", () -> new CharacterCard(GGCharacters.FISCHL.getCharacterID(), 3, CharacterRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FISCHL_C4 = reg.register("fischl_c4", () -> new CharacterCard(GGCharacters.FISCHL.getCharacterID(), 4, CharacterRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FISCHL_C5 = reg.register("fischl_c5", () -> new CharacterCard(GGCharacters.FISCHL.getCharacterID(), 5, CharacterRarity(Rarity.RARE)));
    public static final RegistryObject<Item> FISCHL_C6 = reg.register("fischl_c6", () -> new CharacterCard(GGCharacters.FISCHL.getCharacterID(), 6, CharacterRarity(Rarity.EPIC)));

    public static final RegistryObject<Item> ZHONGLI_C0 = reg.register("zhongli_c0", () -> new CharacterCard(GGCharacters.ZHONGLI.getCharacterID(), 0, Character()));
    public static final RegistryObject<Item> ZHONGLI_C1 = reg.register("zhongli_c1", () -> new CharacterCard(GGCharacters.ZHONGLI.getCharacterID(), 1, Character()));
    public static final RegistryObject<Item> ZHONGLI_C2 = reg.register("zhongli_c2", () -> new CharacterCard(GGCharacters.ZHONGLI.getCharacterID(), 2, Character()));
    public static final RegistryObject<Item> ZHONGLI_C3 = reg.register("zhongli_c3", () -> new CharacterCard(GGCharacters.ZHONGLI.getCharacterID(), 3, CharacterRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ZHONGLI_C4 = reg.register("zhongli_c4", () -> new CharacterCard(GGCharacters.ZHONGLI.getCharacterID(), 4, CharacterRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ZHONGLI_C5 = reg.register("zhongli_c5", () -> new CharacterCard(GGCharacters.ZHONGLI.getCharacterID(), 5, CharacterRarity(Rarity.RARE)));
    public static final RegistryObject<Item> ZHONGLI_C6 = reg.register("zhongli_c6", () -> new CharacterCard(GGCharacters.ZHONGLI.getCharacterID(), 6, CharacterRarity(Rarity.EPIC)));

    public static final RegistryObject<Item> QIQI_C0 = reg.register("qiqi_c0", () -> new CharacterCard(GGCharacters.QIQI.getCharacterID(), 0, Character()));
    public static final RegistryObject<Item> QIQI_C1 = reg.register("qiqi_c1", () -> new CharacterCard(GGCharacters.QIQI.getCharacterID(), 1, Character()));
    public static final RegistryObject<Item> QIQI_C2 = reg.register("qiqi_c2", () -> new CharacterCard(GGCharacters.QIQI.getCharacterID(), 2, Character()));
    public static final RegistryObject<Item> QIQI_C3 = reg.register("qiqi_c3", () -> new CharacterCard(GGCharacters.QIQI.getCharacterID(), 3, CharacterRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> QIQI_C4 = reg.register("qiqi_c4", () -> new CharacterCard(GGCharacters.QIQI.getCharacterID(), 4, CharacterRarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> QIQI_C5 = reg.register("qiqi_c5", () -> new CharacterCard(GGCharacters.QIQI.getCharacterID(), 5, CharacterRarity(Rarity.RARE)));
    public static final RegistryObject<Item> QIQI_C6 = reg.register("qiqi_c6", () -> new CharacterCard(GGCharacters.QIQI.getCharacterID(), 6, CharacterRarity(Rarity.EPIC)));

    public static Item.Properties Standard() {
        return new Item.Properties().tab(GenshinGacha.TAB);
    }
    public static Item.Properties StandardRarity(Rarity rarity) {
        return Standard().rarity(rarity);
    }
    public static Item.Properties Character() {
        return new Item.Properties().tab(GenshinGacha.TAB).stacksTo(1).fireResistant();
    }
    public static Item.Properties CharacterRarity(Rarity rarity) {
        return Character().rarity(rarity);
    }
}