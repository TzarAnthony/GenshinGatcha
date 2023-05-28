package com.tzaranthony.genshingatcha.core.util;

import com.tzaranthony.genshingatcha.registries.GGItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Collection;
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

    public static boolean ignoreElementAttackEntity(LivingEntity le, LivingEntity owner) {
        return le instanceof Player || (le instanceof TamableAnimal ta && ta.isOwnedBy(owner));
    }

    public static final HashMap<Attribute, UUID> CharacterAttributeMap = new HashMap<>() {{
        put(Attributes.ATTACK_DAMAGE, UUID.fromString("c6dd5127-b284-4bb0-9c23-e1954f92080b"));
        put(Attributes.ATTACK_SPEED, UUID.fromString("bec8d239-34b4-4635-9205-e60993369f95"));
    }};

    public class primogemDeathStorer {
        protected static final HashMap<Player, NonNullList<ItemStack>> playerPrimogems = new HashMap<>();

        public static void storePlayerPrimos(Player player, Collection<ItemEntity> items) {
            NonNullList<ItemStack> primos = NonNullList.create();
            for (ItemEntity ie : items) {
                if (ie.getItem().is(GGItems.PRIMOGEM.get()) || ie.getItem().is(GGItems.PRIMO_CARD.get())) {
                    primos.add(ie.getItem());
                }
            }
            playerPrimogems.put(player, primos);
        }

        public static void retrievePlayerPrimos(Player oPlayer, Player nPlayer) {
            NonNullList<ItemStack> primos = playerPrimogems.get(oPlayer);
            for (ItemStack stack : primos) {
                nPlayer.getInventory().add(stack);
            }
        }
    }
}