package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.character.Character;
import com.tzaranthony.genshingatcha.core.entities.mobs.hilichurls.ElementalMonster;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;

public class ElementalSlime extends Slime implements ElementalMonster {
    protected int element;

    public ElementalSlime(EntityType<? extends Slime> type, Level level) {
        super(type, level);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Element", this.element);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.element = tag.getInt("Element");
    }

    public void setElement(int element) {
        this.element = element;
    }

    public int getElement() {
        return this.element;
    }

    protected void setSize(int size, boolean setHealth) {
        super.setSize(size, setHealth);
        this.getAttribute(Attributes.ARMOR).setBaseValue((size * 3));
    }

    protected ResourceLocation getDefaultLootTable() {
        return this.isTiny() ? BuiltInLootTables.EMPTY : this.getType().getDefaultLootTable();
    }

    protected ParticleOptions getParticleType() {
        return ParticleTypes.ITEM_SLIME;
    }

    public boolean isOnFire() {
        if (this.element == Character.Element.PYRO.getId()) {
            return false;
        }
        return super.isOnFire();
    }

    public float getBrightness() {
        if (this.element == Character.Element.PYRO.getId() || element == Character.Element.ELECTRO.getId()) {
            return 1.0F;
        }
        return super.getBrightness();
    }

    protected void jumpInLiquid(TagKey<Fluid> fluidTag) {
        if (fluidTag == FluidTags.LAVA && this.element == Character.Element.PYRO.getId()) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x, (0.22F + (float)this.getSize() * 0.05F), vec3.z);
            this.hasImpulse = true;
        } else if (fluidTag == FluidTags.WATER && this.element == Character.Element.HYDRO.getId()) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x, (0.22F + (float)this.getSize() * 0.05F), vec3.z);
            this.hasImpulse = true;
        } else {
            super.jumpInLiquid(fluidTag);
        }
    }

    @Override
    protected void dealDamage(LivingEntity le) {
        super.dealDamage(le);
        if (this.level.random.nextInt(5) == 0) {
            if (this.element == Character.Element.CRYO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.CRYO.get(), 200));
                le.addEffect(new MobEffectInstance(GGEffects.FREEZING.get(), 200));
            } else if (this.element == Character.Element.PYRO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.PYRO.get(), 200));
                le.setSecondsOnFire(10);
            } else if (this.element == Character.Element.ELECTRO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.ELECTRO.get(), 200));
                le.hurt(DamageSource.LIGHTNING_BOLT, 4.0F);
            } else if (this.element == Character.Element.GEO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.GEO.get(), 200));
                le.hurt(DamageSource.FALLING_STALACTITE, 4.0F);
            } else if (this.element == Character.Element.HYDRO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.HYDRO.get(), 200));
                le.hurt(DamageSource.DROWN, 2.0F);
            } else if (this.element == Character.Element.DENDRO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.DENDRO.get(), 200));
                le.hurt(DamageSource.CACTUS, 2.0F);
            } else if (this.element == Character.Element.ANEMO.getId()) {
                le.addEffect(new MobEffectInstance(GGEffects.ANEMO.get(), 200));
                le.hurt(DamageSource.IN_WALL, 2.0F);
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