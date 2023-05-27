package com.tzaranthony.genshingatcha.core.items.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface ElementalWeapon {
    void setCooldown(int coolDown, ItemStack stack);

    int getCooldown(ItemStack stack);

    void interateCooldown(CompoundTag tag);

    void activate(ItemStack stack);

    boolean isActive(CompoundTag tag);

    default int getMaxCooldown() {
        return 200;
    }
}