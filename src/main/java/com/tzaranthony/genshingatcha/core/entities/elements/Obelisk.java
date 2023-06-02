package com.tzaranthony.genshingatcha.core.entities.elements;

import com.tzaranthony.genshingatcha.core.util.EntityUtil;
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
    protected int lifeTicks = 400;
    protected boolean clientSideAttackStarted;
    protected LivingEntity owner;
    @Nullable
    protected UUID ownerUUID;
    protected int constRank = 0;

    public Obelisk(EntityType<? extends EvokerFangs> type, Level level) {
        super(type, level);
    }

    public Obelisk(Level level, double x, double y, double z, float yRot, int warmup, LivingEntity le, int constRank) {
        this(EntityType.EVOKER_FANGS, level);
        this.warmupDelayTicks = warmup;
        this.setOwner(le);
        this.setYRot(yRot * (180F / (float)Math.PI));
        this.setPos(x, y, z);
        this.constRank = constRank;
    }

    protected void defineSynchedData() {
    }

    public void setOwner(LivingEntity le) {
        this.owner = le;
        this.ownerUUID = le == null ? null : le.getUUID();
    }

    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }
        return this.owner;
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        this.warmupDelayTicks = tag.getInt("Warmup");
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
        this.constRank = tag.getInt("ConstRank");
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Warmup", this.warmupDelayTicks);
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
        tag.putInt("ConstRank", this.constRank);
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (this.lifeTicks % 20 == 0) {
                    for(int i = 0; i < 12; ++i) {
                        double d0 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d1 = this.getY() + 0.05D + this.random.nextDouble();
                        double d2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d3 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = 0.3D + this.random.nextDouble() * 0.3D;
                        double d5 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        this.level.addParticle(ParticleTypes.CRIT, d0, d1, d2, d3, d4, d5);
                    }
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (!this.sentSpikeEvent) {
                this.level.broadcastEntityEvent(this, (byte) 4);
                this.sentSpikeEvent = true;
            }

            if (this.lifeTicks % 20 == 0) {
                for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.0D, 0.0D, 2.0D))) {
                    this.dealDamageTo(livingentity);
                }
            }

            if (--this.lifeTicks < 0) {
                this.discard();
            }
        }
    }

    protected void dealDamageTo(LivingEntity target) {
        if (target.isAlive() && !target.isInvulnerable() && EntityUtil.ignoreElementAttackEntity(target, this.getOwner())) {
            float dmg = 4.0F + (this.constRank >= 3 ? 2.0F : 0.0F);
            target.hurt(DamageSource.indirectMagic(this, this.getOwner()), dmg);
            target.addEffect(new MobEffectInstance(GGEffects.GEO.get()));
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