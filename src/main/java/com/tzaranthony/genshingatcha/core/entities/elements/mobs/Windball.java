package com.tzaranthony.genshingatcha.core.entities.elements.mobs;

import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.AbstractElementalProjectile;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.effects.ElementEffectInstance;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class Windball extends AbstractElementalProjectile implements ItemSupplier {
    public Windball(EntityType<? extends Windball> projectile, Level level) {
        super(projectile, level);
    }

    public Windball(LivingEntity owner, Level level, double powX, double powY, double powZ) {
        super(GGEntities.WIND_BALL.get(), owner, Element.E.ANEMO.getId(), level, powX, powY, powZ);
    }

    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.SPIT;
    }

    @Override
    protected void performOnEntity(LivingEntity target, Entity user) {
        this.doExplosion();
    }

    @Override
    protected void performOnBlock(Entity owner, Level level, BlockPos pos) {
        this.doExplosion();
    }

    protected void doExplosion() {
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaeffectcloud.setOwner((LivingEntity)entity);
        }
        areaeffectcloud.setRadius(3.5F);
        areaeffectcloud.setRadiusOnUse(-0.5F);
        areaeffectcloud.setWaitTime(5);
        areaeffectcloud.setDuration(40);
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
        areaeffectcloud.addEffect(new ElementEffectInstance(this.getElement(), 200));
        this.level.addFreshEntity(areaeffectcloud);

        this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.ENDER_PEARL);
    }
}