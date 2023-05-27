package com.tzaranthony.genshingatcha.core.items.util;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.registries.GGItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public enum GGArmorMaterial implements net.minecraft.world.item.ArmorMaterial {
    ONE(GenshinGacha.MOD_ID + ":one", 100, new int[]{1, 2, 3, 1}, 20, SoundEvents.ARMOR_EQUIP_GOLD,
            0.0F, 0.0F, () -> {return Ingredient.of(GGItems.PRIMOGEM.get());
    }),
    TWO(GenshinGacha.MOD_ID + ":two", 200, new int[]{2, 5, 6, 2}, 25, SoundEvents.ARMOR_EQUIP_CHAIN,
            0.0F, 0.0F, () -> {return Ingredient.of(GGItems.PRIMOGEM.get());
    }),
    THREE(GenshinGacha.MOD_ID + ":three", 300, new int[]{3, 6, 8, 3}, 30, SoundEvents.ARMOR_EQUIP_GENERIC,
            1.0F, 0.0F, () -> {return Ingredient.of(GGItems.PRIMOGEM.get());
    }),
    FOUR(GenshinGacha.MOD_ID + ":four", 400, new int[]{5, 8, 9, 5}, 35, SoundEvents.ARMOR_EQUIP_DIAMOND,
            3.0F, 0.1F, () -> {return Ingredient.of(GGItems.PRIMOGEM.get());
    }),
    FIVE(GenshinGacha.MOD_ID + ":five", 500, new int[]{8, 10, 12, 8}, 40, SoundEvents.ARMOR_EQUIP_NETHERITE,
            5.0F, 0.2F, () -> {return Ingredient.of(GGItems.PRIMOGEM.get());
    });

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durability;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairMaterial;

    GGArmorMaterial(String name, int durability, int[] damageReductionAmountArray, int enchantability,
                    SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> p_i231593_10_) {
        this.name = name;
        this.durability = durability;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = new LazyLoadedValue<>(p_i231593_10_);
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slotType) {
        return HEALTH_PER_SLOT[slotType.getIndex()] * this.durability;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slotType) {
        return this.damageReductionAmountArray[slotType.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}