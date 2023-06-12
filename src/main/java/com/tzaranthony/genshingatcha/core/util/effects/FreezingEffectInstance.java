package com.tzaranthony.genshingatcha.core.util.effects;

import com.tzaranthony.genshingatcha.registries.GGEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class FreezingEffectInstance extends MobEffectInstance {

    public FreezingEffectInstance(int duration) {
        this(duration, 0);
    }

    public FreezingEffectInstance(int duration, int amplifier) {
        super(GGEffects.FREEZING.get(), duration, amplifier);
    }

    public boolean tick(LivingEntity affected, Runnable func) {
        if (this.getDuration() > 0) {
            affected.setIsInPowderSnow(true);
            affected.setTicksFrozen(affected.getTicksFrozen() + 4);
        }

        return super.tick(affected, func);
    }
}