package com.tzaranthony.genshingatcha.core.util.damage;

import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class IndirectEntityElementMagicDamageSource extends EntityElementDamageSource {
    @Nullable
    private final Entity owner;

    public IndirectEntityElementMagicDamageSource(String name, Entity source, @Nullable Entity owner, int elementId) {
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
        String s = "death.attack." + Element.ElementGetter.get(this.element).getDamage().getMsgId() + "." + this.msgId;
        return new TranslatableComponent(s, p_19410_.getDisplayName(), component);
    }
}