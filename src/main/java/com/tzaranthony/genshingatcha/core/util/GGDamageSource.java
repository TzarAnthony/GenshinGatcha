package com.tzaranthony.genshingatcha.core.util;

import com.tzaranthony.genshingatcha.core.entities.elements.AbstractMagicProjectile;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;

import javax.annotation.Nullable;

public class GGDamageSource extends DamageSource {
    // Effects
    public static final DamageSource CRYO = (new GGDamageSource("cryo")).setMagic();
    public static final DamageSource PYRO = (new GGDamageSource("pyro")).setMagic().setIsFire();
    public static final DamageSource ELECTRO = (new GGDamageSource("electro")).setMagic();
    public static final DamageSource GEO = (new GGDamageSource("geo")).setMagic().bypassMagic();
    public static final DamageSource HYDRO = (new GGDamageSource("hydro")).setMagic();
    public static final DamageSource DENDRO = (new GGDamageSource("dendro")).setMagic();
    public static final DamageSource ANEMO = (new GGDamageSource("anemo")).setMagic().bypassArmor();

    public static DamageSource playerElementAttack(Player player, int elementId) {
        return new EntityElementDamageSource("player", player, elementId);
    }

    public static DamageSource mobElementAttack(LivingEntity livingEntity, int elementId) {
        return new EntityElementDamageSource("mob", livingEntity, elementId);
    }

    public static DamageSource indirectMagicElement(Entity magic, @Nullable Entity owner, int elementId) {
        return (new IndirectEntityElementMagicDamageSource("magic", magic, owner, elementId)).setProjectile().setMagic();
    }

    public static DamageSource magicElement(AbstractMagicProjectile magic, @Nullable Entity owner, int elementId) {
        return (new IndirectEntityElementMagicDamageSource("indirectMagic", magic, owner, elementId)).setProjectile().setMagic();
    }

    public static DamageSource arrowElement(AbstractArrow arrow, @Nullable Entity owner, int elementId) {
        return (new IndirectEntityElementDamageSource("arrow", arrow, owner, elementId)).setProjectile();
    }

    public GGDamageSource(String p_19333_) {
        super(p_19333_);
    }
}