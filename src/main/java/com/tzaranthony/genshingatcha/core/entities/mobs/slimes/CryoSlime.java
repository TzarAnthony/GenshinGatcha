package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.ElementalArrow;
import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.shapes.CollisionContext;

public class CryoSlime extends AbstractElementalSlime {
    public CryoSlime(EntityType<? extends AbstractElementalSlime> type, Level level) {
        super(type, level);
        this.setElement(Element.E.CRYO.getId());
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isCharging()) {
            if (this.level.isClientSide) {
                for(int i = 0; i < 3; ++i) {
                    double x = this.getX() + ((double) (i - 1) * 0.25D);
                    double y = this.getEyeY() + 1.2F;
                    double z = this.getZ() + ((double) (i - 1) * 0.25D);
                    this.level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0, 0, 0);
                }
            }
        }
    }

    protected ParticleOptions getParticleType() {
        return ParticleTypes.ITEM_SNOWBALL;
    }

    public boolean canFreeze() {
        return false;
    }

    @Override
    protected void onChangedBlock(BlockPos pos) {
        onEntityMoved(this, this.level, pos, 1);
    }

    protected void jumpInLiquid(TagKey<Fluid> fluidTag) {
        if (fluidTag == FluidTags.WATER) {
            BlockPos pos = this.getOnPos();
            this.setPos(this.getX(), this.getY() + 0.3D, this.getZ());
            onEntityMoved(this, this.level, pos, 1);
        }
        super.jumpInLiquid(fluidTag);
    }

    public static void onEntityMoved(LivingEntity le, Level level, BlockPos pos, int radius) {
        BlockState blockstate = Blocks.FROSTED_ICE.defaultBlockState();
        float f = (float)Math.min(16, 2 + radius);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(BlockPos blockpos : BlockPos.betweenClosed(pos.offset((double)(-f), -1.0D, (double)(-f)), pos.offset((double)f, -1.0D, (double)f))) {
            if (blockpos.closerToCenterThan(le.position(), (double)f)) {
                blockpos$mutableblockpos.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                BlockState blockstate1 = level.getBlockState(blockpos$mutableblockpos);
                if (blockstate1.isAir()) {
                    BlockState blockstate2 = level.getBlockState(blockpos);
                    boolean isFull = blockstate2.getBlock() == Blocks.WATER && blockstate2.getValue(LiquidBlock.LEVEL) == 0;
                    if (blockstate2.getMaterial() == Material.WATER && isFull && blockstate.canSurvive(level, blockpos) && level.isUnobstructed(blockstate, blockpos, CollisionContext.empty()) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(le, net.minecraftforge.common.util.BlockSnapshot.create(level.dimension(), level, blockpos), net.minecraft.core.Direction.UP)) {
                        level.setBlockAndUpdate(blockpos, blockstate);
                        level.scheduleTick(blockpos, Blocks.FROSTED_ICE, Mth.nextInt(le.getRandom(), 60, 120));
                    }
                }
            }
        }
    }

    protected boolean slimeChargeActivity(int chargeTime, LivingEntity tgt) {
        boolean isEnd = chargeTime >= 40;
        if (isEnd) {
            for(int i = 0; i < 3; ++i) {
                ElementalArrow arrow = new ElementalArrow(this, this.getElement(), this.level);
                double x = this.getX() + ((double) (i - 1) * 0.25D);
                double y = this.getEyeY() + 1.2F;
                double z = this.getZ() + ((double) (i - 1) * 0.25D);
                arrow.setPos(x, y, z);
                double d0 = tgt.getX() - x;
                double d1 = tgt.getY(0.3333333333333333D) - y;
                double d2 = tgt.getZ() - z;
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                arrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(13 - this.level.getDifficulty().getId() * 4));
                this.playSound(SoundEvents.SHULKER_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level.addFreshEntity(arrow);
            }
        }
        return isEnd;
    }
}