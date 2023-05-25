package com.tzaranthony.genshingatcha.core.util.effects;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FreezingEffect extends MobEffect {

    public FreezingEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity affected, int amplifier) {
        affected.hurt(DamageSource.FREEZE, 0.2F * (float) (amplifier + 1));
        affected.setIsInPowderSnow(true);
        affected.setTicksFrozen(affected.getTicksFrozen() + 4);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int i = 1 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}