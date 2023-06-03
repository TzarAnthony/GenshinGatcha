package com.tzaranthony.genshingatcha.core.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.tzaranthony.genshingatcha.core.capabilities.CharacterHelper;
import com.tzaranthony.genshingatcha.core.character.Character;
import com.tzaranthony.genshingatcha.core.entities.projectiles.ElementalArrow;
import com.tzaranthony.genshingatcha.core.items.util.ElementalWeapon;
import com.tzaranthony.genshingatcha.core.items.util.GGBowMaterial;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.registries.GGCharacters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
    private final int cooldown;
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ElementalBow(GGBowMaterial bowMaterial, Properties properties) {
        super(properties);
        this.bowMaterial = bowMaterial;
        this.cooldown = bowMaterial.getCooldown();
        this.attackDamage = bowMaterial.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public void releaseUsing(ItemStack bowStack, Level level, LivingEntity user, int drawTime) {
        if (!level.isClientSide && user instanceof Player sPlayer) {
            int i = this.getUseDuration(bowStack) - drawTime;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(bowStack, level, sPlayer, i, true);
            if (i < 0) return;

            float f = this.getSpeedForTime(i);
            if (!(f < 0.1F)) {
                AbstractArrow abstractarrow = null;
                boolean createdArrow = false;
                if (isOffCooldown(bowStack.getOrCreateTag())) {
                    Character character = GGCharacters.characterMap.get(CharacterHelper.getCharacter(sPlayer));
                    if (character != null) {
                        Element.E element = character.getElement();
                        abstractarrow = new ElementalArrow(sPlayer, element, level);
                        this.resetCooldown(bowStack);
                        createdArrow = true;
                    }
                }
                if (!createdArrow) {
                    ArrowItem arrowitem = (ArrowItem) Items.ARROW;
                    abstractarrow = arrowitem.createArrow(level, ItemStack.EMPTY, sPlayer);
                    if (f == 1.0F) {
                        abstractarrow.setCritArrow(true);
                    }
                    abstractarrow.pickup = AbstractArrow.Pickup.DISALLOWED;
                }

                abstractarrow = customArrow(abstractarrow);
                abstractarrow.shootFromRotation(sPlayer, sPlayer.getXRot(), sPlayer.getYRot(), 0.0F, f * 3.0F, 1.0F);

                int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bowStack);
                if (j > 0) {
                    abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                }

                int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bowStack);
                if (k > 0) {
                    abstractarrow.setKnockback(k);
                }

                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bowStack) > 0) {
                    abstractarrow.setSecondsOnFire(100);
                }

                level.addFreshEntity(abstractarrow);
            }

            level.playSound(null, sPlayer.getX(), sPlayer.getY(), sPlayer.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            sPlayer.awardStat(Stats.ITEM_USED.get(this));
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

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, level, player, hand, true);
        if (ret != null) return ret;
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return !this.isOffCooldown(stack.getOrCreateTag());
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (float) this.getCooldown(stack) * 13.0F / (float) this.cooldown);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity owner, int tick, boolean selected) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!isOffCooldown(tag)) {
            this.interateCooldown(tag);
        }
    }

    @Override
    public int getDefaultProjectileRange() {
        return this.bowMaterial.getCooldown();
    }


    public void resetCooldown(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(ELEMENT_COOLDOWN, this.cooldown);
    }

    @Override
    public int getCooldown(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getInt(ELEMENT_COOLDOWN);
    }

    @Override
    public void interateCooldown(CompoundTag tag) {
        tag.putInt(ELEMENT_COOLDOWN, Math.max(0, tag.getInt(ELEMENT_COOLDOWN) - 1));
    }

    @Override
    public boolean isOffCooldown(CompoundTag tag) {
        return tag.getInt(ELEMENT_COOLDOWN) <= 0;
    }
}