package com.tzaranthony.genshingatcha.core.entities.elements;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public abstract class AbstractMagicProjectile extends Projectile {
    private static final Logger LOGGER = LogUtils.getLogger();
    private int life;
    private int lifetime = 20 + this.random.nextInt(3);
    protected int constRank = 0;
    protected boolean discardOnEntityHit = false;
    private Class ignoreType = null;

    public AbstractMagicProjectile(EntityType<? extends AbstractMagicProjectile> projectile, Level level) {
        super(projectile, level);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double dist) {
        return dist < 4096.0D;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return super.shouldRender(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 vec33 = this.getDeltaMovement();
        this.move(MoverType.SELF, vec33);
        this.setDeltaMovement(vec33);

        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        this.onHit(hitresult);

        this.updateRotation();
        if (this.life == 0 && !this.isSilent() && this.getOwner() != null) {
            this.playCustomSound();
        }

        ++this.life;
        if (this.level.isClientSide && this.life % 2 < 2) {
            this.playOptionalParticle();
        }

        if (!this.level.isClientSide && this.life > this.lifetime) {
            this.fizzle();
        }
    }

    protected abstract void playCustomSound();

    protected abstract void playOptionalParticle();

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        Entity user = this.getOwner();
        if ((target.getClass() != this.ignoreType)) {
            this.performOnEntity(target, user);
            if (this.discardOnEntityHit) {
                this.fizzle();
            }
        }
    }

    protected void onHitBlock(BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        super.onHitBlock(result);
        this.performOnBlock(this.getOwner(), this.level, pos);
        if (!this.noPhysics) {
            this.fizzle();
        }
    }

    protected abstract void performOnEntity(Entity target, Entity user);

    protected abstract void performOnBlock(Entity owner, Level level, BlockPos pos);

    protected void fizzle() {
        this.level.broadcastEntityEvent(this, (byte)17);
        this.discard();
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
        this.life = tag.getInt("Life");
        this.lifetime = tag.getInt("MaxLife");
        if (tag.hasUUID("Owner")) {
            this.setOwner(this.level.getPlayerByUUID(tag.getUUID("Owner")));
        }
        this.constRank = tag.getInt("ConstRank");
        this.discardOnEntityHit = tag.getBoolean("ShouldDiscard");
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
        tag.putInt("Life", this.life);
        tag.putInt("MaxLife", this.lifetime);
        if (this.getOwner() != null) {
            tag.putUUID("Owner", this.getOwner().getUUID());
        }
        tag.putInt("ConstRank", this.constRank);
        tag.putBoolean("ShouldDiscard", this.discardOnEntityHit);
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public boolean isAttackable() {
        return false;
    }
}