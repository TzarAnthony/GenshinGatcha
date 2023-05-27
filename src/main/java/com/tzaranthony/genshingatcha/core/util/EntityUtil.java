package com.tzaranthony.genshingatcha.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class EntityUtil {
    public static BlockPos getFloorInRange(Level level, double x, double yMin, double yMax, double z) {
        boolean canSpawn = false;
        BlockPos blockpos = new BlockPos(x, yMax, z);
        double d0 = 0.0D;
        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(level, blockpos1, Direction.UP)) {
                if (!level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }
                canSpawn = true;
                break;
            }
            blockpos = blockpos.below();
        } while (blockpos.getY() >= Mth.floor(yMin));

        if (canSpawn) {
            return blockpos.above((int) d0);
        }
        return null;
    }

    public static void performExplosion(DamageSource source, Entity entity, @Nullable LivingEntity owner, float baseDmg, double range, boolean shouldIgnite) {
        Vec3 vec3 = entity.position();
        for(Mob le : entity.level.getEntitiesOfClass(Mob.class, entity.getBoundingBox().inflate(3.0D))) {
            if (!(entity.distanceToSqr(le) > 25.0D) && le instanceof TamableAnimal ta && !ta.isOwnedBy(owner)) {
                boolean flag = false;
                for(int i = 0; i < 2; ++i) {
                    Vec3 vec31 = new Vec3(le.getX(), le.getY(0.5D * (double)i), le.getZ());
                    HitResult hitresult = entity.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
                    if (hitresult.getType() == HitResult.Type.MISS) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    float f1 = baseDmg * (float)Math.sqrt((range - (double) entity.distanceTo(le)) / range);
                    le.hurt(source, f1);
                    if (shouldIgnite) {
                        le.setSecondsOnFire(10);
                    }
                }
            }
        }
    }

    public static final HashMap<Attribute, UUID> CharacterAttributeMap = new HashMap<>() {{
        put(Attributes.ATTACK_DAMAGE, UUID.fromString("c6dd5127-b284-4bb0-9c23-e1954f92080b"));
        put(Attributes.ATTACK_SPEED, UUID.fromString("bec8d239-34b4-4635-9205-e60993369f95"));
    }};
}