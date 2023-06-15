package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.entities.elements.mobs.Pyroball;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.registries.GGParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class PyroSlime extends AbstractElementalSlime {
    public PyroSlime(EntityType<? extends AbstractElementalSlime> type, Level level) {
        super(type, level);
        this.setElement(Element.E.PYRO.getId());
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isCharging()) {
            if (this.level.isClientSide) {
                for(int i = 0; i < 5; ++i) {
                    double d0 = this.getX() + this.random.nextDouble();
                    double d1 = this.getY() + this.random.nextDouble();
                    double d2 = this.getZ() + this.random.nextDouble();
                    double d3 = (this.random.nextDouble() - 0.5D) * 0.5D;
                    double d4 = (this.random.nextDouble() - 0.5D) * 0.5D;
                    double d5 = (this.random.nextDouble() - 0.5D) * 0.5D;
                    int k = this.random.nextInt(2) * 2 - 1;
                    if (this.random.nextBoolean()) {
                        d2 = this.getZ() + 0.5D + 0.25D * (double)k;
                        d5 =(this.random.nextFloat() * 2.0F * (float)k);
                    } else {
                        d0 = this.getX() + 0.5D + 0.25D * (double)k;
                        d3 = (this.random.nextFloat() * 2.0F * (float)k);
                    }

                    this.level.addParticle(GGParticleTypes.MOVING_FIRE.get(), d0, d1, d2, d3, d4, d5);
                }
            }
        }
    }

    protected ParticleOptions getParticleType() {
        return ParticleTypes.FLAME;
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean fireImmune() {
        return true;
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected void jumpInLiquid(TagKey<Fluid> fluidTag) {
        if (fluidTag == FluidTags.LAVA) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x, (0.22F + (float)this.getSize() * 0.05F), vec3.z);
            this.hasImpulse = true;
        } else {
            super.jumpInLiquid(fluidTag);
        }
    }

    @Override
    public void die(DamageSource source) {
        if (level.isClientSide()) {
            Random rand = this.random;
            for(int j = 0; j < 128; ++j) {
                double d0 = (double) j / 127.0D;
                float f = (rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
                double d1 = Mth.lerp(d0, this.xo, this.getX()) + (rand.nextDouble() - 0.5D) * this.getBbWidth() * 2.0D;
                double d2 = Mth.lerp(d0, this.yo, this.getY()) + rand.nextDouble() * (double) this.getBbHeight();
                double d3 = Mth.lerp(d0, this.zo, this.getZ()) + (rand.nextDouble() - 0.5D) * this.getBbWidth() * 2.0D;
                level.addParticle(ParticleTypes.SMALL_FLAME, d1, d2, d3, f, f1, f2);
            }
        }
        super.die(source);
    }

    @Override
    public void remove(RemovalReason reason) {
        BlockPos cPos;
        int diameter = this.getSize() * 2 + 1;
        if (diameter == 3) {
            this.level.setBlock(this.getOnPos().above(), Blocks.FIRE.defaultBlockState(), 2);
        } else {
            BlockPos startPos = this.getOnPos().below(this.getSize()).north(this.getSize()).east(this.getSize());
            for (int i = 0; i < diameter; ++i) {
                for (int j = 0; j < diameter; ++j) {
                    if (this.random.nextInt(3) == 0) {
                        cPos = startPos.south(i).west(j);
                        BlockPos blockpos = EntityUtil.getFloorInRange(this.level, cPos.getX(), cPos.getY() - 4, cPos.getY() + 4.0D, cPos.getZ());
                        if (blockpos != null) {
                            this.level.setBlock(blockpos, Blocks.FIRE.defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
        this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.0F);
        super.remove(reason);
    }

    protected boolean slimeChargeActivity(int chargeTime, LivingEntity tgt) {
        if (chargeTime >= 41 && chargeTime % 5 == 0) {
            Pyroball pyroball = new Pyroball(this, this.level);
            double x = this.getX();
            double y = this.getEyeY() - 0.5D;
            double z = this.getZ();
            pyroball.setPos(x, y, z);
            double d0 = tgt.getX() - x;
            double d1 = tgt.getY(0.3333333333333333D) - y;
            double d2 = tgt.getZ() - z;
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            pyroball.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(13 - this.level.getDifficulty().getId() * 4));
            this.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(pyroball);
        }
        return chargeTime >= 55;
    }
}