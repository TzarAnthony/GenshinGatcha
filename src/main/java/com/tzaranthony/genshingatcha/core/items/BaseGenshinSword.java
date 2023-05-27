package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.core.items.util.ElementalWeapon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class BaseGenshinSword extends SwordItem implements ElementalWeapon {
    protected static final String ELEMENT_COOLDOWN = "elemental_attack_charge";

    public BaseGenshinSword(Tier tier, int dmg, float cooldown, Properties properties) {
        super(tier, dmg, cooldown, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity user, LivingEntity target) {
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.isActive(stack.getOrCreateTag());
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (float) this.getCooldown(stack) * 13.0F / (float) this.getMaxCooldown());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity owner, int tick, boolean selected) {
        CompoundTag tag = stack.getOrCreateTag();
        if (isActive(tag)) {
            this.interateCooldown(tag);
        }
    }

    @Override
    public void setCooldown(int coolDown, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(ELEMENT_COOLDOWN, coolDown);
    }

    @Override
    public int getCooldown(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(ELEMENT_COOLDOWN)) {
            return 0;
        }
        return tag.getInt(ELEMENT_COOLDOWN);
    }

    @Override
    public void interateCooldown(CompoundTag tag) {
        tag.putInt(ELEMENT_COOLDOWN, Math.max(0, tag.getInt(ELEMENT_COOLDOWN) - 1));
    }

    @Override
    public void activate(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(ELEMENT_COOLDOWN, this.getMaxCooldown());
    }

    @Override
    public boolean isActive(CompoundTag tag) {
        if (!tag.contains(ELEMENT_COOLDOWN)) {
            return false;
        }
        return tag.getInt(ELEMENT_COOLDOWN) > 0;
    }
}