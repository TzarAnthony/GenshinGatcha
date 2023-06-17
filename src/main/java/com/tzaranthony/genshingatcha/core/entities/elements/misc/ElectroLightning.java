package com.tzaranthony.genshingatcha.core.entities.elements.misc;

import com.google.common.collect.Sets;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import com.tzaranthony.genshingatcha.core.util.effects.ElementEffectInstance;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ElectroLightning extends LightningBolt {
    private int life;
    public long seed;
    private final Set<Entity> hitEntities = Sets.newHashSet();
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public ElectroLightning(EntityType<? extends LightningBolt> bolt, Level level) {
        super(bolt, level);
        this.noCulling = true;
        this.life = 2;
        this.seed = this.random.nextLong();
        this.setVisualOnly(true);
        this.setDamage(3.0F);
    }

    public void tick() {
        super.tick();
        if (this.life >= 0 &&!(this.level.isClientSide)) {
            List<Entity> list1 = this.level.getEntities(this, new AABB(this.getX() - 3.0D, this.getY() - 3.0D, this.getZ() - 3.0D, this.getX() + 3.0D, this.getY() + 6.0D + 3.0D, this.getZ() + 3.0D), Entity::isAlive);

            for(Entity entity : list1) {
                if (entity != this.owner && !net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(entity, this)) {
                    entity.thunderHit((ServerLevel)this.level, this);
                    if (this.owner != null) {
                        entity.hurt(GGDamageSource.indirectMagicElement(this, this.getOwner(), Element.E.ELECTRO.getId()), this.getDamage());
                    } else {
                        entity.hurt(GGDamageSource.ELECTRO, this.getDamage());
                    }
                    if (entity instanceof LivingEntity le) {
                        le.addEffect(new ElementEffectInstance(Element.E.ELECTRO.getEffect()));
                    }
                }
            }

            this.hitEntities.addAll(list1);
            if (this.getCause() != null) {
                CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.getCause(), list1);
            }
        }
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }
        return this.owner;
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }
}