package com.tzaranthony.genshingatcha.core.entities.elements.mobs;

import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.ElementalArrow;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class Pyroball extends ElementalArrow implements ItemSupplier {
    public Pyroball(EntityType<? extends ElementalArrow> entityType, Level level) {
        super(entityType, level);
    }

    public Pyroball(LivingEntity owner, Level level) {
        super(GGEntities.PYROBALL.get(), owner, Element.E.PYRO.getId(), level);
        this.setSoundEvent(SoundEvents.FIRECHARGE_USE);
    }

    protected void doPostHitEffects(BlockPos pos, Direction dir) {
        this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);
        super.doPostHitEffects(pos, dir);
    }

    public ItemStack getItem() {
        return new ItemStack(Items.FIRE_CHARGE);
    }
}