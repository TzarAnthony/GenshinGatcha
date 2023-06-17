package com.tzaranthony.genshingatcha.core.entities.mobs.hilichurls;

import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import com.tzaranthony.genshingatcha.core.util.effects.ElementEffectInstance;
import com.tzaranthony.genshingatcha.registries.GGItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
        if (target instanceof LivingEntity tgt) {
            float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float f1 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), tgt.getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(this);

            int i = EnchantmentHelper.getFireAspect(this);
            if (i > 0) {
                tgt.setSecondsOnFire(i * 4);
            }

            tgt.addEffect(new ElementEffectInstance(this.getElement()));
            boolean flag = tgt.hurt(GGDamageSource.mobElementAttack(this, this.getElement()), f);
            if (flag) {
                if (f1 > 0.0F) {
                    tgt.knockback((f1 * 0.5F), Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }

                if (tgt instanceof Player) {
                    Player player = (Player)tgt;
                    this.maybeDisableShield(player, this.getMainHandItem(), player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY);
                }

                this.doEnchantDamageEffects(this, tgt);
                this.setLastHurtMob(tgt);
            }
            return flag;
        }
        return super.doHurtTarget(target);
    }

    private void maybeDisableShield(Player player, ItemStack weaponStack, ItemStack shieldStack) {
        if (!weaponStack.isEmpty() && !shieldStack.isEmpty() && weaponStack.getItem() instanceof AxeItem && shieldStack.is(Items.SHIELD)) {
            float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.random.nextFloat() < f) {
                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.level.broadcastEntityEvent(player, (byte)30);
            }
        }
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