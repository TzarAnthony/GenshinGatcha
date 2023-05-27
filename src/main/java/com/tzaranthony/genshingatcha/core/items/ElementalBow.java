package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.core.items.util.ElementalWeapon;
import com.tzaranthony.genshingatcha.core.items.util.GGBowMaterial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class ElementalBow extends BowItem implements ElementalWeapon {
    protected static final String ELEMENT_COOLDOWN = "elemental_attack_charge";
    private final GGBowMaterial bowMaterial;

    public ElementalBow(GGBowMaterial bowMaterial, Properties properties) {
        super(properties);
        this.bowMaterial = bowMaterial;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int drawTime) {
        if (!this.isActive(stack.getOrCreateTag())) {
            super.releaseUsing(stack, level, user, drawTime);
            return;
        }

        if (!level.isClientSide && user instanceof Player) {
            Player player = (Player) user;

            //TODO: get elemental arrows here
            ItemStack itemstack = player.getProjectile(stack);

            int i = this.getUseDuration(stack) - drawTime;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
            if (i < 0) return;

            if (!itemstack.isEmpty()) {
                float f = this.getSpeedForTime(i);
                if (!(f < 0.1F)) {
                    //TODO: replace with elemental arrows
                    ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                    AbstractArrow abstractarrow = arrowitem.createArrow(level, itemstack, player);
                    abstractarrow = customArrow(abstractarrow);
                    abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                    if (f == 1.0F) {
                        abstractarrow.setCritArrow(true);
                    }

                    int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                    if (j > 0) {
                        abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                    }

                    int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                    if (k > 0) {
                        abstractarrow.setKnockback(k);
                    }

                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                        abstractarrow.setSecondsOnFire(100);
                    }

                    level.addFreshEntity(abstractarrow);
                }

                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    public float getSpeedForTime(int drawTime) {
        float f = (float) drawTime / this.bowMaterial.getSpeed();
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
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
    public int getDefaultProjectileRange() {
        return this.bowMaterial.getRange();
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