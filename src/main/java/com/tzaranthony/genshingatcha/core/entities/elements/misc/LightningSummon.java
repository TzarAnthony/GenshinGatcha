package com.tzaranthony.genshingatcha.core.entities.elements.misc;

import com.tzaranthony.genshingatcha.core.entities.elements.ParticleCloudEntity;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LightningSummon extends ParticleCloudEntity {
    public LightningSummon(EntityType<? extends LightningSummon> wall, Level level) {
        super(wall, level);
        this.setParticle(ParticleTypes.WITCH);
        this.setRadius(1.0F);
        this.lifespan = 40;
    }

    public LightningSummon(Level level, double x, double y, double z, float yRot, int waitTime, LivingEntity owner) {
        this(GGEntities.LIGHTNING_SUMMONS.get(), level);
        this.setPos(x, y, z);
        this.setYRot(yRot);
        this.waitTime = waitTime;
        this.owner = owner;
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.tickCount >= this.waitTime) {
            ElectroLightning lightning = GGEntities.ELECTRO_LIGHTNING.get().create(this.level);
            lightning.moveTo(Vec3.atBottomCenterOf(this.blockPosition()));
            lightning.setOwner(this.getOwner());
            this.level.addFreshEntity(lightning);
            this.discard();
        }
    }

    @Override
    protected void spawnParticles(int i, float f1, boolean flag) {
        f1 = this.getRadius() + 0.5F;
        for(int j = 0; j < 4 ; ++j) {
            float f2 = this.random.nextFloat() * ((float)Math.PI * 2F);
            float f3 = Mth.sqrt(this.random.nextFloat()) * f1;
            double x = this.getX() + (double)(Mth.cos(f2) * f3);
            double y = this.getY() + 0.2D;
            double z = this.getZ() + (double)(Mth.sin(f2) * f3);
            double xd = (0.5D - this.random.nextDouble()) * 0.15D;
            double yd = 0.0D;
            double zd = (0.5D - this.random.nextDouble()) * 0.15D;

            this.level.addAlwaysVisibleParticle(this.getParticle(), true, x, y, z, xd, yd, zd);
        }
    }
}