package com.tzaranthony.genshingatcha.core.entities.mobs.hilichurls;

import com.tzaranthony.genshingatcha.core.entities.projectiles.ElementalArrow;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.Map;

public class CrossbowHilichurl extends AbstractHilichurl implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Pillager.class, EntityDataSerializers.BOOLEAN);
    private static final float power = 1.6F;

    public CrossbowHilichurl(EntityType<? extends AbstractHilichurl> type, Level level) {
        super(type, level);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    public HilichurlArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return HilichurlArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem)) {
            return HilichurlArmPose.CROSSBOW_HOLD;
        } else {
            return this.isAggressive() ? HilichurlArmPose.ATTACKING : HilichurlArmPose.NEUTRAL;
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(accessor, difficulty, spawnType, groupData, tag);
        this.populateDefaultEquipmentSlots(difficulty);
        this.populateDefaultEquipmentEnchantments(difficulty);
        return spawngroupdata;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
    }

    protected void enchantSpawnedWeapon(float diffId) {
        super.enchantSpawnedWeapon(diffId);
        Difficulty diff = this.level.getDifficulty();
        ItemStack itemstack = this.getMainHandItem();
        if (itemstack.is(Items.CROSSBOW)) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack);
            if (this.random.nextInt(50) == 0) {
                map.putIfAbsent(Enchantments.MULTISHOT, 1);
            }
            if (this.random.nextInt(100) == 0) {
                if (diff == Difficulty.HARD) {
                    map.putIfAbsent(Enchantments.QUICK_CHARGE, 3);
                } else if (diff == Difficulty.NORMAL) {
                    map.putIfAbsent(Enchantments.QUICK_CHARGE, 2);
                } else {
                    map.putIfAbsent(Enchantments.QUICK_CHARGE, 1);
                }
            }
            if (this.random.nextInt(300) == 0) {
                if (diff == Difficulty.HARD) {
                    map.putIfAbsent(Enchantments.PIERCING, 4);
                } else if (diff == Difficulty.NORMAL) {
                    map.putIfAbsent(Enchantments.PIERCING, 2);
                } else {
                    map.putIfAbsent(Enchantments.PIERCING, 1);
                }
            }
            EnchantmentHelper.setEnchantments(map, itemstack);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
        }
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem weaponItem) {
        return weaponItem == Items.CROSSBOW;
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean isCharging) {
        this.entityData.set(IS_CHARGING_CROSSBOW, isCharging);
    }

    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity tgt, ItemStack bowStack, Projectile prj, float offset) {
        ElementalArrow ea = new ElementalArrow(this, this.element, this.level);
        this.shootCrossbowProjectile(this, tgt, ea, offset, power);
    }

    @Override
    public void performRangedAttack(LivingEntity tgt, float mod) {
        this.performCrossbowAttack(this, power);
    }
}