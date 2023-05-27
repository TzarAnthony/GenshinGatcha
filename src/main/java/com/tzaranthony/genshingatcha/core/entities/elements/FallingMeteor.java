package com.tzaranthony.genshingatcha.core.entities.elements;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class FallingMeteor extends Projectile {
    private static final EntityDataAccessor<Integer> DATA_ROTATION = SynchedEntityData.defineId(FallingMeteor.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_SIZE = SynchedEntityData.defineId(FallingMeteor.class, EntityDataSerializers.FLOAT);

    public FallingMeteor(EntityType<? extends FallingMeteor> type, Level level) {
        super(type, level);
    }

    public FallingMeteor(Level level, double x, double y, double z, float size, LivingEntity owner) {
        this(GGEntities.METEOR.get(), level);
        this.setPos(x - 40 + this.random.nextInt(80), level.getMaxBuildHeight() + 20, z - 40 + this.random.nextInt(80));
        this.calculateVectorAndStartFalling(x, y, z);
        this.setSize(size);
        this.setOwner(owner);
    }


    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ROTATION, 0);
        this.entityData.define(DATA_SIZE, 0.5F);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (DATA_SIZE.equals(dataAccessor)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        this.move(MoverType.SELF, vec3);
        this.setDeltaMovement(vec3);

        this.setRotation(this.getRotation() + 5);
        if (this.getRotation() > 359) {
            this.setRotation(0);
        }

        if (this.isOnGround()) {
            this.discard();
            this.explode();
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        this.explode();
    }

    protected void calculateVectorAndStartFalling(double x, double y, double z) {
        double d0 = x - this.getX();
        double d1 = z - this.getZ();
        double d2 = Math.sqrt(d0 * d0 + d1 * d1);
        double d3 = y - this.getY() + d2 * (double)0.2F;
        Vector3f vector3f = this.getProjectileShotVector(new Vec3(x, y ,z), new Vec3(d0, d3, d1));
        this.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 4.0F, 0);
    }

    protected Vector3f getProjectileShotVector(Vec3 target, Vec3 path) {
        Vec3 vec3 = path.normalize();
        Vec3 vec31 = vec3.cross(new Vec3(0.0D, 1.0D, 0.0D));
        if (vec31.lengthSqr() <= 1.0E-7D) {
            vec31 = vec3.cross(target);
        }

        Quaternion quaternion = new Quaternion(new Vector3f(vec31), 90.0F, true);
        Vector3f vector3f = new Vector3f(vec3);
        vector3f.transform(quaternion);
        Quaternion quaternion1 = new Quaternion(vector3f, 0.0F, true);
        Vector3f vector3f1 = new Vector3f(vec3);
        vector3f1.transform(quaternion1);
        return vector3f1;
    }

    public void setRotation(int rotation) {
        this.entityData.set(DATA_ROTATION, rotation);
    }

    public int getRotation() {
        return this.entityData.get(DATA_ROTATION);
    }

    public void setSize(float size) {
        this.entityData.set(DATA_SIZE, Math.min(size, 3.0F));
    }

    public float getSize() {
        return Math.max(this.entityData.get(DATA_SIZE) * 3.0F, 1.0F);
    }

    protected void explode() {
        this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 10.0F, Explosion.BlockInteraction.NONE);

//        MagicExplosion explosion = new MagicExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), 10.0F + (this.getSize() * 2.5F));
//        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level, explosion)) return;
//        explosion.explode();
//        explosion.finalizeExplosion(true);
    }

    public float getBlockExplosionResistance(Explosion explosion, BlockGetter getter, BlockPos pos, BlockState bState, FluidState fState, float resistance) {
        return !fState.isEmpty() ? Math.min(0.8F, resistance) : (!bState.isAir() && !bState.is(BlockTags.WITHER_IMMUNE) ? Math.min(3.0F, resistance)/2 : resistance);
    }

    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.getSize(), this.getSize());
    }
}