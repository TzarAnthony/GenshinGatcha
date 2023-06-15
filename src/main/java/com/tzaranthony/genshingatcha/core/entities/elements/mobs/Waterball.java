package com.tzaranthony.genshingatcha.core.entities.elements.mobs;

import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.AbstractArrowLikeElementalProjectile;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Waterball extends AbstractArrowLikeElementalProjectile implements ItemSupplier {
    protected boolean bounced = false;
    public Waterball(EntityType<? extends Waterball> entityType, Level level) {
        super(entityType, level);
    }

    public Waterball(LivingEntity owner, Level level) {
        super(GGEntities.WATERBALL.get(), owner, Element.E.HYDRO.getId(), level);
        this.setSoundEvent(SoundEvents.GENERIC_SPLASH);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("hasBounced", this.bounced);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.inGround = tag.getBoolean("hasBounced");
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround) {
            List<Entity> entities = this.level.getEntities(this, this.getBoundingBox(), this::canHitEntity);
            if (!entities.isEmpty()) {
                EntityHitResult entityhitresult = new EntityHitResult(entities.get(0));
                this.onHitEntity(entityhitresult);
            }
        }
    }

    protected float calculateDamage() {
        if (this.inGround) {
            return (float) this.getBaseDamage();
        }
        return super.calculateDamage();
    }


    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.bounced) {
            BlockState blockstate = this.level.getBlockState(result.getBlockPos());
            blockstate.onProjectileHit(this.level, blockstate, result, this);
            Vec3 vec3 = this.getDeltaMovement();
            if (vec3.y < 0.0D) {
                this.setDeltaMovement(vec3.x, -vec3.y, vec3.z);
            }
            this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.shakeTime = 7;
            this.setCritArrow(false);
            this.bounced = true;
        } else {
            super.onHitBlock(result);
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity tgt) {
        super.doPostHurtEffects(tgt);
        tgt.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));
    }

    protected void doPostHitEffects(BlockPos pos, Direction dir) {
    }

    public void setSoundEvent(SoundEvent p_36741_) {
        super.setSoundEvent(SoundEvents.GENERIC_SPLASH);
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.GENERIC_SPLASH;
    }

    @Override
    protected void fizzle() {
    }

    public ItemStack getItem() {
        return new ItemStack(Items.HEART_OF_THE_SEA);
    }
}