package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.entities.elements.mobs.Waterball;
import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class HydroSlime extends AbstractElementalSlime {
    public HydroSlime(EntityType<? extends AbstractElementalSlime> type, Level level) {
        super(type, level);
        this.setElement(Element.E.HYDRO.getId());
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
    }

    protected ParticleOptions getParticleType() {
        return ParticleTypes.SPLASH;
    }

    public boolean isShaking() {
        return this.isCharging();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    protected void jumpInLiquid(TagKey<Fluid> fluidTag) {
        if (fluidTag == FluidTags.WATER) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x, (0.22F + (float)this.getSize() * 0.05F), vec3.z);
            this.hasImpulse = true;
        } else {
            super.jumpInLiquid(fluidTag);
        }
    }

    protected boolean slimeChargeActivity(int chargeTime, LivingEntity tgt) {
        boolean isEnd = chargeTime >= 40;
        if (isEnd) {
            Waterball waterball = new Waterball(this, this.level);
            waterball.setPos(this.getX(), this.getEyeY() + 1.0F, this.getZ());
            double d0 = tgt.getX() - this.getX();
            double d1 = tgt.getY(0.3333333333333333D) - waterball.getY();
            double d2 = tgt.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            waterball.shoot(d0, d1 + d3 * (double)0.2F, d2, 0.8F, (float)(13 - this.level.getDifficulty().getId() * 4));
            this.playSound(SoundEvents.SHULKER_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(waterball);
        }
        return isEnd;
    }
}