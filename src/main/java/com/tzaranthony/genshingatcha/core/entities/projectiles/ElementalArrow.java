package com.tzaranthony.genshingatcha.core.entities.projectiles;

import com.google.common.collect.Lists;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.GGDamageSource;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ElementalArrow extends AbstractArrow {
    private static final EntityDataAccessor<Integer> ELEMENT_ID = SynchedEntityData.defineId(ElementalArrow.class, EntityDataSerializers.INT);
    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;
    @Nullable
    private List<Entity> piercedAndKilledEntities;

    public ElementalArrow(EntityType<? extends ElementalArrow> entityType, Level level) {
        super(entityType, level);
    }

    public ElementalArrow(LivingEntity owner, Element.E element, Level level) {
        this(owner, element.getId(), level);
    }

    public ElementalArrow(LivingEntity owner, int element, Level level) {
        super(GGEntities.ELEMENTAL_ARROW.get(), owner, level);
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
                    this.level.addParticle(Element.ElementGetter.get(this.getElement()).getParticle(), this.getX() + d5 * (double)i / 4.0D, this.getY() + d6 * (double)i / 4.0D, this.getZ() + d1 * (double)i / 4.0D, -d5, -d6 + 0.2D, -d1);
                }
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity le) {
            float f = (float)this.getDeltaMovement().length();
            int i = Mth.ceil(Mth.clamp((double)f * this.getBaseDamage(), 0.0D, 2.147483647E9D));
            if (this.getPierceLevel() > 0) {
                if (this.piercingIgnoreEntityIds == null) {
                    this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
                }
                if (this.piercedAndKilledEntities == null) {
                    this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
                }
                if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
                    this.discard();
                    return;
                }
                this.piercingIgnoreEntityIds.add(le.getId());
            }

            if (this.isCritArrow()) {
                long j = this.random.nextInt(i / 2 + 2);
                i = (int)Math.min(j + (long)i, 2147483647L);
            }

            Entity entity1 = this.getOwner();
            DamageSource damagesource;
            if (entity1 == null) {
                damagesource = GGDamageSource.arrowElement(this, this, this.getElement());
            } else {
                damagesource = GGDamageSource.arrowElement(this, entity1, this.getElement());
                if (entity1 instanceof LivingEntity) {
                    ((LivingEntity)entity1).setLastHurtMob(le);
                }
            }

            boolean flag = le.getType() == EntityType.ENDERMAN;
            int k = le.getRemainingFireTicks();
            if (this.isOnFire() && !flag) {
                le.setSecondsOnFire(5);
            }

            if (le.hurt(damagesource, (float)i)) {
                if (flag) {
                    return;
                }

                if (!this.level.isClientSide && this.getPierceLevel() <= 0) {
                    le.setArrowCount(le.getArrowCount() + 1);
                }

                if (this.getKnockback() > 0) {
                    Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.getKnockback() * 0.6D);
                    if (vec3.lengthSqr() > 0.0D) {
                        le.push(vec3.x, 0.1D, vec3.z);
                    }
                }

                if (!this.level.isClientSide && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(le, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, le);
                }

                this.doPostHurtEffects(le);
                if (entity1 != null && le != entity1 && le instanceof Player && entity1 instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }

                if (!le.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(le);
                }

                if (!this.level.isClientSide && entity1 instanceof ServerPlayer) {
                    ServerPlayer serverplayer = (ServerPlayer)entity1;
                    if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, this.piercedAndKilledEntities);
                    } else if (!le.isAlive() && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, Arrays.asList(le));
                    }
                }

                this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                if (this.getPierceLevel() <= 0) {
                    this.discard();
                }
            } else {
                le.setRemainingFireTicks(k);
                this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
                this.setYRot(this.getYRot() + 180.0F);
                this.yRotO += 180.0F;
                if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                    if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    }
                    this.discard();
                }
            }
        } else {
            super.onHitEntity(result);
        }
        doPostHitEffects(result.getEntity().getOnPos(), result.getEntity().getDirection());
        this.discard();
    }

    protected boolean canHitEntity(Entity p_36743_) {
        return super.canHitEntity(p_36743_) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(p_36743_.getId()));
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
        areaeffectcloud.addEffect(new MobEffectInstance(Element.ElementGetter.get(this.getElement()).getEffect(), 100));
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