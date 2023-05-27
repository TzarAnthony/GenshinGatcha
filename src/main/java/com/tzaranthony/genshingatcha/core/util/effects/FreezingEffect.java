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
        affected.hurt(DamageSource.FREEZE, (float) (amplifier + 1));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int i = 20 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}