package com.tzaranthony.genshingatcha.core.entities.elements;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.MagicExplosion;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FallingMeteor extends Projectile {
    private static final EntityDataAccessor<Integer> DATA_ROTATION = SynchedEntityData.defineId(FallingMeteor.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_SIZE = SynchedEntityData.defineId(FallingMeteor.class, EntityDataSerializers.FLOAT);
    protected int constRank;

    public FallingMeteor(EntityType<? extends FallingMeteor> type, Level level) {
        super(type, level);
    }

    public FallingMeteor(Level level, double x, double y, double z, LivingEntity owner, int constRank) {
        this(GGEntities.METEOR.get(), level);
        this.setPos(x - 40 + this.random.nextInt(80), level.getMaxBuildHeight() + 20, z - 40 + this.random.nextInt(80));
        this.calculateVectorAndStartFalling(x, y, z);
        this.setSize(5.0F);
        this.setOwner(owner);
        this.constRank = constRank;
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.constRank = tag.getInt("ConstRank");
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ConstRank", this.constRank);
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
        float radius = 10.0F;
        if (this.constRank >= 4) {
            radius *= 1.2F;
        }

        float dmgBonus = 0.0F;
        if (this.constRank >= 5) {
            dmgBonus = 5.0F;
        }

        MagicExplosion explosion = new MagicExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), radius, Element.E.GEO.getId(), dmgBonus);
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level, explosion)) return;
        explosion.explode();
        explosion.finalizeExplosion(true);

        if (this.constRank >= 2) {
            float f2 = radius * 2.0F;
            double k1 = Mth.floor(this.getX() - (double)f2 - 1.0D);
            double l1 = Mth.floor(this.getX() + (double)f2 + 1.0D);
            double i2 = Mth.floor(this.getY() - (double)f2 - 1.0D);
            double i1 = Mth.floor(this.getY() + (double)f2 + 1.0D);
            double j2 = Mth.floor(this.getZ() - (double)f2 - 1.0D);
            double j1 = Mth.floor(this.getZ() + (double)f2 + 1.0D);
            List<Player> players = this.level.getEntitiesOfClass(Player.class, new AABB(k1, i2, j2, l1, i1, j1));
            for (Player player : players) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 3));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 4));
                if (this.constRank >= 6) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60));
                }
            }
        }
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