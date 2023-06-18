package com.tzaranthony.genshingatcha.core.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tzaranthony.genshingatcha.core.entities.mobs.ElementalEntity;
import com.tzaranthony.genshingatcha.core.util.tags.GGItemTags;
import com.tzaranthony.genshingatcha.registries.GGItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
        return le == owner || (owner.isAlliedTo(le) || (le instanceof TamableAnimal ta && ta.isOwnedBy(owner)));
    }

    public static boolean isEntityImmuneToElement(LivingEntity le, int elementId) {
        if (le instanceof ElementalEntity em) {
            return em.getElement() == elementId;
        }
        return Element.ElementGetter.get(elementId).isEntityImmune(le);
    }

    public class deathStorer {
        protected static final HashMap<Player, NonNullList<ItemStack>> playerLoot = new HashMap<>();

        public static void storePlayerItems(Player player, Collection<ItemEntity> items) {
            NonNullList<ItemStack> primos = NonNullList.create();
            for (ItemEntity ie : items) {
                if (ie.getItem().is(GGItems.PRIMOGEM.get()) || ie.getItem().is(GGItems.PRIMO_CARD.get()) || ie.getItem().is(GGItemTags.CHARACTERS)) {
                    primos.add(ie.getItem());
                }
            }
            playerLoot.put(player, primos);
        }

        public static void retrievePlayerItems(Player oPlayer, Player nPlayer) {
            NonNullList<ItemStack> primos = playerLoot.get(oPlayer);
            for (ItemStack stack : primos) {
                nPlayer.getInventory().add(stack);
            }
        }
    }

    public static final HashMap<Float, AttributeModifier> CharacterAttributeMap = new HashMap<>() {{
        put(1.1F, new AttributeModifier(UUID.fromString("c6dd5127-b284-4bb0-9c23-e1954f92080b"), "diluc_c1", 0.10D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        put(1.2F, new AttributeModifier(UUID.fromString("bec8d239-34b4-4635-9205-e60993369f95"), "diluc_c2", 0.03D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        put(2.1F, new AttributeModifier(UUID.fromString("ca0337f5-3396-4da7-ae03-44dbe21cef83"), "fischl_c1", 0.20D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        put(4.2F, new AttributeModifier(UUID.fromString("ff863bb0-9b7d-4366-8027-6a2502cd95c3"), "qiqi_c1", 0.10D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }};

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        multimap.put(Attributes.ATTACK_DAMAGE, CharacterAttributeMap.get(1.1F));
        multimap.put(Attributes.ATTACK_SPEED, CharacterAttributeMap.get(1.2F));
        multimap.put(Attributes.ATTACK_DAMAGE, CharacterAttributeMap.get(2.1F));
        multimap.put(Attributes.ATTACK_DAMAGE, CharacterAttributeMap.get(4.2F));
        return multimap;
    }
}