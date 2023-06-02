package com.tzaranthony.genshingatcha.core.entities.projectiles;

import com.tzaranthony.genshingatcha.core.character.Character;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ElementalArrow extends AbstractArrow {
    private static final EntityDataAccessor<Integer> ELEMENT_ID = SynchedEntityData.defineId(ElementalArrow.class, EntityDataSerializers.INT);

    public ElementalArrow(EntityType<? extends ElementalArrow> entityType, Level level) {
        super(entityType, level);
    }

    public ElementalArrow(LivingEntity owner, Character.Element element, Level level) {
        this(owner, element.getId(), level);
    }

    public ElementalArrow(LivingEntity owner, int element, Level level) {
        super(GGEntities.ELECTRO_ARROW.get(), owner, level);
        this.setElement(element);
        this.pickup = AbstractArrow.Pickup.DISALLOWED;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
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

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            Vec3 vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            if (this.isCritArrow()) {
                for(int i = 0; i < 4; ++i) {
                    this.level.addParticle(getParticleForElement(), this.getX() + d5 * (double)i / 4.0D, this.getY() + d6 * (double)i / 4.0D, this.getZ() + d1 * (double)i / 4.0D, -d5, -d6 + 0.2D, -d1);
                }
            }
        }
    }

    private ParticleOptions getParticleForElement() {
        switch (this.getElement()) {
            case 0:
                return ParticleTypes.SNOWFLAKE;
            case 1:
                return ParticleTypes.LAVA;
            case 2:
                return ParticleTypes.ELECTRIC_SPARK;
            case 3:
                return ParticleTypes.MYCELIUM;
            case 4:
                return ParticleTypes.BUBBLE;
            case 5:
                return ParticleTypes.COMPOSTER;
            default:
                return ParticleTypes.SPIT;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        doPostHitEffects(result.getEntity().getOnPos(), result.getEntity().getDirection());
        this.discard();
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        doPostHitEffects(result.getBlockPos(), result.getDirection());
        this.discard();
    }

    private void doPostHitEffects(BlockPos pos, Direction dir) {
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaeffectcloud.setOwner((LivingEntity)entity);
        }
        areaeffectcloud.setRadius(3.5F);
        areaeffectcloud.setRadiusOnUse(-0.5F);
        areaeffectcloud.setWaitTime(5);
        areaeffectcloud.setDuration(40);
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
        areaeffectcloud.addEffect(getElementalEffect());
        if (this.getElement() == 1) {
            createFire(pos, dir, (Player) entity);
        }
        this.level.addFreshEntity(areaeffectcloud);
    }

    protected void createFire(BlockPos pos, Direction dir, Player player) {
        BlockState state = this.level.getBlockState(pos);

        if (!CampfireBlock.canLight(state) && !CandleBlock.canLight(state) && !CandleCakeBlock.canLight(state)) {
            BlockPos blockpos1 = pos.relative(dir);
            if (BaseFireBlock.canBePlacedAt(level, blockpos1, dir)) {
                level.playSound(player, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState blockstate1 = BaseFireBlock.getState(level, blockpos1);
                level.setBlock(blockpos1, blockstate1, 11);
                level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
            }
        } else {
            level.playSound(player, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
        }
    }

    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
        target.addEffect(getElementalEffect(), this.getEffectSource());
    }

    protected MobEffectInstance getElementalEffect() {
        switch (this.getElement()) {
            case 0:
                return new MobEffectInstance(GGEffects.CRYO.get(), 100);
            case 1:
                return new MobEffectInstance(GGEffects.PYRO.get(), 100);
            case 2:
                return new MobEffectInstance(GGEffects.ELECTRO.get(), 100);
            case 3:
                return new MobEffectInstance(GGEffects.GEO.get(), 100);
            case 4:
                return new MobEffectInstance(GGEffects.HYDRO.get(), 100);
            case 5:
                return new MobEffectInstance(GGEffects.DENDRO.get(), 100);
            default:
                return new MobEffectInstance(GGEffects.ANEMO.get(), 100);
        }
    }

    public void setElement(int id) {
        this.entityData.set(ELEMENT_ID, id);
    }

    public int getElement() {
        return this.entityData.get(ELEMENT_ID);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
}