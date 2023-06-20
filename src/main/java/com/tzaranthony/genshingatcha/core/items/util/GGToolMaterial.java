package com.tzaranthony.genshingatcha.core.items.util;

import com.tzaranthony.genshingatcha.registries.GGItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum GGToolMaterial implements Tier {
    ONE(2, 50, 11.0F, 0.5F, 20, () -> {
        return Ingredient.of(GGItems.PRIMOGEM.get());
            }),
    TWO(3, 80, 12.0F, 1.5F, 25, () -> {
        return Ingredient.of(GGItems.PRIMOGEM.get());
    }),
    THREE(4, 140, 13.0F, 3.5F, 30, () -> {
        return Ingredient.of(GGItems.PRIMOGEM.get());
    }),
    FOUR(5, 373, 14.0F, 6.0F, 35, () -> {
        return Ingredient.of(GGItems.PRIMOGEM.get());
    }),
    FIVE(6, 1561, 15.0F, 8.0F, 40, () -> {
        return Ingredient.of(GGItems.PRIMOGEM.get());
    }),
    FISH(8, 1831, 1000.0F, 994.0F, 100, () -> {
        return Ingredient.of(GGItems.PRIMOGEM.get());
    });

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairMaterial;

    GGToolMaterial(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getUses() {
        return maxUses;
    }

    @Override
    public float getSpeed() {
        return efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamage;
    }

    @Override
    public int getLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial.get();
    }
}