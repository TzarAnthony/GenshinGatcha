package com.tzaranthony.genshingatcha.core.util.effects;

import com.tzaranthony.genshingatcha.registries.GGEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;

public class ComboEffect extends MobEffect {
    public ComboEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity affected, int amplifier) {
        if (affected.hasEffect(GGEffects.CHARGED.get()) && affected.isInWaterOrRain()) {
            affected.hurt(DamageSource.LIGHTNING_BOLT, (float) (amplifier + 1) * 2.0F);
        }
        if (affected.hasEffect(GGEffects.CATALYZE.get())) {
            if (affected.hasEffect(GGEffects.PYRO.get())) {
                ElementalEffect.doElementalExplosion(affected, DamageSource.IN_FIRE, 1.5D, true);
                affected.removeEffect(GGEffects.PYRO.get());
                affected.removeEffect(GGEffects.CATALYZE.get());
            } else if (affected.hasEffect(GGEffects.HYDRO.get())) {
                affected.level.setBlock(affected.getOnPos().above(), Blocks.CACTUS.defaultBlockState(), 0);
                ElementalEffect.doElementalExplosion(affected, DamageSource.CACTUS);
                affected.removeEffect(GGEffects.HYDRO.get());
                affected.removeEffect(GGEffects.CATALYZE.get());
            } else if (affected.hasEffect(GGEffects.ELECTRO.get())) {
                affected.hurt(DamageSource.LIGHTNING_BOLT, (float) (amplifier + 1));
            } else if (affected.hasEffect(GGEffects.DENDRO.get())) {
                affected.hurt(DamageSource.CACTUS, (float) (amplifier + 1) * 2.0F);
            }
        }
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