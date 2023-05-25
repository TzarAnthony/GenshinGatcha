package com.tzaranthony.genshingatcha.core.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class BaseGenshinSword extends SwordItem {

    public BaseGenshinSword(Tier tier, int dmg, float cooldown, Properties properties) {
        super(tier, dmg, cooldown, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity user, LivingEntity target) {
        return true;
    }
}