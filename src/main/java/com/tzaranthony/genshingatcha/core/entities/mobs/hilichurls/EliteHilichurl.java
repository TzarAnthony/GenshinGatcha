package com.tzaranthony.genshingatcha.core.entities.mobs.hilichurls;

import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.UUID;

public class EliteHilichurl extends AbstractHilichurl {
    protected int cannotUseItemRemaining;
    private static final UUID FAST_FALLING_ID = UUID.fromString("d4cc6724-db47-4503-969c-d9ff00aa8758");
    private static final AttributeModifier FAST_FALLING = new AttributeModifier(FAST_FALLING_ID, "Gravity acceleration", 0.08, AttributeModifier.Operation.ADDITION); // Add -0.07 to 0.08 so we get the vanilla default of 0.01

    public EliteHilichurl(EntityType<? extends AbstractHilichurl> type, Level level) {
        super(type, level);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new WeaponMeleeAttackGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
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

    @Override
    public boolean doHurtTarget(Entity target) {
        if (target instanceof LivingEntity tgt) {
            float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float f1 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), tgt.getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(this);

            int i = EnchantmentHelper.getFireAspect(this);
            if (i > 0) {
                tgt.setSecondsOnFire(i * 4);
            }

            tgt.addEffect(new MobEffectInstance(Element.ElementGetter.get(this.getElement()).getEffect(), 100));
            boolean flag = tgt.hurt(GGDamageSource.mobElementAttack(this, this.getElement()), f);
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

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        if (this.getElement() == Element.E.PYRO.getId()) {
            //replace with giant axe
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        }
    }

    class WeaponMeleeAttackGoal extends MeleeAttackGoal {
        //TODO: add attack sequences and elemental effects
        public WeaponMeleeAttackGoal(EliteHilichurl hilichurl) {
            super(hilichurl, 1.0D, false);
        }

        protected void checkAndPerformAttack(LivingEntity tgt, double distance) {
            double d0 = this.getAttackReachSqr(tgt);
            if (distance <= d0 && this.getTicksUntilNextAttack() <= 0) {
                this.resetAttackCooldown();
                if (mob.level.random.nextInt(4) != 0) {
                    this.mob.stopUsingItem();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(tgt);
                }
            }
        }
    }
}