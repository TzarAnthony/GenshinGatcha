package com.tzaranthony.genshingatcha.core.entities.elements.misc;

import com.google.common.collect.Maps;
import com.tzaranthony.genshingatcha.core.entities.elements.ParticleCloudEntity;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import com.tzaranthony.genshingatcha.core.util.effects.ElementEffectInstance;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class AreaFirePlume extends ParticleCloudEntity {
    private final Map<Entity, Integer> victims = Maps.newHashMap();
    private int reapplicationDelay = 20;

    public AreaFirePlume(EntityType<? extends AreaFirePlume> wall, Level level) {
        super(wall, level);
        this.setParticle(ParticleTypes.FLAME);
        this.setRadius(1.0F);
        this.lifespan = 40;
    }

    public AreaFirePlume(Level level, double x, double y, double z, float yRot, int waitTime, LivingEntity owner) {
        this(GGEntities.FIRE_PLUME.get(), level);
        this.setPos(x, y, z);
        this.setYRot(yRot);
        this.waitTime = waitTime;
        this.owner = owner;
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.reapplicationDelay = tag.getInt("ReapplicationDelay");
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ReapplicationDelay", this.reapplicationDelay);
    }

    public void tick() {
        super.tick();
        float f = this.getRadius();
        if (!this.level.isClientSide || this.tickCount < this.waitTime) {
            if (this.tickCount % 5 == 0) {
                this.victims.entrySet().removeIf((p_146784_) -> {
                    return this.tickCount >= p_146784_.getValue();
                });
                List<LivingEntity> list1 = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                if (!list1.isEmpty()) {
                    for(LivingEntity target : list1) {
                        if (!EntityUtil.ignoreElementAttackEntity(target, this.owner) && !EntityUtil.isEntityImmuneToElement(target, Element.E.PYRO.getId()) && !this.victims.containsKey(target) && !target.fireImmune()) {
                            double d8 = target.getX() - this.getX();
                            double d1 = target.getZ() - this.getZ();
                            double d3 = d8 * d8 + d1 * d1;
                            if (d3 <= (double)(f * f)) {
                                this.victims.put(target, this.tickCount + this.reapplicationDelay);
                                target.setSecondsOnFire(10);
                                target.addEffect(new ElementEffectInstance(GGEffects.PYRO.get()));
                                if (this.getOwner() != null) {
                                    target.hurt(GGDamageSource.indirectMagicElement(this, this.getOwner(), Element.E.PYRO.getId()), 10.0F);
                                } else {
                                    target.hurt(GGDamageSource.ELECTRO, 10.0F);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void spawnParticles(int i, float f1, boolean flag) {
        ParticleOptions particleoptions = this.getParticle();
        if (this.tickCount < this.waitTime) {
            for(int j = 0; j < i; ++j) {
                double d0 = this.getX();
                double d2 = this.getY();
                double d4 = this.getZ();
                this.level.addAlwaysVisibleParticle(particleoptions, d0, d2, d4, 0.0D, 0.0D, 0.0D);
            }
        }

        for(int j = 0; j < i; ++j) {
            double d0 = this.getX();
            double d2 = this.getY();
            double d4 = this.getZ();
            double d5 = (0.5D - this.random.nextDouble()) * 0.15D;
            double d6 = (0.01D + this.random.nextDouble()) * 0.15D + 0.1D;
            double d7 = (0.5D - this.random.nextDouble()) * 0.15D;
            this.level.addAlwaysVisibleParticle(particleoptions, d0, d2, d4, d5, d6, d7);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 4.0F);
    }
}