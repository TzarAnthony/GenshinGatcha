package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.entities.mobs.hilichurls.ElementalMonster;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityElementDamageSource;
import com.tzaranthony.genshingatcha.core.util.GGDamageSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
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

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source == Element.ElementGetter.get(this.element).getDamage() || (source instanceof EntityElementDamageSource eds && eds.getElement() == this.element) || super.isInvulnerableTo(source);
    }

    public boolean isOnFire() {
        if (this.element == Element.E.PYRO.getId()) {
            return false;
        }
        return super.isOnFire();
    }

    public float getBrightness() {
        if (this.element == Element.E.PYRO.getId() || element == Element.E.ELECTRO.getId()) {
            return 1.0F;
        }
        return super.getBrightness();
    }

    protected void jumpInLiquid(TagKey<Fluid> fluidTag) {
        if (fluidTag == FluidTags.LAVA && this.element == Element.E.PYRO.getId()) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x, (0.22F + (float)this.getSize() * 0.05F), vec3.z);
            this.hasImpulse = true;
        } else if (fluidTag == FluidTags.WATER && this.element == Element.E.HYDRO.getId()) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x, (0.22F + (float)this.getSize() * 0.05F), vec3.z);
            this.hasImpulse = true;
        } else {
            super.jumpInLiquid(fluidTag);
        }
    }

    @Override
    protected void dealDamage(LivingEntity le) {
        if (this.isAlive()) {
            int i = this.getSize();
            if (this.distanceToSqr(le) < 0.6D * (double)i * 0.6D * (double)i && this.hasLineOfSight(le) && le.hurt(GGDamageSource.mobElementAttack(this, this.element), this.getAttackDamage())) {
                le.addEffect(new MobEffectInstance(Element.ElementGetter.get(this.element).getEffect(), 100));
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