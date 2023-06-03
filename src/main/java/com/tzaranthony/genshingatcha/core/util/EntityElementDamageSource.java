package com.tzaranthony.genshingatcha.core.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class EntityElementDamageSource extends DamageSource {
    protected final Entity entity;
    protected int element;

    public EntityElementDamageSource(String name, Entity source, int element) {
        super(name);
        this.entity = source;
        this.element = element;
    }

    public int getElement() {
        return this.element;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Component getLocalizedDeathMessage(LivingEntity tgt) {
        ItemStack itemstack = this.entity instanceof LivingEntity ? ((LivingEntity)this.entity).getMainHandItem() : ItemStack.EMPTY;
        String s = "death.attack." + Element.ElementGetter.get(this.element).getDamage().getMsgId() + "." + this.msgId;
        return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? new TranslatableComponent(s + ".item", tgt.getDisplayName(), this.entity.getDisplayName(), itemstack.getDisplayName()) : new TranslatableComponent(s, tgt.getDisplayName(), this.entity.getDisplayName());
    }

    public boolean scalesWithDifficulty() {
        return this.entity instanceof LivingEntity && !(this.entity instanceof Player);
    }

    @Nullable
    public Vec3 getSourcePosition() {
        return this.entity.position();
    }

    public String toString() {
        return "EntityElementDamageSource (" + this.entity + ")";
    }
}