package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.entities.mobs.ElementalEntity;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.core.util.damage.EntityElementDamageSource;
import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import com.tzaranthony.genshingatcha.core.util.effects.ElementEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class AbstractElementalSlime extends Slime implements ElementalEntity {
    private static final EntityDataAccessor<Integer> ELEMENT = SynchedEntityData.defineId(AbstractElementalSlime.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(AbstractElementalSlime.class, EntityDataSerializers.BOOLEAN);
    private int bigSlimeAttackCooldown;

    public AbstractElementalSlime(EntityType<? extends AbstractElementalSlime> type, Level level) {
        super(type, level);
        this.resetBigSlimeAttackCooldown();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AbstractElementalSlime.BigSlimeSpecialAttack(this));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ELEMENT, 1);
        this.entityData.define(CHARGING, false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Element", this.getElement());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setElement(tag.getInt("Element"));
    }

    public void setElement(int elementId) {
        this.entityData.set(ELEMENT, elementId);
    }

    public int getElement() {
        return this.entityData.get(ELEMENT);
    }

    public boolean isCharging() {
        return this.entityData.get(CHARGING);
    }

    public void setCharging(boolean p_32759_) {
        this.entityData.set(CHARGING, p_32759_);
    }

    public void tick() {
        if (this.isBig()) {
            this.bigSlimeAttackCooldown = Math.max(--this.bigSlimeAttackCooldown, 0);
        }
        if (this.tickCount % 40 == 0 && !this.hasEffect(Element.ElementGetter.get(this.getElement()).getEffect())) {
            this.reapplyElement();
        }
        super.tick();
    }

    protected void reapplyElement() {
        this.addEffect(new ElementEffectInstance(this.getElement(), Integer.MAX_VALUE));
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        data = super.finalizeSpawn(accessor, difficulty, type, data, tag);
        this.reapplyElement();
        return data;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        if (effect.getEffect() == Element.ElementGetter.get(this.getElement()).getEffect()) {
            return false;
        }
        return super.canBeAffected(effect);
    }

    protected boolean isBig() {
        return this.getSize() >= 3;
    }

    public boolean checkBigSlimeAttackCooldown() {
        return this.bigSlimeAttackCooldown <= 0;
    };

    public void resetBigSlimeAttackCooldown() {
        this.bigSlimeAttackCooldown = 300;
    };

    protected void setSize(int size, boolean resetHealth) {
        super.setSize(size, resetHealth);
        this.getAttribute(Attributes.ARMOR).setBaseValue((size * 5));
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double) (size * size) + 8.0D);
        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }
    }

    public EntityType<? extends AbstractElementalSlime> getType() {
        return (EntityType<? extends AbstractElementalSlime>) super.getType();
    }

    protected ResourceLocation getDefaultLootTable() {
        return this.isTiny() ? BuiltInLootTables.EMPTY : this.getType().getDefaultLootTable();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source == Element.ElementGetter.get(this.getElement()).getDamage() || (source instanceof EntityElementDamageSource eds && eds.getElement() == this.getElement()) || super.isInvulnerableTo(source);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        int i = this.getSize();
        if (!this.level.isClientSide && i > 1 && this.isDeadOrDying()) {
            Component component = this.getCustomName();
            boolean flag = this.isNoAi();
            float f = (float)i / 4.0F;
            int j = i / 2;
            int k = 2 + this.random.nextInt(3);

            for(int l = 0; l < k; ++l) {
                float f1 = ((float)(l % 2) - 0.5F) * f;
                float f2 = ((float)(l / 2) - 0.5F) * f;
                AbstractElementalSlime slime = this.getType().create(this.level);
                if (this.isPersistenceRequired()) {
                    slime.setPersistenceRequired();
                }

                slime.setElement(this.getElement());
                slime.setCustomName(component);
                slime.setNoAi(flag);
                slime.setInvulnerable(this.isInvulnerable());
                slime.setSize(j, true);
                slime.moveTo(this.getX() + (double)f1, this.getY() + 0.5D, this.getZ() + (double)f2, this.random.nextFloat() * 360.0F, 0.0F);
                this.level.addFreshEntity(slime);
            }
        }
        this.setRemoved(reason);
        if (reason == Entity.RemovalReason.KILLED) {
            this.gameEvent(GameEvent.ENTITY_KILLED);
        }
        this.invalidateCaps();
    }

    @Override
    protected void dealDamage(LivingEntity le) {
        if (this.isAlive() && !EntityUtil.isEntityImmuneToElement(le, this.getElement())) {
            int i = this.getSize();
            if (this.distanceToSqr(le) < 0.6D * (double)i * 0.6D * (double)i && this.hasLineOfSight(le) && le.hurt(GGDamageSource.mobElementAttack(this, this.getElement()), this.getAttackDamage())) {
                le.addEffect(new ElementEffectInstance(this.getElement()));
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.doEnchantDamageEffects(this, le);
            }
        }
    }

    protected float getAttackDamage() {
        return super.getAttackDamage() + 6.0F;
    }

    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }

    public boolean isShaking() {
        return false;
    }

    protected void startCharge() {
    }

    protected abstract boolean slimeChargeActivity(int chargeTime, LivingEntity tgt);

    static class BigSlimeSpecialAttack extends Goal {
        private final AbstractElementalSlime slime;
        public int chargeTime;

        public BigSlimeSpecialAttack(AbstractElementalSlime p_33648_) {
            this.slime = p_33648_;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity == null) {
                return false;
            } else {
                return this.slime.isBig() && this.slime.canAttack(livingentity) && this.slime.checkBigSlimeAttackCooldown();
            }
        }

        public void start() {
            this.chargeTime = 0;
            this.slime.startCharge();
            super.start();
        }

        public void stop() {
            this.slime.setCharging(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity != null) {
                if (livingentity.distanceToSqr(this.slime) < 4096.0D && this.slime.hasLineOfSight(livingentity)) {
                    ++this.chargeTime;
                    this.slime.setCharging(true);
                    this.slime.lookAt(livingentity, 10.0F, 10.0F);
                    boolean isFinal = this.slime.slimeChargeActivity(this.chargeTime, livingentity);
                    if (isFinal) {
                        this.chargeTime = 0;
                        this.slime.resetBigSlimeAttackCooldown();
                    }
                } else if (this.chargeTime > 0) {
                    --this.chargeTime;
                }
            }
        }
    }
}