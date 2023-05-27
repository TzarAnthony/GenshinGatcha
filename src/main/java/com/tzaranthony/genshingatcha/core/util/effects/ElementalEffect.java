package com.tzaranthony.genshingatcha.core.util.effects;

import com.tzaranthony.genshingatcha.registries.GGEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ElementalEffect extends MobEffect {
    public ElementalEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity affected, int amplifier) {
        if (affected.hasEffect(GGEffects.PYRO.get()) && affected.hasEffect(GGEffects.CRYO.get())) {
            affected.hurt(DamageSource.MAGIC, 8.0F);
            affected.removeEffect(GGEffects.PYRO.get());
            affected.removeEffect(GGEffects.CRYO.get());
            return;
        }
        if (affected.hasEffect(GGEffects.PYRO.get()) && affected.hasEffect(GGEffects.ELECTRO.get())) {
            this.doElementalExplosion(affected, DamageSource.IN_FIRE);
            affected.removeEffect(GGEffects.PYRO.get());
            affected.removeEffect(GGEffects.ELECTRO.get());
            return;
        }
        if (affected.hasEffect(GGEffects.CRYO.get()) && affected.hasEffect(GGEffects.ELECTRO.get())) {
            this.doElementalExplosion(affected, DamageSource.FREEZE);
            affected.removeEffect(GGEffects.CRYO.get());
            affected.removeEffect(GGEffects.ELECTRO.get());
            return;
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

    public void doElementalExplosion(LivingEntity affected, DamageSource source) {
        float f = 8.0F;
        double d0 = 5.0D;
        Vec3 vec3 = affected.position();

        for(LivingEntity le : affected.level.getEntitiesOfClass(Mob.class, affected.getBoundingBox().inflate(3.0D))) {
            if (!(affected.distanceToSqr(le) > 25.0D)) {
                boolean flag = false;

                for(int i = 0; i < 2; ++i) {
                    Vec3 vec31 = new Vec3(le.getX(), le.getY(0.5D * (double)i), le.getZ());
                    HitResult hitresult = affected.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, affected));
                    if (hitresult.getType() == HitResult.Type.MISS) {
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    float f1 = f * (float)Math.sqrt((d0 - (double) affected.distanceTo(le)) / d0);
                    le.hurt(source, f1);
                }
            }
        }
    }
}