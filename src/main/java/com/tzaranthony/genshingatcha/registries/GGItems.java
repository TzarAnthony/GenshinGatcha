package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.items.Claymore;
import com.tzaranthony.genshingatcha.core.items.ElementalSword;
import com.tzaranthony.genshingatcha.core.items.Spear;
import com.tzaranthony.genshingatcha.core.items.util.GGArmorMaterial;
import com.tzaranthony.genshingatcha.core.items.util.GGToolMaterial;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GGItems {
    public static final DeferredRegister<Item> reg = DeferredRegister.create(ForgeRegistries.ITEMS, GenshinGacha.MOD_ID);

    public static final RegistryObject<Item> PRIMOGEM = reg.register("primogem", () -> new Item(Standard().fireResistant())); //TODO: make these not lost on death, and increase stack size to 2048

    // weapons
    public static final RegistryObject<Item> SWORD_ONE = reg.register("sword_one", () -> new ElementalSword(GGToolMaterial.ONE, 0.0F, Standard()));
    public static final RegistryObject<Item> SWORD_TWO = reg.register("sword_two", () -> new ElementalSword(GGToolMaterial.TWO, 0.2F, Standard()));
    public static final RegistryObject<Item> SWORD_THREE = reg.register("sword_three", () -> new ElementalSword(GGToolMaterial.THREE, 0.6F, Standard()));
    public static final RegistryObject<Item> SWORD_FOUR = reg.register("sword_four", () -> new ElementalSword(GGToolMaterial.FOUR, 1.0F, Standard()));
    public static final RegistryObject<Item> SWORD_FIVE = reg.register("sword_five", () -> new ElementalSword(GGToolMaterial.FIVE, 1.4F, Standard()));

    public static final RegistryObject<Item> CLAYMORE_ONE = reg.register("claymore_one", () -> new Claymore(GGToolMaterial.ONE, 4.0F, Standard()));
    public static final RegistryObject<Item> CLAYMORE_TWO = reg.register("claymore_two", () -> new Claymore(GGToolMaterial.TWO, 5.0F, Standard()));
    public static final RegistryObject<Item> CLAYMORE_THREE = reg.register("claymore_three", () -> new Claymore(GGToolMaterial.THREE, 6.0F, Standard()));
    public static final RegistryObject<Item> CLAYMORE_FOUR = reg.register("claymore_four", () -> new Claymore(GGToolMaterial.FOUR, 7.0F, Standard()));
    public static final RegistryObject<Item> CLAYMORE_FIVE = reg.register("claymore_five", () -> new Claymore(GGToolMaterial.FIVE, 8.0F, Standard()));

    public static final RegistryObject<Item> SPEAR_ONE = reg.register("spear_one", () -> new Spear(GGToolMaterial.ONE, 6.0F, Standard()));
    public static final RegistryObject<Item> SPEAR_TWO = reg.register("spear_two", () -> new Spear(GGToolMaterial.TWO, 7.0F, Standard()));
    public static final RegistryObject<Item> SPEAR_THREE = reg.register("spear_three", () -> new Spear(GGToolMaterial.THREE, 8.0F, Standard()));
    public static final RegistryObject<Item> SPEAR_FOUR = reg.register("spear_four", () -> new Spear(GGToolMaterial.FOUR, 9.0F, Standard()));
    public static final RegistryObject<Item> SPEAR_FIVE = reg.register("spear_five", () -> new Spear(GGToolMaterial.FIVE, 10.0F, Standard()));


    // artifacts
    public static final RegistryObject<Item> HELMET_ONE = reg.register("helmet_one", () -> new ArmorItem(GGArmorMaterial.ONE, EquipmentSlot.HEAD, Standard()));
    public static final RegistryObject<Item> CHESTPLATE_ONE = reg.register("chestplate_one", () -> new ArmorItem(GGArmorMaterial.ONE, EquipmentSlot.CHEST, Standard()));
    public static final RegistryObject<Item> LEGGINGS_ONE = reg.register("leggings_one", () -> new ArmorItem(GGArmorMaterial.ONE, EquipmentSlot.LEGS, Standard()));
    public static final RegistryObject<Item> BOOTS_ONE = reg.register("boots_one", () -> new ArmorItem(GGArmorMaterial.ONE, EquipmentSlot.FEET, Standard()));

    public static final RegistryObject<Item> HELMET_TWO = reg.register("helmet_two", () -> new ArmorItem(GGArmorMaterial.TWO, EquipmentSlot.HEAD, Standard()));
    public static final RegistryObject<Item> CHESTPLATE_TWO = reg.register("chestplate_two", () -> new ArmorItem(GGArmorMaterial.TWO, EquipmentSlot.CHEST, Standard()));
    public static final RegistryObject<Item> LEGGINGS_TWO = reg.register("leggings_two", () -> new ArmorItem(GGArmorMaterial.TWO, EquipmentSlot.LEGS, Standard()));
    public static final RegistryObject<Item> BOOTS_TWO = reg.register("boots_two", () -> new ArmorItem(GGArmorMaterial.TWO, EquipmentSlot.FEET, Standard()));

    public static final RegistryObject<Item> HELMET_THREE = reg.register("helmet_three", () -> new ArmorItem(GGArmorMaterial.THREE, EquipmentSlot.HEAD, Standard()));
    public static final RegistryObject<Item> CHESTPLATE_THREE = reg.register("chestplate_three", () -> new ArmorItem(GGArmorMaterial.THREE, EquipmentSlot.CHEST, Standard()));
    public static final RegistryObject<Item> LEGGINGS_THREE = reg.register("leggings_three", () -> new ArmorItem(GGArmorMaterial.THREE, EquipmentSlot.LEGS, Standard()));
    public static final RegistryObject<Item> BOOTS_THREE = reg.register("boots_three", () -> new ArmorItem(GGArmorMaterial.THREE, EquipmentSlot.FEET, Standard()));

    public static final RegistryObject<Item> HELMET_FOUR = reg.register("helmet_four", () -> new ArmorItem(GGArmorMaterial.FOUR, EquipmentSlot.HEAD, Standard()));
    public static final RegistryObject<Item> CHESTPLATE_FOUR = reg.register("chestplate_four", () -> new ArmorItem(GGArmorMaterial.FOUR, EquipmentSlot.CHEST, Standard()));
    public static final RegistryObject<Item> LEGGINGS_FOUR = reg.register("leggings_four", () -> new ArmorItem(GGArmorMaterial.FOUR, EquipmentSlot.LEGS, Standard()));
    public static final RegistryObject<Item> BOOTS_FOUR = reg.register("boots_four", () -> new ArmorItem(GGArmorMaterial.FOUR, EquipmentSlot.FEET, Standard()));

    public static final RegistryObject<Item> HELMET_FIVE = reg.register("helmet_five", () -> new ArmorItem(GGArmorMaterial.FIVE, EquipmentSlot.HEAD, Standard()));
    public static final RegistryObject<Item> CHESTPLATE_FIVE = reg.register("chestplate_five", () -> new ArmorItem(GGArmorMaterial.FIVE, EquipmentSlot.CHEST, Standard()));
    public static final RegistryObject<Item> LEGGINGS_FIVE = reg.register("leggings_five", () -> new ArmorItem(GGArmorMaterial.FIVE, EquipmentSlot.LEGS, Standard()));
    public static final RegistryObject<Item> BOOTS_FIVE = reg.register("boots_five", () -> new ArmorItem(GGArmorMaterial.FIVE, EquipmentSlot.FEET, Standard()));


    public static Item.Properties Standard() {
        return new Item.Properties().tab(GenshinGacha.TAB);
    }
}