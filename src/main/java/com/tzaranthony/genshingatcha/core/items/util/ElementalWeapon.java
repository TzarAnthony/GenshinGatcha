package com.tzaranthony.genshingatcha.core.items.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface ElementalWeapon {
    void resetCooldown(ItemStack stack);

    int getCooldown(ItemStack stack);

    void interateCooldown(CompoundTag tag);

    boolean isOffCooldown(CompoundTag tag);
}