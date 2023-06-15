package com.tzaranthony.genshingatcha.core.entities.elements.projectiles;

import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public abstract class AbstractElementalProjectile extends AbstractHurtingProjectile {
    private static final EntityDataAccessor<Integer> ELEMENT_ID = SynchedEntityData.defineId(AbstractArrowLikeElementalProjectile.class, EntityDataSerializers.INT);
    protected int constRank = 0;
    protected boolean discardOnEntityHit = false;

    public AbstractElementalProjectile(EntityType<? extends AbstractElementalProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public AbstractElementalProjectile(EntityType<? extends AbstractElementalProjectile> entityType, LivingEntity owner, int element, Level level, double powX, double powY, double powZ) {
        super(entityType, owner, powX, powY, powZ, level);
        this.setElement(element);
    }

    protected void defineSynchedData() {
        this.entityData.define(ELEMENT_ID, 0);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setElement(tag.getInt("Element"));
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Element", this.getElement());
    }

    protected boolean shouldBurn() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    public boolean isAttackable() {
        return false;
    }

    public boolean hurt(DamageSource source, float p_37382_) {
        return false;
    }

    public float getBrightness() {
        return 0.0F;
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        Entity user = this.getOwner();
        if (target instanceof LivingEntity tgt) {
            if (!EntityUtil.isEntityImmuneToElement(tgt, this.getElement())) {
                tgt.hurt(GGDamageSource.indirectMagicElement(this, user, this.getElement()), 7.0F);
                tgt.addEffect(new MobEffectInstance(Element.ElementGetter.get(this.getElement()).getEffect(), 100));
                this.performOnEntity(tgt, user);
                if (user instanceof LivingEntity owner) {
                    this.doEnchantDamageEffects(owner, tgt);
                }
            }
            this.fizzle();
        }
    }

    protected void onHitBlock(BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        super.onHitBlock(result);
        this.performOnBlock(this.getOwner(), this.level, pos);
        if (!this.noPhysics) {
            this.fizzle();
        }
    }

    protected abstract void performOnEntity(LivingEntity target, Entity user);

    protected abstract void performOnBlock(Entity owner, Level level, BlockPos pos);

    protected void fizzle() {
        this.discard();
    }

    public void setElement(int id) {
        this.entityData.set(ELEMENT_ID, id);
    }

    public int getElement() {
        return this.entityData.get(ELEMENT_ID);
    }
}