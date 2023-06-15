package com.tzaranthony.genshingatcha.core.entities.elements.projectiles;

import com.google.common.collect.Lists;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
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
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrowLikeElementalProjectile extends AbstractArrow {
    private static final EntityDataAccessor<Integer> ELEMENT_ID = SynchedEntityData.defineId(AbstractArrowLikeElementalProjectile.class, EntityDataSerializers.INT);
    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;
    @Nullable
    private List<Entity> piercedAndKilledEntities;

    public AbstractArrowLikeElementalProjectile(EntityType<? extends AbstractArrowLikeElementalProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public AbstractArrowLikeElementalProjectile(EntityType<? extends AbstractArrowLikeElementalProjectile> entityType, LivingEntity owner, int element, Level level) {
        super(entityType, owner, level);
        this.setElement(element);
        this.pickup = Pickup.DISALLOWED;
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

    protected void handlePiercingLivingEntities(LivingEntity tgt) {
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
            this.piercingIgnoreEntityIds.add(tgt.getId());
        }
    }

    protected DamageSource makeDamageSource(Entity owner, LivingEntity tgt) {
        if (owner == null) {
            return GGDamageSource.arrowElement(this, this, this.getElement());
        } else {
            if (owner instanceof LivingEntity) {
                ((LivingEntity)owner).setLastHurtMob(tgt);
            }
            return GGDamageSource.arrowElement(this, owner, this.getElement());
        }
    }

    protected float calculateDamage() {
        float f = (float)this.getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double)f * this.getBaseDamage(), 0.0D, 2.147483647E9D));
        if (this.isCritArrow()) {
            long j = this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(j + (long)i, 2147483647L);
        }
        return (float) i;
    }

    protected void handleHurtEntity(Entity owner, LivingEntity tgt) {
        if (this.getKnockback() > 0) {
            Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.getKnockback() * 0.6D);
            if (vec3.lengthSqr() > 0.0D) {
                tgt.push(vec3.x, 0.1D, vec3.z);
            }
        }

        if (!this.level.isClientSide && owner instanceof LivingEntity) {
            EnchantmentHelper.doPostHurtEffects(tgt, owner);
            EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, tgt);
        }

        this.doPostHurtEffects(tgt);
        if (owner != null && tgt != owner && tgt instanceof Player && owner instanceof ServerPlayer && !this.isSilent()) {
            ((ServerPlayer)owner).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
        }

        if (!tgt.isAlive() && this.piercedAndKilledEntities != null) {
            this.piercedAndKilledEntities.add(tgt);
        }
    }

    protected void checkAdvancements(Entity owner, LivingEntity tgt) {
        if (!this.level.isClientSide && owner instanceof ServerPlayer) {
            ServerPlayer serverplayer = (ServerPlayer)owner;
            if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, this.piercedAndKilledEntities);
            } else if (!tgt.isAlive() && this.shotFromCrossbow()) {
                CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, Arrays.asList(tgt));
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity tgt) {
            handlePiercingLivingEntities(tgt);

            Entity owner = this.getOwner();
            DamageSource damagesource = makeDamageSource(owner, tgt);

            boolean flag = tgt.getType() == EntityType.ENDERMAN || EntityUtil.isEntityImmuneToElement(tgt, this.getElement());
            int k = tgt.getRemainingFireTicks();
            if (!flag && tgt.hurt(damagesource, calculateDamage())) {
                if ((this.isOnFire() || this.getElement() == Element.E.PYRO.getId())) {
                    tgt.setSecondsOnFire(5);
                }

                handleHurtEntity(owner, tgt);
                checkAdvancements(owner, tgt);

                this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                if (this.getPierceLevel() <= 0) {
                    this.discard();
                }
            } else {
                tgt.setRemainingFireTicks(k);
                this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
                this.setYRot(this.getYRot() + 180.0F);
                this.yRotO += 180.0F;
                if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                    if (this.pickup == Pickup.ALLOWED) {
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    }
                    this.discard();
                }
            }
        } else {
            super.onHitEntity(result);
        }
        doPostHitEffects(result.getEntity().getOnPos(), result.getEntity().getDirection());
        if (!this.level.isClientSide && this.getElement() == Element.E.PYRO.getId()) {
            createFireAtPos(result.getEntity().getOnPos());
        }
        this.fizzle();
    }

    protected boolean canHitEntity(Entity p_36743_) {
        return super.canHitEntity(p_36743_) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(p_36743_.getId()));
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        doPostHitEffects(result.getBlockPos(), result.getDirection());
        if (!this.level.isClientSide && this.getElement() == Element.E.PYRO.getId()) {
            createFireAtPos(result.getBlockPos().relative(result.getDirection()));
        }
        this.fizzle();
    }

    protected void doPostHurtEffects(LivingEntity tgt) {
        tgt.addEffect(new MobEffectInstance(Element.ElementGetter.get(this.getElement()).getEffect(), 100));
        super.doPostHurtEffects(tgt);
    }

    protected abstract void doPostHitEffects(BlockPos pos, Direction dir);

    protected void createFireAtPos(BlockPos pos) {
        if (this.level.isEmptyBlock(pos)) {
            this.level.setBlockAndUpdate(pos, BaseFireBlock.getState(this.level, pos));
        }
    }

    protected void fizzle() {
        this.discard();
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