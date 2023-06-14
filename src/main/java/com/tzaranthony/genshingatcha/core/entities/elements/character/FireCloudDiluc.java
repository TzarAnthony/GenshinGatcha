package com.tzaranthony.genshingatcha.core.entities.elements.character;

import com.tzaranthony.genshingatcha.core.entities.elements.FullParticleCloudEntity;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireCloudDiluc extends FullParticleCloudEntity {
    protected boolean isSecond;

    public FireCloudDiluc(EntityType<? extends FireCloudDiluc> type, Level level) {
        super(type, level);
        this.setParticle(ParticleTypes.FLAME);
        this.lifespan = 30;
        this.setRadius(0.5F);
    }

    public FireCloudDiluc(Level level, double x, double y, double z, float yRot, int waitTime, LivingEntity owner, int constRank, boolean isSecond) {
        this(GGEntities.FIRE_CLOUD.get(), level);
        this.setPos(x, y, z);
        this.setYRot(yRot);
        this.waitTime = waitTime;
        this.owner = owner;
        this.constRank = constRank;
        this.isSecond = isSecond;
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.isSecond = tag.getBoolean("IsSecond");
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsSecond", this.isSecond);
    }

    @Override
    protected void performOnEntity(LivingEntity le) {
        if (!EntityUtil.ignoreElementAttackEntity(le, this.owner) && !EntityUtil.isEntityImmuneToElement(le, Element.E.PYRO.getId())) {
            float dmg = 2.0F + (this.constRank >= 3 ? 1.0F : 0.0F);
            dmg = dmg * (this.isSecond ? 1.4F : 1.0F);
            le.hurt(GGDamageSource.PYRO, dmg);
            le.setSecondsOnFire(10);
            le.addEffect(new MobEffectInstance(GGEffects.PYRO.get(), 100));
        }
    }

    @Override
    protected void performOnDiscard() {
        playSound(SoundEvents.FIRECHARGE_USE, 2.2F + this.random.nextFloat() * 0.2F, 0.9F + this.random.nextFloat() * 0.15F);

        float dmg = 3.5F + (this.constRank >= 3 ? 2.0F : 0.0F);
        dmg = dmg * (this.isSecond ? 1.4F : 1.0F);
        double range = 5.0D;
        Vec3 vec3 = this.position();
        for (LivingEntity le : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3.0D))) {
            if (!(this.distanceToSqr(le) > 25.0D) && !EntityUtil.ignoreElementAttackEntity(le, this.owner) && !EntityUtil.isEntityImmuneToElement(le, Element.E.PYRO.getId())) {
                boolean flag = false;
                for(int i = 0; i < 2; ++i) {
                    Vec3 vec31 = new Vec3(le.getX(), le.getY(0.5D * (double)i), le.getZ());
                    HitResult hitresult = this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    if (hitresult.getType() == HitResult.Type.MISS) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    float fDmg = dmg * (float)Math.sqrt((range - (double) this.distanceTo(le)) / range);
                    le.hurt(DamageSource.explosion(this.getOwner()), fDmg);
                }
            }
        }
        this.discard();
    }

    @Override
    protected void spawnParticles(int i, float f1, boolean flag) {
        if (!flag && this.tickCount < (this.waitTime / 2 + this.lifespan)) {
            f1 = this.getRadius() + 0.5F;

            for(int j = 0; j < 8 ; ++j) {
                float f2 = this.random.nextFloat() * ((float)Math.PI * 2F);
                float f3 = Mth.sqrt(this.random.nextFloat()) * f1;
                double x = this.getX() + (double)(Mth.cos(f2) * f3);
                double y = this.getY() + (double)(Mth.cos(f2) * f3 * 0.5F);
                double z = this.getZ() + (double)(Mth.sin(f2) * f3);
                double xd = (0.5D - this.random.nextDouble()) * 0.15D;
                double yd = (0.5D - this.random.nextDouble()) * 0.05D;
                double zd = (0.5D - this.random.nextDouble()) * 0.15D;

                this.level.addAlwaysVisibleParticle(this.getParticle(), true, x, y, z, xd, yd, zd);
            }
        }
    }
}