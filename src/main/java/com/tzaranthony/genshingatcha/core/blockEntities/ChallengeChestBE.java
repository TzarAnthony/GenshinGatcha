package com.tzaranthony.genshingatcha.core.blockEntities;

import com.tzaranthony.genshingatcha.core.entities.mobs.ElementalEntity;
import com.tzaranthony.genshingatcha.core.entities.mobs.slimes.*;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.registries.GGBlockEntities;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import com.tzaranthony.genshingatcha.registries.GGItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class ChallengeChestBE extends BlockEntity implements LidBlockEntity {
    private final ChestLidController chestLidController = new ChestLidController();
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            ChallengeChestBE.playSound(level, pos, state, SoundEvents.CHEST_OPEN);
        }

        protected void onClose(Level level, BlockPos pos, BlockState state) {
            ChallengeChestBE.playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
        }

        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int idk, int idk2) {
            ChallengeChestBE.this.signalOpenCount(level, pos, state, idk, idk2);
        }

        protected boolean isOwnContainer(Player player) {
            return false;
        }
    };
    protected boolean slimesGenerated = false;
    protected int slimeType = -1;
    protected int slimeCount;
    protected List<UUID> slimeUUIDs = NonNullList.create();
    protected List<Integer> slimeEIDs = NonNullList.create();
    public boolean rewardGiven = false;
    public int rewardTick = 0;
    protected static Map<Integer, EntityType> elementSlimeMap = new HashMap<>(){{
        put(Element.E.CRYO.getId(), GGEntities.CRYO_SLIME.get());
        put(Element.E.PYRO.getId(), GGEntities.PYRO_SLIME.get());
        put(Element.E.ELECTRO.getId(), GGEntities.ELECTRO_SLIME.get());
        put(Element.E.GEO.getId(), GGEntities.GEO_SLIME.get());
        put(Element.E.HYDRO.getId(), GGEntities.HYDRO_SLIME.get());
        put(Element.E.DENDRO.getId(), GGEntities.DENDRO_SLIME.get());
        put(Element.E.ANEMO.getId(), GGEntities.ANEMO_SLIME.get());
    }};
    protected static Map<Integer, Class> elementSlimeClassMap = new HashMap<>(){{
        put(Element.E.CRYO.getId(), CryoSlime.class);
        put(Element.E.PYRO.getId(), PyroSlime.class);
        put(Element.E.ELECTRO.getId(), ElectroSlime.class);
        put(Element.E.GEO.getId(), GeoSlime.class);
        put(Element.E.HYDRO.getId(), HydroSlime.class);
        put(Element.E.DENDRO.getId(), DendroSlime.class);
        put(Element.E.ANEMO.getId(), AnemoSlime.class);
    }};

    public ChallengeChestBE(BlockPos pos, BlockState state) {
        super(GGBlockEntities.CHALLENGE_CHEST_BE.get(), pos, state);
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.slimeCount = tag.getInt("slime_count");
        if (tag.contains("slimesUUID")) {
            CompoundTag tag1 = tag.getCompound("slimesUUID");
            for (int i = 0; i < this.slimeCount; ++i) {
                this.slimeUUIDs.add(tag1.getUUID("slime" + i));
            }
        }
        if (tag.contains("slimesEID")) {
            CompoundTag tag1 = tag.getCompound("slimes");
            for (int i = 0; i < this.slimeCount; ++i) {
                this.slimeEIDs.add(tag1.getInt("slime" + i));
            }
        }
        this.slimeType = tag.getInt("element");
        this.slimesGenerated = tag.getBoolean("slimes_generated");
        this.rewardGiven = tag.getBoolean("reward_given");
        this.rewardTick = tag.getInt("rewardTick");
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("slime_count", this.slimeCount);
        if (!this.slimeUUIDs.isEmpty()) {
            CompoundTag tag1 = new CompoundTag();
            for (int i = 0; i < this.slimeCount; ++i) {
                tag1.putUUID("slime" + i, this.slimeUUIDs.get(i));
            }
            tag.put("slimesUUID", tag1);
        }
        if (!this.slimeEIDs.isEmpty()) {
            CompoundTag tag1 = new CompoundTag();
            for (int i = 0; i < this.slimeCount; ++i) {
                tag1.putInt("slime" + i, this.slimeEIDs.get(i));
            }
            tag.put("slimesEID", tag1);
        }
        tag.putInt("element", this.slimeType);
        tag.putBoolean("slimes_generated", this.slimesGenerated);
        tag.putBoolean("reward_given", this.rewardGiven);
        tag.putInt("rewardTick", this.rewardTick);
    }

    public void incrementTick() {
        ++this.rewardTick;
    }

    public boolean tryOpenChest(Player player) {
        BlockPos spawnPos = this.getBlockPos();
        if (this.rewardGiven) {
            this.playEmptyMessage(player);
            return false;
        } else if (!this.slimesGenerated) {
            this.slimeType = ElementalEntity.getElementFromBiome(this.getLevel().getBiome(this.getBlockPos()), this.getLevel().random);
            EntityType slimeType = elementSlimeMap.get(this.slimeType);
            this.slimeCount = this.level.random.nextInt(2) + 1;
            Optional<BlockPos> optPos = EntityUtil.getValidSpawnPos(3, 6, this.level, this.getBlockPos());
            for (int i = 0; i < this.slimeCount; ++i) {
                AbstractElementalSlime slime = (AbstractElementalSlime) slimeType.create(this.level);
                if (optPos.isPresent()) {
                    spawnPos = optPos.get();
                }
                slime.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                slime.setSize(1 << 2);
                slime.setPersistenceRequired();
                this.level.addFreshEntity(slime);
                this.slimeUUIDs.add(slime.getUUID());
                this.slimeEIDs.add(slime.getId());
            }
            setChanged(this.level, this.getBlockPos(), this.getBlockState());
            this.slimesGenerated = true;
            this.level.playSound(null, this.getBlockPos(), SoundEvents.WITCH_CELEBRATE, SoundSource.BLOCKS, 1.0F, (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F + 1.0F);
            return false;
        } else if (!this.areSlimesDead()) {
            playLockMessage(player);
            return false;
        } else if (!this.rewardGiven && !this.remove) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
            this.chestLidController.shouldBeOpen(true);
            for (int i = 0; i < this.slimeCount; ++i) {
                this.level.addFreshEntity(new ItemEntity(this.level, spawnPos.getX(), spawnPos.above().getY(), spawnPos.getZ(), new ItemStack(GGItems.PRIMOGEM.get(), 64)));
            }
            setChanged(this.level, this.getBlockPos(), this.getBlockState());
            this.rewardGiven = true;
            return true;
        }
        return false;
    }

    private boolean areSlimesDead() {
        boolean result = true;
        if (this.level.isClientSide) {
            for (int eID : this.slimeEIDs) {
                LivingEntity slime = (LivingEntity) this.level.getEntity(eID);
                result = result && (slime == null || slime.isRemoved() || slime.isDeadOrDying());
                if (slime != null) {
                    this.addGlowing(slime);
                }
            }
        } else {
            for (UUID uuid : this.slimeUUIDs) {
                LivingEntity slime = (LivingEntity) ((ServerLevel) this.level).getEntity(uuid);
                result = result && (slime == null || slime.isRemoved() || slime.isDeadOrDying());
                if (slime != null) {
                    this.addGlowing(slime);
                }
            }
        }
        if (this.slimeType != -1 && result) {
            List<AbstractElementalSlime> slimes = this.level.getEntitiesOfClass(elementSlimeClassMap.get(this.slimeType), new AABB(this.getBlockPos()).inflate(16));
            result = result && slimes.isEmpty();
            for (AbstractElementalSlime slime : slimes) {
                this.addGlowing(slime);
            }
        }
        return result;
    }

    public static void playLockMessage(Player player) {
        player.displayClientMessage(new TranslatableComponent("container.genshingatcha.challenge_incomplete"), true);
        player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public static void playEmptyMessage(Player player) {
        player.displayClientMessage(new TranslatableComponent("container.genshingatcha.empty"), true);
        player.playNotifySound(SoundEvents.SHULKER_BULLET_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    protected void addGlowing(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100));
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent event) {
        level.playSound((Player)null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, event, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ChallengeChestBE ccBE) {
        if (ccBE.rewardGiven) {
            ccBE.incrementTick();
            if (ccBE.rewardTick >= 100) {
                level.removeBlock(ccBE.getBlockPos(), true);
            }
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, ChallengeChestBE ccBE) {
        ccBE.chestLidController.tickLid();
    }

    public boolean triggerEvent(int p_59285_, int p_59286_) {
        if (p_59285_ == 1) {
            this.chestLidController.shouldBeOpen(p_59286_ > 0);
            return true;
        } else {
            return super.triggerEvent(p_59285_, p_59286_);
        }
    }

    public float getOpenNess(float openess) {
        return this.chestLidController.getOpenness(openess);
    }

    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int idk, int idk2) {
        Block block = state.getBlock();
        level.blockEvent(pos, block, 1, idk2);
    }
}