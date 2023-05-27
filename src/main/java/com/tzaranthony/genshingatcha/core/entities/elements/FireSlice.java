package com.tzaranthony.genshingatcha.core.entities.elements;

import com.tzaranthony.genshingatcha.registries.GGEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FireSlice extends AbstractMagicProjectile {
    public FireSlice(EntityType<? extends AbstractMagicProjectile> projectile, Level level) {
        super(projectile, level);
    }

    public FireSlice(EntityType<? extends AbstractMagicProjectile> projectile, Level level, Entity owner, double x, double y, double z) {
        this(projectile, level);
        this.setOwner(owner);
        this.setPos(x, y, z);
        this.setDeltaMovement(this.random.nextGaussian() * 0.001D, 0.05D, this.random.nextGaussian() * 0.001D);
    }

    @Override
    protected void playCustomSound() {
        this.playSound(SoundEvents.FIRECHARGE_USE, 1, 1);
    }

    @Override
    protected void playOptionalParticle() {
    }

    @Override
    protected void performOnEntity(Entity target, Entity user) {
        target.setSecondsOnFire(15);
        target.hurt(DamageSource.indirectMagic(this, user).setIsFire(), 15.0F);
        if (target instanceof LivingEntity) {
            ((LivingEntity) target).addEffect(new MobEffectInstance(GGEffects.PYRO.get(), 300));
        }
    }

    @Override
    protected void performSpellOnBlock(Entity owner, Level level, BlockPos pos) {
    }
}