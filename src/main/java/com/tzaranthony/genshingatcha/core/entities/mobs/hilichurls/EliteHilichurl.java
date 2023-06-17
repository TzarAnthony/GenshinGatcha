package com.tzaranthony.genshingatcha.core.entities.mobs.hilichurls;

import com.tzaranthony.genshingatcha.core.entities.elements.misc.LightningSummon;
import com.tzaranthony.genshingatcha.core.entities.elements.mobs.SlimeToss;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import com.tzaranthony.genshingatcha.core.util.effects.ElementEffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EliteHilichurl extends AbstractHilichurl {
    protected int cannotUseItemRemaining;
    private static final UUID FAST_FALLING_ID = UUID.fromString("d4cc6724-db47-4503-969c-d9ff00aa8758");
    private static final AttributeModifier FAST_FALLING = new AttributeModifier(FAST_FALLING_ID, "Gravity acceleration", 0.08, AttributeModifier.Operation.ADDITION); // Add -0.07 to 0.08 so we get the vanilla default of 0.01

    private final UnarmedAttackGoal unarmedAttackGoal = new UnarmedAttackGoal(this, 1.5D);
    private final WeaponAttackGoal weaponAttackGoal = new WeaponAttackGoal(this, 1.2D);


    public EliteHilichurl(EntityType<? extends AbstractHilichurl> type, Level level) {
        super(type, level);
        this.assessWeaponGoal();
    }

    protected void assessWeaponGoal() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.unarmedAttackGoal);
            this.goalSelector.removeGoal(this.weaponAttackGoal);
            ItemStack itemstack = this.getMainHandItem();
            if (itemstack.getItem() instanceof AxeItem) {
                this.goalSelector.addGoal(4, this.weaponAttackGoal);
            } else {
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

    public HilichurlArmPose getArmPose() {
        return this.isAggressive() ? HilichurlArmPose.ATTACKING : HilichurlArmPose.NEUTRAL;
    }

    public void tick() {
        if (!this.isUsingItem() && this.getTarget() != null && this.cannotUseItemRemaining <= 0) {
            if (((this.getTarget().isUsingItem() && this.getTarget().getUseItem().getItem() instanceof ProjectileWeaponItem) || this.distanceTo(this.getTarget()) > 10) && this.random.nextInt(50) == 0) {
                this.startUsingItem(InteractionHand.OFF_HAND);
                this.useItemRemaining = 200;
            }
        }
        if (this.isUsingItem()) {
            this.useItemRemaining = Math.max(this.useItemRemaining - 1, 0);
            if (this.useItemRemaining <= 0) {
                this.stopUsingItem();
            }
        }
        this.cannotUseItemRemaining = Math.max(this.cannotUseItemRemaining - 1, 0);
        super.tick();
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(accessor, difficulty, spawnType, groupData, tag);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        this.populateDefaultEquipmentSlots(difficulty);
        this.populateDefaultEquipmentEnchantments(difficulty);
        return spawngroupdata;
    }

    public boolean causeFallDamage(float p_149717_, float p_149718_, DamageSource p_149719_) {
        return false;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        if ((this.getElement() == Element.E.PYRO.getId() || this.getElement() == Element.E.ELECTRO.getId()) && (this.level.random.nextInt((int) (6.0F - difficulty.getDifficulty().getId())) == 0)) {
            //replace with giant axe
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
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

    public void slamPositionUnarmed() {
        List<LivingEntity> targets = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3.5D, 0.0D, 3.5D));
        for (LivingEntity tgt : targets) {
            if (!this.isAlliedTo(tgt) || !EntityUtil.isEntityImmuneToElement(tgt, this.getElement())) {
                this.doHurtTarget(tgt, 0.6F, 0.4F);
            }
        }

        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY() - 0.3D, this.getZ());
        areaeffectcloud.setOwner(this);
        areaeffectcloud.setRadius(3.5F);
        areaeffectcloud.setRadiusOnUse(-0.5F);
        areaeffectcloud.setWaitTime(1);
        areaeffectcloud.setDuration(20);
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
        areaeffectcloud.addEffect(new ElementEffectInstance(this.getElement(), 150));
        this.level.addFreshEntity(areaeffectcloud);
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
                double d0 = this.getAttackReachSqr(tgt); //~1.5 * 2 + 1.5 * 2 + x = 9 + x
                if (distance <= 20.0F * 20.0F) {// 30
                    if (this.hill.level.random.nextInt(5) == 0) {
                        this.hill.slimeToss(tgt);
                    }
                    //can jump and slam the ground (60% non-elemental, 40% elemental) --- 80%
                }

                if (distance <= 10.0F * 10.F) {
                    //can dash at a target and will damage the target on collision (100% non-elemental)
                }

                if (distance <= 3.0F * 3.0F) {// 9
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
                if (distance <= 25.0F * 25.0F) {// 30
                    if (this.hill.level.random.nextInt(5) == 0) {
                        this.hill.slimeToss(tgt);
                    }
                    //can do jump slam attack that creates 5 lines of elements in a cone --- 80%
                }

                if (distance <= (this.getAttackReachSqr(tgt) + 3.0F)) {// 11
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
}