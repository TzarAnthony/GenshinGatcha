package com.tzaranthony.genshingatcha.core.entities.mobs.hilichurls;

import com.tzaranthony.genshingatcha.core.character.Character;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import com.tzaranthony.genshingatcha.registries.GGItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class MeleeHilichurl extends AbstractHilichurl {
    protected int cannotUseItemRemaining;

    public MeleeHilichurl(EntityType<? extends AbstractHilichurl> type, Level level) {
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

    @Override
    public void stopUsingItem() {
        super.stopUsingItem();
        this.cannotUseItemRemaining = 100;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (target instanceof LivingEntity le && this.level.random.nextInt(6) == 0) {
            if (this.element == Character.Element.CRYO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.CRYO.get(), 100));
                le.addEffect(new MobEffectInstance(GGEffects.FREEZING.get(), 100));
            } else if (this.element == Character.Element.PYRO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.PYRO.get(), 100));
                le.setSecondsOnFire(5);
            } else if (this.element == Character.Element.ELECTRO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.ELECTRO.get(), 100));
                le.hurt(DamageSource.LIGHTNING_BOLT, 2.0F);
            } else if (this.element == Character.Element.GEO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.GEO.get(), 100));
                le.hurt(DamageSource.FALLING_STALACTITE, 2.0F);
            } else if (this.element == Character.Element.HYDRO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.HYDRO.get(), 200));
                le.hurt(DamageSource.DROWN, 2.0F);
            } else if (this.element == Character.Element.DENDRO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.DENDRO.get(), 200));
                le.hurt(DamageSource.CACTUS, 2.0F);
            } else if (this.element == Character.Element.ANEMO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.ANEMO.get(), 200));
                le.hurt(DamageSource.IN_WALL, 2.0F);
            }
        }
        return super.doHurtTarget(target);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        ItemStack stack;
        switch (this.level.random.nextInt(6)) {
            case 0:
                stack = new ItemStack(GGItems.SWORD_ONE.get());
                break;
            case 1:
                stack = new ItemStack(GGItems.SWORD_TWO.get());
                break;
            case 2:
                stack = new ItemStack(GGItems.SWORD_THREE.get());
                break;
            case 3:
                stack = new ItemStack(Items.IRON_SWORD);
                break;
            default:
                stack = new ItemStack(Items.IRON_AXE);
                break;
        }
        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
    }

    class WeaponMeleeAttackGoal extends MeleeAttackGoal {
        public WeaponMeleeAttackGoal(MeleeHilichurl hilichurl) {
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