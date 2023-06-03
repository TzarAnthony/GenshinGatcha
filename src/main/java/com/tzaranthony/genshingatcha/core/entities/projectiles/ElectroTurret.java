package com.tzaranthony.genshingatcha.core.entities.projectiles;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ElectroTurret extends Entity {
    protected int constRank = 0;
    protected int lifespan = 400;
    protected float dmg;
    protected LivingEntity owner;
    protected UUID ownerUUID;
    protected pseudoLookControl lookControl;

    public ElectroTurret(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.lookControl = new pseudoLookControl(this);
    }

    public ElectroTurret(Level level, double x, double y, double z, float yRot, LivingEntity owner, int constRank) {
        this(GGEntities.FIRE_CLOUD.get(), level);
        this.setPos(x, y, z);
        this.setYRot(yRot);
        this.owner = owner;
        this.dmg = 1.0F + (float) this.owner.getAttributes().getValue(Attributes.ATTACK_DAMAGE);
        if (this.constRank >= 3) {
            this.dmg *= 1.5F;
        }
        this.constRank = constRank;
        if (this.constRank >= 6) {
            this.lifespan += 200;
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
        this.lifespan = tag.getInt("Duration");
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
        this.constRank = tag.getInt("ConstRank");
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
        tag.putInt("Duration", this.lifespan);
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
        tag.putInt("ConstRank", this.constRank);
    }

    @Override
    public void tick() {
        super.tick();
        this.lookControl.tick();
        if (this.level.isClientSide) {
        } else {
            if (this.tickCount >= this.lifespan) {
                this.discard();
                return;
            }
            if (this.tickCount == 5) {
                List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5));
                for (LivingEntity le : entities) {
                    if (!EntityUtil.ignoreElementAttackEntity(le, this.owner)) {
                        le.hurt(DamageSource.indirectMagic(this, this.owner), dmg);
                        le.addEffect(new MobEffectInstance(GGEffects.ELECTRO.get(), 200));
                    }
                }
            } else if (this.tickCount % 20 == 0) {
                int range = (this.constRank >= 2 ? 30 : 20);
                Monster tgt = this.level.getNearestEntity(Monster.class, TargetingConditions.forCombat().range(range), null, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(5));
                this.lookControl.setLookAt(tgt.getX(), this.getY(), tgt.getZ());
                shootCrossbowProjectile(this, tgt);
            }
        }
    }

    private void shootCrossbowProjectile(Entity shooter, LivingEntity target) {
        Projectile prj = this.getArrow();
        double d0 = target.getX() - shooter.getX();
        double d1 = target.getZ() - shooter.getZ();
        double d2 = Math.sqrt(d0 * d0 + d1 * d1);
        double d3 = target.getY(0.3333333333333333D) - prj.getY() + d2 * (double)0.2F;
        Vector3f vector3f = this.getProjectileShotVector(shooter, new Vec3(d0, d3, d1), 0.0F);
        prj.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 1.6F, (float)(14 - shooter.level.getDifficulty().getId() * 4));
        shooter.playSound(SoundEvents.LIGHTNING_BOLT_IMPACT, 1.0F, 1.0F / (shooter.level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    private Vector3f getProjectileShotVector(Entity shooter, Vec3 vec, float offset) {
        Vec3 vec3 = vec.normalize();
        Vec3 vec31 = vec3.cross(new Vec3(0.0D, 1.0D, 0.0D));
        if (vec31.lengthSqr() <= 1.0E-7D) {
            vec31 = vec3.cross(shooter.getUpVector(1.0F));
        }

        Quaternion quaternion = new Quaternion(new Vector3f(vec31), 90.0F, true);
        Vector3f vector3f = new Vector3f(vec3);
        vector3f.transform(quaternion);
        Quaternion quaternion1 = new Quaternion(vector3f, offset, true);
        Vector3f vector3f1 = new Vector3f(vec3);
        vector3f1.transform(quaternion1);
        return vector3f1;
    }

    private AbstractArrow getArrow() {
        AbstractArrow abstractarrow = new ElementalArrow(this.owner, Element.E.ELECTRO, this.level);
        abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractarrow.setShotFromCrossbow(true);
        if (this.constRank >= 3) {
            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + 2.5D);
        }
        return abstractarrow;
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    class pseudoLookControl {
        protected final ElectroTurret turret;
        protected float yMaxRotSpeed;
        protected float xMaxRotAngle;
        protected int lookAtCooldown;
        protected double wantedX;
        protected double wantedY;
        protected double wantedZ;

        public pseudoLookControl(ElectroTurret p_24945_) {
            this.turret = p_24945_;
        }

        public void setLookAt(double p_24947_, double p_24948_, double p_24949_) {
            this.setLookAt(p_24947_, p_24948_, p_24949_, 10.0F, 360.0F);
        }

        public void setLookAt(double p_24951_, double p_24952_, double p_24953_, float p_24954_, float p_24955_) {
            this.wantedX = p_24951_;
            this.wantedY = p_24952_;
            this.wantedZ = p_24953_;
            this.yMaxRotSpeed = p_24954_;
            this.xMaxRotAngle = p_24955_;
            this.lookAtCooldown = 2;
        }

        public void tick() {
            if (this.resetXRotOnTick()) {
                this.turret.setXRot(0.0F);
            }

            if (this.lookAtCooldown > 0) {
                --this.lookAtCooldown;
                this.getYRotD().ifPresent((p_181130_) -> {
                    this.turret.yRotO = this.rotateTowards(this.turret.yRotO, p_181130_, this.yMaxRotSpeed);
                });
                this.getXRotD().ifPresent((p_181128_) -> {
                    this.turret.setXRot(this.rotateTowards(this.turret.getXRot(), p_181128_, this.xMaxRotAngle));
                });
            } else {
                this.turret.yRotO = this.rotateTowards(this.turret.yRotO, this.turret.yRotO, 10.0F);
            }
        }

        protected boolean resetXRotOnTick() {
            return true;
        }

        protected Optional<Float> getXRotD() {
            double d0 = this.wantedX - this.turret.getX();
            double d1 = this.wantedY - this.turret.getEyeY();
            double d2 = this.wantedZ - this.turret.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d3) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI))));
        }

        protected Optional<Float> getYRotD() {
            double d0 = this.wantedX - this.turret.getX();
            double d1 = this.wantedZ - this.turret.getZ();
            return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d0) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F);
        }

        protected float rotateTowards(float p_24957_, float p_24958_, float p_24959_) {
            float f = Mth.degreesDifference(p_24957_, p_24958_);
            float f1 = Mth.clamp(f, -p_24959_, p_24959_);
            return p_24957_ + f1;
        }
    }
}