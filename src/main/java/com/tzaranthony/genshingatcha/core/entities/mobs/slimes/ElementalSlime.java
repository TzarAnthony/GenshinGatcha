package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.entities.mobs.ElementalGroupData;
import com.tzaranthony.genshingatcha.core.entities.mobs.ElementalMob;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityElementDamageSource;
import com.tzaranthony.genshingatcha.core.util.GGDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ElementalSlime extends Slime implements ElementalMob {
    private static final EntityDataAccessor<Integer> ELEMENT = SynchedEntityData.defineId(ElementalSlime.class, EntityDataSerializers.INT);

    public ElementalSlime(EntityType<? extends ElementalSlime> type, Level level) {
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

    protected void setSize(int size, boolean setHealth) {
        super.setSize(size, setHealth);
        this.getAttribute(Attributes.ARMOR).setBaseValue((size * 3));
    }

    public EntityType<? extends ElementalSlime> getType() {
        return (EntityType<? extends ElementalSlime>) super.getType();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        groupData = super.finalizeSpawn(accessor, difficulty, spawnType, groupData, tag);
        int eSetter = getElementFromBiome(accessor.getBiome(this.blockPosition()), this.random);
        this.setElement(eSetter);
        groupData = new ElementalGroupData(eSetter);
        return groupData;
    }

    protected ResourceLocation getDefaultLootTable() {
        return this.isTiny() ? BuiltInLootTables.EMPTY : this.getType().getDefaultLootTable();
    }

    protected ParticleOptions getParticleType() {
        switch (this.getElement()) {
            case 0:
                return ParticleTypes.ITEM_SNOWBALL;
            case 1:
                return ParticleTypes.FLAME;
            case 2:
                return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.CRYING_OBSIDIAN));
            case 3:
                return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.TUFF));
            case 4:
                return ParticleTypes.SPLASH;
            case 5:
                return ParticleTypes.SPORE_BLOSSOM_AIR;
            default:
                return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.CYAN_WOOL));
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source == Element.ElementGetter.get(this.getElement()).getDamage() || (source instanceof EntityElementDamageSource eds && eds.getElement() == this.getElement()) || super.isInvulnerableTo(source);
    }

    public boolean canFreeze() {
        return this.getElement() != Element.E.CRYO.getId();
    }

    public boolean isOnFire() {
        if (this.getElement() == Element.E.PYRO.getId()) {
            return false;
        }
        return super.isOnFire();
    }

    public float getBrightness() {
        if (this.getElement() == Element.E.PYRO.getId() || this.getElement() == Element.E.ELECTRO.getId()) {
            return 1.0F;
        }
        return super.getBrightness();
    }

    protected void jumpInLiquid(TagKey<Fluid> fluidTag) {
        if (fluidTag == FluidTags.LAVA && this.getElement() == Element.E.PYRO.getId()) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x, (0.22F + (float)this.getSize() * 0.05F), vec3.z);
            this.hasImpulse = true;
        } else if (fluidTag == FluidTags.WATER && this.getElement() == Element.E.HYDRO.getId()) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x, (0.22F + (float)this.getSize() * 0.05F), vec3.z);
            this.hasImpulse = true;
        } else {
            super.jumpInLiquid(fluidTag);
        }
    }

    @Override
    protected void onChangedBlock(BlockPos pos) {
        if (this.getElement() == Element.E.CRYO.getId()) {
            FrostWalkerEnchantment.onEntityMoved(this, this.level, pos, 1);
        }
        //pyro slimes add fire?
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
                ElementalSlime slime = this.getType().create(this.level);
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
        if (this.isAlive()) {
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

    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }
}