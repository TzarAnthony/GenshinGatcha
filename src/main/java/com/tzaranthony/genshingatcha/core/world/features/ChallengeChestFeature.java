package com.tzaranthony.genshingatcha.core.world.features;

import com.mojang.serialization.Codec;
import com.tzaranthony.genshingatcha.core.blocks.ChallengeChest;
import com.tzaranthony.genshingatcha.registries.GGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

import java.util.Random;

public class ChallengeChestFeature extends Feature<ProbabilityFeatureConfiguration> {
    public ChallengeChestFeature(Codec<ProbabilityFeatureConfiguration> PFC) {
        super(PFC);
    }

    public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> PFC) {
        int i = 0;
        Random random = PFC.random();
        WorldGenLevel worldgenlevel = PFC.level();
        BlockPos blockpos = PFC.origin();
        BlockPos.MutableBlockPos mutablePos = blockpos.mutable();
        ProbabilityFeatureConfiguration PFCconfig = PFC.config();
        boolean isAir = worldgenlevel.isEmptyBlock(mutablePos);
        boolean isWater = worldgenlevel.getBlockState(mutablePos).is(Blocks.WATER);
        if (isAir || isWater) {
            if (random.nextFloat() < PFCconfig.probability) {
                int k = random.nextInt(4) + 1;
                for(int l = blockpos.getX() - k; l <= blockpos.getX() + k; ++l) {
                    for(int i1 = blockpos.getZ() - k; i1 <= blockpos.getZ() + k; ++i1) {
                        int j1 = l - blockpos.getX();
                        int k1 = i1 - blockpos.getZ();
                        if (j1 * j1 + k1 * k1 <= k * k) {
                            if (isAir) {
                                mutablePos.set(l, worldgenlevel.getHeight(Heightmap.Types.WORLD_SURFACE, l, i1) - 1, i1);
                                worldgenlevel.setBlock(mutablePos.above(), GGBlocks.CHALLENGE_CHEST.get().defaultBlockState().setValue(ChallengeChest.FACING, this.getRandomFacingDirection(random)), 2);
                                return true;
                            } else if (isWater) {
                                mutablePos.set(l, worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR, l, i1) - 1, i1);
                                worldgenlevel.setBlock(mutablePos, GGBlocks.CHALLENGE_CHEST.get().defaultBlockState().setValue(ChallengeChest.FACING, this.getRandomFacingDirection(random)).setValue(ChallengeChest.WATERLOGGED, Boolean.valueOf(true)), 2);
                                return true;
                            }
                        }
                    }
                }
            }
            ++i;
        }
        return i > 0;
    }

    protected Direction getRandomFacingDirection(Random random) {
        switch (random.nextInt(5)) {
            case 1:
                return Direction.NORTH;
            case 2:
                return Direction.SOUTH;
            case 3:
                return Direction.EAST;
            default:
                return Direction.WEST;
        }
    }
}