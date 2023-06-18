package com.tzaranthony.genshingatcha.core.util.effects;

import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ElementalEffect extends MobEffect {
    public ElementalEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity affected, int amplifier) {
        if (affected.hasEffect(GGEffects.CRYO.get()) && affected.isInWater()) {
            affected.addEffect(new FreezingEffectInstance(100));
        }
        if (affected.hasEffect(GGEffects.ANEMO.get()) && hasAnyElementalEffect(affected)) {
            this.doElementalExplosion(affected, GGDamageSource.ANEMO);
            affected.removeEffect(GGEffects.ANEMO.get());
            clearAllElementalEffects(affected);
            return;
        }
        if (affected.hasEffect(GGEffects.GEO.get()) && hasAnyElementalEffect(affected)) {
            ItemEntity ie = new ItemEntity(affected.level, affected.getX(), affected.getY(), affected.getZ(), new ItemStack(Items.GOLDEN_CARROT));
            affected.level.addFreshEntity(ie);
            affected.removeEffect(GGEffects.GEO.get());
            clearAllElementalEffects(affected);
            return;
        }
        if (affected.hasEffect(GGEffects.PYRO.get()) && affected.hasEffect(GGEffects.HYDRO.get())) {
            affected.hurt(GGDamageSource.PYRO, 3.0F);
            affected.hurt(GGDamageSource.HYDRO, 3.0F);
            affected.removeEffect(GGEffects.PYRO.get());
            affected.removeEffect(GGEffects.HYDRO.get());
            return;
        }
        if (affected.hasEffect(GGEffects.PYRO.get()) && affected.hasEffect(GGEffects.ELECTRO.get())) {
            //maybe add knockback for this
            this.doElementalExplosion(affected, DamageSource.GENERIC);
            affected.removeEffect(GGEffects.PYRO.get());
            affected.removeEffect(GGEffects.ELECTRO.get());
            return;
        }
        if (affected.hasEffect(GGEffects.PYRO.get()) && affected.hasEffect(GGEffects.CRYO.get())) {
            affected.hurt(GGDamageSource.PYRO, 3.0F);
            affected.hurt(GGDamageSource.CRYO, 3.0F);
            affected.removeEffect(GGEffects.PYRO.get());
            affected.removeEffect(GGEffects.CRYO.get());
            return;
        }
        if (affected.hasEffect(GGEffects.PYRO.get()) && affected.hasEffect(GGEffects.DENDRO.get())) {
            this.doElementalExplosion(affected, GGDamageSource.PYRO, 1.5D, true);
            affected.removeEffect(GGEffects.PYRO.get());
            affected.removeEffect(GGEffects.DENDRO.get());
            return;
        }
        if (affected.hasEffect(GGEffects.HYDRO.get()) && affected.hasEffect(GGEffects.ELECTRO.get())) {
            affected.addEffect(new MobEffectInstance(GGEffects.CHARGED.get(), 400));
            affected.removeEffect(GGEffects.HYDRO.get());
            affected.removeEffect(GGEffects.ELECTRO.get());
            return;
        }
        if (affected.hasEffect(GGEffects.HYDRO.get()) && affected.hasEffect(GGEffects.CRYO.get())) {
            affected.addEffect(new FreezingEffectInstance(400));
            affected.removeEffect(GGEffects.HYDRO.get());
            affected.removeEffect(GGEffects.CRYO.get());
            return;
        }
        if (affected.hasEffect(GGEffects.HYDRO.get()) && affected.hasEffect(GGEffects.DENDRO.get())) {
            affected.level.setBlock(affected.getOnPos().above(), Blocks.CACTUS.defaultBlockState(), 0);
            this.doElementalExplosion(affected, GGDamageSource.DENDRO);
            affected.removeEffect(GGEffects.HYDRO.get());
            affected.removeEffect(GGEffects.DENDRO.get());
            return;
        }
        if (affected.hasEffect(GGEffects.ELECTRO.get()) && affected.hasEffect(GGEffects.CRYO.get())) {
            this.doElementalExplosion(affected, GGDamageSource.CRYO);
            affected.removeEffect(GGEffects.ELECTRO.get());
            affected.removeEffect(GGEffects.CRYO.get());
            return;
        }
        if (affected.hasEffect(GGEffects.ELECTRO.get()) && affected.hasEffect(GGEffects.DENDRO.get())) {
            affected.addEffect(new MobEffectInstance(GGEffects.CATALYZE.get(), 400));
            affected.removeEffect(GGEffects.ELECTRO.get());
            affected.removeEffect(GGEffects.DENDRO.get());
            return;
        }
    }

    private boolean hasAnyElementalEffect(LivingEntity affected) {
        return affected.hasEffect(GGEffects.CRYO.get()) || affected.hasEffect(GGEffects.PYRO.get())
                || affected.hasEffect(GGEffects.ELECTRO.get()) || affected.hasEffect(GGEffects.HYDRO.get());
    }

    private void clearAllElementalEffects(LivingEntity affected) {
        if (affected.removeEffect(GGEffects.CRYO.get())) {
            return;
        } else if (affected.removeEffect(GGEffects.PYRO.get())) {
            return;
        } else if (affected.removeEffect(GGEffects.ELECTRO.get())) {
            return;
        } else if (affected.removeEffect(GGEffects.HYDRO.get())) {
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

    public static void doElementalExplosion(LivingEntity affected, DamageSource source) {
        doElementalExplosion(affected, source, 5.0D, false);
    }

    public static void doElementalExplosion(LivingEntity affected, DamageSource source, double distance, boolean fire) {
        float f = 8.0F;
        Vec3 vec3 = affected.position();

        for(LivingEntity le : affected.level.getEntitiesOfClass(Mob.class, affected.getBoundingBox().inflate(distance))) {
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
                    float f1 = f * (float)Math.sqrt((distance - (double) affected.distanceTo(le)) / distance);
                    le.hurt(source, f1);
                    if (fire) {
                        le.setSecondsOnFire(4);
                    }
                }
            }
        }
    }
}