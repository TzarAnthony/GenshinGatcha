package com.tzaranthony.genshingatcha.core.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MagicExplosion extends Explosion {
    private final boolean fire;
    private final Random random = new Random();
    private final Level level;
    private final double x;
    private final double y;
    private final double z;
    @Nullable
    private final Entity source;
    private final float radius;
    private final List<BlockPos> toBlow = Lists.newArrayList();
    private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();

    public MagicExplosion(Entity entity, double x, double y, double z, float radius) {
        this(entity.level, entity, x, y, z, radius, false);
    }

    public MagicExplosion(Level p_46051_, @Nullable Entity p_46052_, double x, double y, double z, float radius, boolean fires) {
        super(p_46051_, p_46052_, null, null, x, y, z, radius, fires, BlockInteraction.NONE);
        this.level = p_46051_;
        this.source = p_46052_;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.z = z;
        this.fire = fires;
    }

    public void explode() {
        this.level.gameEvent(this.source, GameEvent.EXPLODE, new BlockPos(this.x, this.y, this.z));
        Set<BlockPos> set = Sets.newHashSet();
        this.toBlow.addAll(set);


        float f2 = this.radius * 2.0F;
        int k1 = Mth.floor(this.x - (double)f2 - 1.0D);
        int l1 = Mth.floor(this.x + (double)f2 + 1.0D);
        int i2 = Mth.floor(this.y - (double)f2 - 1.0D);
        int i1 = Mth.floor(this.y + (double)f2 + 1.0D);
        int j2 = Mth.floor(this.z - (double)f2 - 1.0D);
        int j1 = Mth.floor(this.z + (double)f2 + 1.0D);
        List<Entity> list = this.level.getEntities(this.source, new AABB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.level, this, list, f2);
        Vec3 vec3 = new Vec3(this.x, this.y, this.z);

        for(int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = list.get(k2);
            if (!entity.ignoreExplosion() && !(entity instanceof Player)) {
                double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double)f2;
                if (d12 <= 1.0D) {
                    double d5 = entity.getX() - this.x;
                    double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
                    double d9 = entity.getZ() - this.z;
                    double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d13 != 0.0D) {
                        d5 /= d13;
                        d7 /= d13;
                        d9 /= d13;
                        double d14 = (double)getSeenPercent(vec3, entity);
                        double d10 = (1.0D - d12) * d14;
                        entity.hurt(this.getDamageSource(), (float) ((int)((d10 * d10 + d10) / 2.0D * 7.0D * (double)f2 + 1.0D)) * 2.0F);
                        double d11 = d10;
                        if (entity instanceof LivingEntity) {
                            d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)entity, d10);
                        }

                        entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                    }
                }
            }
        }
    }

    public void finalizeExplosion(boolean showParticles) {
        if (this.level.isClientSide) {
            this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
        }
        if (showParticles) {
            if (!(this.radius < 2.0F)) {
                this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            } else {
                this.level.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            }
        }

        if (this.fire) {
            for(BlockPos blockpos2 : this.toBlow) {
                if (this.random.nextInt(3) == 0 && this.level.getBlockState(blockpos2).isAir() && this.level.getBlockState(blockpos2.below()).isSolidRender(this.level, blockpos2.below())) {
                    this.level.setBlockAndUpdate(blockpos2, BaseFireBlock.getState(this.level, blockpos2));
                }
            }
        }
    }

    public Map<Player, Vec3> getHitPlayers() {
        return this.hitPlayers;
    }

    public void clearToBlow() {
        this.toBlow.clear();
    }

    public List<BlockPos> getToBlow() {
        return this.toBlow;
    }
}