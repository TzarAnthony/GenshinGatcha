package com.tzaranthony.genshingatcha.core.entities.elements;

import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.core.util.effects.FreezingEffectInstance;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class AreaFrostCloud extends FullParticleCloudEntity {
    protected boolean isUlt;

    public AreaFrostCloud(EntityType<? extends AreaFrostCloud> type, Level level) {
        super(type, level);
        this.setParticle(ParticleTypes.SNOWFLAKE);
        this.lifespan = 60;
        this.waitTime = 0;
        this.setRadius(4.0F);
    }

    public AreaFrostCloud(Level level, double x, double y, double z, float yRot, LivingEntity owner, int constRank, boolean isUlt) {
        this(GGEntities.FROST_CLOUD.get(), level);
        this.setPos(x, y, z);
        this.setYRot(yRot);
        this.owner = owner;
        this.constRank = constRank;
        this.isUlt = isUlt;
        if (this.isUlt) {
            this.setRadius(8.0F);
            this.lifespan = 20;
        }
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.isUlt = tag.getBoolean("IsUlt");
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsUlt", this.isUlt);
    }

    @Override
    protected void performOnEntity(LivingEntity le) {
        if (!this.isUlt) {
            if (EntityUtil.ignoreElementAttackEntity(le, this.owner)) {
                int amp = this.constRank >= 5 ? 1 : 0;
                if (!le.hasEffect(MobEffects.REGENERATION)) {
                    le.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, amp));
                }
            } else if (!EntityUtil.isEntityImmuneToElement(le, Element.E.CRYO.getId())) {
                int amp = this.constRank >= 5 ? 2 : 0;
                le.addEffect(new FreezingEffectInstance(60, amp));
                le.addEffect(new MobEffectInstance(GGEffects.CRYO.get(), 100));
            }
        }
    }

    @Override
    protected void performOnDiscard() {
        if (this.isUlt) {
            int amp = 0;
            if (this.constRank >= 3) {
                amp = 2;
            }

            for(LivingEntity le : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {
                if (EntityUtil.ignoreElementAttackEntity(le, this.owner)) {
                    le.heal(4.0F);
                    le.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, amp));
                    if (this.constRank >= 6 && le instanceof Player player && this.random.nextInt(8) == 0) {
                        player.addItem(new ItemStack(Items.TOTEM_OF_UNDYING));
                    }
                } else if (!EntityUtil.isEntityImmuneToElement(le, Element.E.CRYO.getId())) {
                    le.addEffect(new FreezingEffectInstance(100, amp));
                    le.addEffect(new MobEffectInstance(GGEffects.CRYO.get(), 200));
                    if (this.constRank >= 4) {
                        le.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 2));
                    }
                }
            }
        }
        this.discard();
    }
}