package com.tzaranthony.genshingatcha.core.entities.elements;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class ParticleCloudEntity extends Entity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(ParticleCloudEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(ParticleCloudEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE = SynchedEntityData.defineId(ParticleCloudEntity.class, EntityDataSerializers.PARTICLE);
    protected int constRank = 0;
    protected int lifespan = 600;
    protected int waitTime = 20;
    @Nullable
    protected LivingEntity owner;
    @Nullable
    protected UUID ownerUUID;

    public ParticleCloudEntity(EntityType<? extends ParticleCloudEntity> wall, Level level) {
        super(wall, level);
        this.noPhysics = true;
    }

    protected void defineSynchedData() {
        this.getEntityData().define(DATA_RADIUS, 0.5F);
        this.getEntityData().define(DATA_WAITING, false);
        this.getEntityData().define(DATA_PARTICLE, ParticleTypes.ENTITY_EFFECT);
    }

    public void setRadius(float p_19713_) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(p_19713_, 0.0F, 32.0F));
        }
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
        this.lifespan = tag.getInt("Duration");
        this.waitTime = tag.getInt("WaitTime");
        this.setRadius(tag.getFloat("Radius"));
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
        this.constRank = tag.getInt("ConstRank");
        if (tag.contains("Particle", 8)) {
            try {
                this.setParticle(ParticleArgument.readParticle(new StringReader(tag.getString("Particle"))));
            } catch (CommandSyntaxException commandsyntaxexception) {
                LOGGER.warn("Couldn't load custom particle {}", tag.getString("Particle"), commandsyntaxexception);
            }
        }
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
        tag.putInt("Duration", this.lifespan);
        tag.putInt("WaitTime", this.waitTime);
        tag.putFloat("Radius", this.getRadius());
        tag.putString("Particle", this.getParticle().writeToString());
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
        tag.putInt("ConstRank", this.constRank);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (DATA_RADIUS.equals(dataAccessor)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 2.0F);
    }

    public void tick() {
        super.tick();
        boolean flag = this.isWaiting();
        float f = this.getRadius();
        if (this.level.isClientSide) {
            int i;
            float f1;
            if (flag) {
                i = 2;
                f1 = 0.2F;
            } else {
                i = Mth.ceil((float)Math.PI * f * f);
                f1 = f;
            }

            spawnParticles(i, f1, flag);
        } else {
            if (this.tickCount >= this.waitTime + this.lifespan) {
                this.discard();
                return;
            }

            boolean flag1 = this.tickCount < this.waitTime;
            if (flag != flag1) {
                this.setWaiting(flag1);
            }

            if (flag1) {
                return;
            }
        }
    }

    protected abstract void spawnParticles(int i, float f1, boolean flag);

    public void setOwner(@Nullable LivingEntity p_19719_) {
        this.owner = p_19719_;
        this.ownerUUID = p_19719_ == null ? null : p_19719_.getUUID();
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

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    public ParticleOptions getParticle() {
        return this.getEntityData().get(DATA_PARTICLE);
    }

    public void setParticle(ParticleOptions p_19725_) {
        this.getEntityData().set(DATA_PARTICLE, p_19725_);
    }

    protected void setWaiting(boolean p_19731_) {
        this.getEntityData().set(DATA_WAITING, p_19731_);
    }

    public boolean isWaiting() {
        return this.getEntityData().get(DATA_WAITING);
    }
}