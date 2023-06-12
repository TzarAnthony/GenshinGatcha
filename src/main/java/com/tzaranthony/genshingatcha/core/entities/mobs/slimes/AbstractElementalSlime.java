package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.entities.mobs.ElementalMob;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityElementDamageSource;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.core.util.GGDamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public abstract class AbstractElementalSlime extends Slime implements ElementalMob {
    private static final EntityDataAccessor<Integer> ELEMENT = SynchedEntityData.defineId(AbstractElementalSlime.class, EntityDataSerializers.INT);

    public AbstractElementalSlime(EntityType<? extends AbstractElementalSlime> type, Level level) {
        super(type, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ELEMENT, 1);
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
        //TODO: add extra attacks for max size slimes
        if (this.isAlive() && !EntityUtil.isEntityImmuneToElement(le, this.getElement())) {
            int i = this.getSize();
            if (this.distanceToSqr(le) < 0.6D * (double)i * 0.6D * (double)i && this.hasLineOfSight(le) && le.hurt(GGDamageSource.mobElementAttack(this, this.getElement()), this.getAttackDamage())) {
                le.addEffect(new MobEffectInstance(Element.ElementGetter.get(this.getElement()).getEffect(), 100));
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.doEnchantDamageEffects(this, le);
            }
        }
    }

    public boolean causeFallDamage(float p_149717_, float p_149718_, DamageSource p_149719_) {
        return false;
    }

    protected float getAttackDamage() {
        return super.getAttackDamage() + 4.0F;
    }

    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }
}