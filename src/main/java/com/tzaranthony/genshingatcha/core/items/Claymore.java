package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.core.items.util.IAttackReachExtending;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class Claymore extends BaseGenshinSword implements IAttackReachExtending {
    private final float attackReach;

    public Claymore(Tier tier, float reach, Item.Properties properties) {
        super(tier, 5, -3.2F, properties);
        this.attackReach = reach;
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity user, LivingEntity target) {
        if (target instanceof Player ptgt) {
            maybeDisableShield(user, ptgt);
        } else if (target.isUsingItem() && target.getUseItem().is(Items.SHIELD)) {
            target.stopUsingItem();
        }
        return super.hurtEnemy(stack, user, target);
    }

    @Override
    public float getAttackReach() {
        return this.attackReach;
    }

    public static void maybeDisableShield(LivingEntity user, Player ptgt) {
        ItemStack stack1 = ptgt.getUseItem();
        if (!stack1.isEmpty() && stack1.is(Items.SHIELD)) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(user) * 0.05F;
            if (user.getRandom().nextFloat() < f) {
                ptgt.getCooldowns().addCooldown(Items.SHIELD, 100);
                user.level.broadcastEntityEvent(ptgt, (byte)30);
            }
        }
    }

}