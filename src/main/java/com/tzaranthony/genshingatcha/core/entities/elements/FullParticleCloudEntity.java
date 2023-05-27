package com.tzaranthony.genshingatcha.core.entities.elements;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class FullParticleCloudEntity extends ParticleCloudEntity{
    public FullParticleCloudEntity(EntityType<? extends FullParticleCloudEntity> type, Level level) {
        super(type, level);
    }

    public void tick() {
        if (!this.isWaiting()) {
            List<Mob> entities = this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox());
            if (!entities.isEmpty()) {
                for(Mob le : entities) {
                    this.performOnEntity(le);
                }
            }
        }

        if (this.tickCount >= this.waitTime + this.lifespan) {
            this.performOnDiscard();
        }

        super.tick();
    }

    protected abstract void performOnEntity(LivingEntity le);

    protected abstract void performOnDiscard();

    protected void spawnParticles(int i, float f1, boolean flag) {
        if (!flag) {
            f1 = this.getRadius() + 0.5F;

            for(int j = 0; j < 12 ; ++j) {
                float f2 = this.random.nextFloat() * ((float)Math.PI * 2F);
                float f3 = Mth.sqrt(this.random.nextFloat()) * f1;
                double x = this.getX() + (double)(Mth.cos(f2) * f3);
                double y = this.getY() + (double)(Mth.cos(f2) * f3);
                double z = this.getZ() + (double)(Mth.sin(f2) * f3);
                double xd = (0.5D - this.random.nextDouble()) * 0.15D;
                double yd = (0.5D - this.random.nextDouble()) * 0.15D;
                double zd = (0.5D - this.random.nextDouble()) * 0.15D;

                this.level.addAlwaysVisibleParticle(this.getParticle(), true, x, y, z, xd, yd, zd);
            }
        }
    }
}