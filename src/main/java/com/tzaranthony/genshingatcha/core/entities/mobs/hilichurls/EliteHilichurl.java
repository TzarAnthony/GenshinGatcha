package com.tzaranthony.genshingatcha.core.entities.mobs.hilichurls;

import com.tzaranthony.genshingatcha.core.entities.elements.misc.LightningSummon;
import com.tzaranthony.genshingatcha.core.entities.elements.mobs.SlimeToss;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import com.tzaranthony.genshingatcha.core.util.effects.ElementEffectInstance;
import com.tzaranthony.genshingatcha.registries.GGItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class EliteHilichurl extends AbstractHilichurl {
    protected int cannotUseItemRemaining;
    private static final UUID FAST_FALLING_ID = UUID.fromString("d4cc6724-db47-4503-969c-d9ff00aa8758");
    private static final AttributeModifier FAST_FALLING = new AttributeModifier(FAST_FALLING_ID, "Gravity acceleration", 0.08, AttributeModifier.Operation.ADDITION);
    private static final EntityDataAccessor<Boolean> JUMPING = SynchedEntityData.defineId(EliteHilichurl.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLAMMING = SynchedEntityData.defineId(EliteHilichurl.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(EliteHilichurl.class, EntityDataSerializers.BOOLEAN);
    private final UnarmedAttackGoal unarmedAttackGoal = new UnarmedAttackGoal(this, 1.5D);
    private final ChargeAttackGoal chargeAttackGoal = new ChargeAttackGoal(this, 4.0D);
    private final WeaponAttackGoal weaponAttackGoal = new WeaponAttackGoal(this, 1.2D);


    public EliteHilichurl(EntityType<? extends AbstractHilichurl> type, Level level) {
        super(type, level);
        this.assessWeaponGoal();
    }

    protected void assessWeaponGoal() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.unarmedAttackGoal);
            this.goalSelector.removeGoal(this.chargeAttackGoal);
            this.goalSelector.removeGoal(this.weaponAttackGoal);
            if (this.getMainHandItem().getItem() instanceof AxeItem) {
                this.goalSelector.addGoal(4, this.weaponAttackGoal);
            } else {
                this.goalSelector.addGoal(3, this.chargeAttackGoal);
                this.goalSelector.addGoal(4, this.unarmedAttackGoal);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.8D)
                .add(Attributes.FOLLOW_RANGE, 20.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(JUMPING, false);
        this.entityData.define(SLAMMING, false);
        this.entityData.define(CHARGING, false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("jumping", this.isJumping());
        tag.putBoolean("slamming", this.isSlamming());
        tag.putBoolean("charging", this.isCharging());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setJumping(tag.getBoolean("jumping"));
        this.setSlamming(tag.getBoolean("slamming"));
        this.setCharging(tag.getBoolean("charging"));
    }

    public void setJumping(boolean isJumping) {
        this.entityData.set(JUMPING, isJumping);
    }

    public boolean isJumping() {
        return this.entityData.get(JUMPING);
    }

    public void setSlamming(boolean isSlamming) {
        this.entityData.set(JUMPING, isSlamming);
    }

    public boolean isSlamming() {
        return this.entityData.get(JUMPING);
    }

    public void setCharging(boolean isCharging) {
        this.entityData.set(CHARGING, isCharging);
    }

    public boolean isCharging() {
        return this.entityData.get(CHARGING);
    }

    public HilichurlArmPose getArmPose() {
        return this.isAggressive() ? HilichurlArmPose.ATTACKING : HilichurlArmPose.NEUTRAL;
    }

    public void tick() {
        if (this.isJumping() && this.isOnGround()) {
            this.setJumping(false);
            if (this.getMainHandItem().getItem() instanceof AxeItem && this.getTarget() != null) {
                this.slamPositionArmed(this.getTarget());
            } else {
                this.slamPositionUnarmed();
            }
        }
        if (this.isSlamming() && this.level.isClientSide()) {
            this.setSlamming(false);
            ParticleOptions particle = Element.ElementGetter.get(this.getElement()).getParticle();
            BlockPos slamPos = this.getOnPos().relative(this.getMotionDirection());
            double x = slamPos.getX() + 0.5D;
            double y = slamPos.getY() + 0.8F;
            double z = slamPos.getZ() + 0.5D;
            for (int i = 0; i < 72; ++i) {
                int angle = i * 5;
                this.level.addParticle(particle, x, y, z, Mth.cos(angle) * 0.5D, 0.2D, Mth.sin(angle) * 0.5D);
            }
        }
        this.cannotUseItemRemaining = Math.max(this.cannotUseItemRemaining - 1, 0);
        super.tick();
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(accessor, difficulty, spawnType, groupData, tag);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get()).addTransientModifier(FAST_FALLING);
        this.populateDefaultEquipmentSlots(difficulty);
        this.populateDefaultEquipmentEnchantments(difficulty);
        return spawngroupdata;
    }

    public boolean causeFallDamage(float p_149717_, float p_149718_, DamageSource p_149719_) {
        return false;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        if ((this.getElement() == Element.E.PYRO.getId() || this.getElement() == Element.E.ELECTRO.getId()) && (this.level.random.nextInt((int) (6.0F - difficulty.getDifficulty().getId())) == 0)) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(GGItems.HILICHURL_GIANT_AXE.get()));
        }
    }

    public boolean doHurtTarget(Entity target, float physPct, float elePct) {
        if (target instanceof LivingEntity tgt) {
            float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float f1 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), tgt.getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(this);

            int i = EnchantmentHelper.getFireAspect(this);
            if (i > 0) {
                tgt.setSecondsOnFire(i * 4);
            }

            boolean flag = tgt.hurt(DamageSource.mobAttack(this), f * physPct);
            flag = flag || tgt.hurt(GGDamageSource.mobElementAttack(this, this.getElement()), f * elePct);
            if (flag) {
                if (f1 > 0.0F) {
                    tgt.knockback((f1 * 0.5F), Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
                this.doEnchantDamageEffects(this, tgt);
                this.setLastHurtMob(tgt);
            }
            return flag;
        }
        return super.doHurtTarget(target);
    }

    public void slimeToss(LivingEntity target) {
        SlimeToss slime = new SlimeToss(this, this.getElement(), this.level);
        slime.setPos(this.getX(), this.getEyeY() + 1.0F, this.getZ());
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - slime.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        slime.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(13 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.EGG_THROW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(slime);
    }

    private void jumpTowardsTarget(LivingEntity target) {
        double boost = Math.abs(Math.sin(Math.PI * 0.7D)) * 6.0D;
//        Vec3 vec3 = this.getLookAngle().normalize();
        Vec3 wantedVec3 = target.position().subtract(this.position()).normalize();
        this.push(wantedVec3.x * boost, wantedVec3.y * boost / 2, wantedVec3.z * boost);
        this.setJumping(true);
    }

    public void slamPositionUnarmed() {
        this.setSlamming(true);
        List<LivingEntity> targets = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3.5D, 0.0D, 3.5D));
        for (LivingEntity tgt : targets) {
            if (!this.isAlliedTo(tgt) || !EntityUtil.isEntityImmuneToElement(tgt, this.getElement())) {
                this.doHurtTarget(tgt, 0.6F, 0.4F);
                tgt.addEffect(new ElementEffectInstance(this.getElement(), 150));
            }
        }
    }

    public void slamPositionArmed(LivingEntity target) {
        this.slamPositionUnarmed();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        float pAngle = (float) Mth.atan2(target.getZ() - z, target.getX() - x);
        for (int i = 0; i < 5; ++i) {
            float nAngle = pAngle + (((float) i - 2.0F) * 0.4F);
            double cos = Mth.cos(nAngle);
            double sin = Mth.sin(nAngle);
            for (int j = 0; j < 3; ++j) {
                double distAlong = 2.25D * (double) (j + 2);
                BlockPos spawnPos = EntityUtil.getFloorInRange(this.level, x + cos * distAlong, Math.min(target.getY(), y) - 2.0D, Math.max(target.getY(), y) + 2.0D, z + sin * distAlong);
                if (spawnPos != null) {
                    this.level.addFreshEntity(new LightningSummon(this.level, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), pAngle, j * 20, this));
                }
            }
        }
    }

    class UnarmedAttackGoal extends MeleeAttackGoal {
        EliteHilichurl hill;

        public UnarmedAttackGoal(EliteHilichurl hilichurl, double speedMod) {
            super(hilichurl, speedMod, false);
            hill = hilichurl;
        }

        protected void checkAndPerformAttack(LivingEntity tgt, double distance) {
            if (this.getTicksUntilNextAttack() <= 0) {
                if (distance <= (900.0F)) {
                    if (this.hill.level.random.nextInt(5) == 0) {
                        this.hill.slimeToss(tgt);
                    }
                    this.hill.jumpTowardsTarget(tgt);
                }

                if (distance <= (100.0F)) {
                    this.hill.setCharging(true);
                }

                if (distance <= this.getAttackReachSqr(tgt)) {
                    this.resetAttackCooldown();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    if (this.hill.level.random.nextInt(10) == 0) {
                        this.hill.slamPositionUnarmed();
                    } else {
                        this.hill.doHurtTarget(tgt);
                    }
                }
                this.resetAttackCooldown();
            }
        }
    }

    class WeaponAttackGoal extends MeleeAttackGoal {
        EliteHilichurl hill;

        public WeaponAttackGoal(EliteHilichurl hilichurl, double speedMod) {
            super(hilichurl, speedMod, false);
            hill = hilichurl;
        }

        protected void checkAndPerformAttack(LivingEntity tgt, double distance) {
            if (this.getTicksUntilNextAttack() <= 0) {
                if (distance <= (900.0F)) {
                    if (this.hill.level.random.nextInt(5) == 0) {
                        this.hill.slimeToss(tgt);
                    }
                    this.hill.jumpTowardsTarget(tgt);
                }

                if (distance <= (this.getAttackReachSqr(tgt) + 3.0F)) {
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    if (this.hill.level.random.nextInt(10) == 0) {
                        this.hill.slamPositionArmed(tgt);
                    } else {
                        this.hill.doHurtTarget(tgt, 0.8F, 0.2F);
                    }
                }
                this.resetAttackCooldown();
            }
        }
    }

    class ChargeAttackGoal extends Goal {
        protected final EliteHilichurl hill;
        private final double speedModifier;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private final int maxChargeTicks = 120;
        private int chargeTicks = 120;
        private int failedPathFindingPenalty = 0;
        private BlockPos targetPos;

        public ChargeAttackGoal(EliteHilichurl hill, double speedMod) {
            this.hill = hill;
            this.speedModifier = speedMod;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            if (!this.hill.isCharging()) {
                return false;
            } else {
                LivingEntity target = this.hill.getTarget();
                if (target == null) {
                    return false;
                } else if (!target.isAlive() || this.hill.distanceToSqr(target) > 100.0F) {
                    return false;
                } else {
                    Vec3 endVec3 = target.position().subtract(this.hill.position());
                    if (this.hill.distanceToSqr(target) < 25.0F) {
                        endVec3.multiply(2.5D, 1.0D, 2.5D);
                    } else {
                        endVec3.multiply(1.2D, 1.0D, 1.2D);
                    }
                    BlockPos pos = new BlockPos(endVec3);
                    this.targetPos = EntityUtil.getFloorInRange(this.hill.level, pos.getX(), pos.getY() - 6.0D, pos.getY() + 6.0D, pos.getZ());
                    this.path = this.hill.getNavigation().createPath(this.targetPos, 0);
                    this.pathedTargetX = this.targetPos.getX();
                    this.pathedTargetY = this.targetPos.getY();
                    this.pathedTargetZ = this.targetPos.getZ();
                    return this.path != null;
                }
            }
        }

        public boolean canContinueToUse() {
            LivingEntity target = this.hill.getTarget();
            if (target == null && this.targetPos != null) {
                return false;
            } else if (!target.isAlive() || !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
                return false;
            } else {
                return this.chargeTicks > 0;
            }
        }

        public void start() {
            this.hill.getNavigation().moveTo(this.path, this.speedModifier);
            this.hill.setAggressive(true);
        }

        public void stop() {
            LivingEntity livingentity = this.hill.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.hill.setTarget(null);
            }
            this.hill.setAggressive(false);
            this.hill.getNavigation().stop();
            this.resetChargeTicks();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            --this.chargeTicks;
            LivingEntity livingentity = this.hill.getTarget();
            if (livingentity != null) {
                this.hill.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                this.hill.getNavigation().moveTo(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ, this.speedModifier);
                this.hitEntities();
            }
        }

        protected void hitEntities() {
            for(LivingEntity target : this.hill.level.getEntitiesOfClass(LivingEntity.class, this.hill.getBoundingBox().inflate(1.0D), (entity) -> {return entity.isAlive();})) {
                if (!(target instanceof AbstractIllager)) {
                    target.hurt(DamageSource.mobAttack(this.hill), (float) this.hill.getAttributeValue(Attributes.ATTACK_DAMAGE));
                }
                this.strongKnockback(target);
            }
        }

        private void strongKnockback(Entity target) {
            double d0 = target.getX() - this.hill.getX();
            double d1 = target.getZ() - this.hill.getZ();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
            target.push(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
        }

        protected void resetChargeTicks() {
            this.chargeTicks = this.maxChargeTicks;
        }
    }
}