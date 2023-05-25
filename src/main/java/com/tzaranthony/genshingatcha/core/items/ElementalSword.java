package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.registries.GGEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class ElementalSword extends BaseGenshinSword {
    public ElementalSword(Tier tier, float cooldown, Properties properties) {
        super(tier, 3, -2.4F + cooldown, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        if (user instanceof Player player) {
            //TODO: Get player element here and move these to a keybinding? Or make a keybind to charge the sword then it'll stay active for a while and apply abilities, render bar based on time remaining?
            int eID = 0;
            if (eID == 0) {
                target.setSecondsOnFire(4);
            } else if (eID == 1) {
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80));
            } else if (eID == 2) {
                target.knockback(0.5F, Mth.sin(player.getYRot() * ((float)Math.PI / 180F)), (-Mth.cos(player.getYRot() * ((float)Math.PI / 180F))));
            } else if (eID == 3) {
                target.addEffect(new MobEffectInstance(GGEffects.FREEZING.get(), 200));
            }
        }
        return super.hurtEnemy(stack, target, user);
    }
}