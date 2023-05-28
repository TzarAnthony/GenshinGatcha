package com.tzaranthony.genshingatcha.core.entities.elements;

import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ElectroSlice extends AbstractMagicProjectile {
    public ElectroSlice(EntityType<? extends AbstractMagicProjectile> projectile, Level level) {
        super(projectile, level);
    }

    public ElectroSlice(EntityType<? extends AbstractMagicProjectile> projectile, Level level, Entity owner, double x, double y, double z, int constRank) {
        this(projectile, level);
        this.setOwner(owner);
        this.setPos(x, y, z);
        this.setDeltaMovement(this.random.nextGaussian() * 0.001D, 0.05D, this.random.nextGaussian() * 0.001D);
        this.constRank = constRank;
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
        float dmg = 10.0F + (this.constRank >= 5 ? 5.0F : 0.0F);
        target.hurt(DamageSource.indirectMagic(this, this.getOwner()), dmg);
        if (target instanceof LivingEntity) {
            ((LivingEntity) target).addEffect(new MobEffectInstance(GGEffects.ELECTRO.get(), 300));
        }
    }

    @Override
    protected void performOnBlock(Entity owner, Level level, BlockPos pos) {
    }

    @Override
    protected void fizzle() {
        if (this.constRank >= 4 && this.getOwner() instanceof Player player) {
            float dmg = (float) (player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).getValue() * 2.22D);
            for (LivingEntity le : player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(8))) {
                if (!EntityUtil.ignoreElementAttackEntity(le, player)) {
                    le.hurt(DamageSource.indirectMagic(this, this.getOwner()), dmg);
                }
            }

            player.heal(player.getMaxHealth() * 0.2F);
        }
        super.fizzle();
    }
}