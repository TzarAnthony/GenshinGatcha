package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
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
            // very confusing, idk if this water bubble shoots another water bubble when it lands or not
            double d1 = 4.0D;
            Vec3 vec3 = this.getViewVector(1.0F);
            double d2 = tgt.getX() - (this.getX() + vec3.x * d1);
            double d3 = tgt.getY(0.5D) - (0.5D + this.getY(0.5D));
            double d4 = tgt.getZ() - (this.getZ() + vec3.z * d1);
            if (!this.isSilent()) {
                level.levelEvent((Player)null, 1016, this.blockPosition(), 0);
            }

            LargeFireball largefireball = new LargeFireball(level, this, d2, d3, d4, 1);
            largefireball.setPos(this.getX() + vec3.x * 4.0D, this.getY(0.5D) + 0.5D, largefireball.getZ() + vec3.z * 4.0D);
            level.addFreshEntity(largefireball);
        }
        return isEnd;
    }
}