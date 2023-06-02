package com.tzaranthony.genshingatcha.core.entities.projectiles;

import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ExplodingThrownPotion extends ThrownPotion {
    public ExplodingThrownPotion(EntityType<? extends ThrownPotion> type, Level level) {
        super(type, level);
    }

    public ExplodingThrownPotion(Level level, LivingEntity entity) {
        super(GGEntities.EXPLODING_POTION.get(), level);
        setOwner(entity);
    }

    protected void onHitBlock(BlockHitResult result) {
        this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 3.0F, Explosion.BlockInteraction.NONE);
        super.onHitBlock(result);
    }

    protected void onHit(HitResult result) {
        this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 3.0F, Explosion.BlockInteraction.NONE);
        super.onHit(result);
    }
}