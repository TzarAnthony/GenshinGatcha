package com.tzaranthony.genshingatcha.core.entities.elements;

import com.tzaranthony.genshingatcha.registries.GGEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class Obelisk extends Entity {
    protected int warmupDelayTicks;
    protected boolean sentSpikeEvent;
    protected int lifeTicks = 600;
    protected boolean clientSideAttackStarted;
    @Nullable
    protected LivingEntity owner;
    @Nullable
    protected UUID ownerUUID;

    public Obelisk(EntityType<? extends EvokerFangs> type, Level level) {
        super(type, level);
    }

    public Obelisk(Level level, double x, double y, double z, float yRot, int warmup, LivingEntity le) {
        this(EntityType.EVOKER_FANGS, level);
        this.warmupDelayTicks = warmup;
        this.setOwner(le);
        this.setYRot(yRot * (180F / (float)Math.PI));
        this.setPos(x, y, z);
    }

    protected void defineSynchedData() {
    }

    public void setOwner(@Nullable LivingEntity le) {
        this.owner = le;
        this.ownerUUID = le == null ? null : le.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }
        return this.owner;
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        this.warmupDelayTicks = tag.getInt("Warmup");
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Warmup", this.warmupDelayTicks);
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (this.lifeTicks == 10) {
                    for(int i = 0; i < 12; ++i) {
                        double d0 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d1 = this.getY() + 0.05D + this.random.nextDouble();
                        double d2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d3 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = 0.3D + this.random.nextDouble() * 0.3D;
                        double d5 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        this.level.addParticle(ParticleTypes.CRIT, d0, d1 + 1.0D, d2, d3, d4, d5);
                    }
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -8) {
                for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.0D, 0.0D, 1.0D))) {
                    this.dealDamageTo(livingentity);
                }
            }

            if (!this.sentSpikeEvent) {
                this.level.broadcastEntityEvent(this, (byte) 4);
                this.sentSpikeEvent = true;
            }

            if (--this.lifeTicks < 0) {
                this.discard();
            }

            if (this.lifeTicks % 60 == 0) {
                for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.0D, 0.0D, 2.0D))) {
                    this.dealDamageTo(livingentity);
                }
            }
        }
    }

    protected void dealDamageTo(LivingEntity target) {
        LivingEntity livingentity = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && target != livingentity) {
            if (livingentity == null) {
                target.hurt(DamageSource.GENERIC, 6.0F);
            } else {
                if (livingentity.isAlliedTo(target)) {
                    return;
                }
                target.hurt(DamageSource.indirectMagic(this, livingentity), 6.0F);
                target.addEffect(new MobEffectInstance(GGEffects.GEO.get()));
            }
        }
    }

    public void handleEntityEvent(byte eBit) {
        super.handleEntityEvent(eBit);
        if (eBit == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.EVOKER_FANGS_ATTACK, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
            }
        }
    }

    public float getAnimationProgress(float pTick) {
        if (!this.clientSideAttackStarted) {
            return 0.0F;
        } else {
            int i = this.lifeTicks - 2;
            return i <= 0 ? 1.0F : 1.0F - ((float)i - pTick) / 20.0F;
        }
    }

    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}