package com.tzaranthony.genshingatcha.core.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class IndirectEntityElementDamageSource extends EntityElementDamageSource {
    @Nullable
    private final Entity owner;

    public IndirectEntityElementDamageSource(String name, Entity source, @Nullable Entity owner, int elementId) {
        super(name, source, elementId);
        this.owner = owner;
    }

    @Nullable
    public Entity getDirectEntity() {
        return this.entity;
    }

    @Nullable
    public Entity getEntity() {
        return this.owner;
    }

    public Component getLocalizedDeathMessage(LivingEntity p_19410_) {
        Component component = this.owner == null ? this.entity.getDisplayName() : this.owner.getDisplayName();
        ItemStack itemstack = this.owner instanceof LivingEntity ? ((LivingEntity)this.owner).getMainHandItem() : ItemStack.EMPTY;
        String s = "death.attack." + Element.ElementGetter.get(this.element).getDamage().getMsgId() + "." + this.msgId;
        String s1 = s + ".item";
        return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? new TranslatableComponent(s1, p_19410_.getDisplayName(), component, itemstack.getDisplayName()) : new TranslatableComponent(s, p_19410_.getDisplayName(), component);
    }
}