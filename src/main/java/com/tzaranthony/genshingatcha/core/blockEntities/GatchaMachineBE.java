package com.tzaranthony.genshingatcha.core.blockEntities;

import com.tzaranthony.genshingatcha.core.container.GachaMachineMenu;
import com.tzaranthony.genshingatcha.core.items.GoodieBag;
import com.tzaranthony.genshingatcha.core.items.PrimoCard;
import com.tzaranthony.genshingatcha.core.networks.ItemS2CPacket;
import com.tzaranthony.genshingatcha.registries.GGBlockEntities;
import com.tzaranthony.genshingatcha.registries.GGItems;
import com.tzaranthony.genshingatcha.registries.GGPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GatchaMachineBE extends BlockEntity implements MenuProvider, Nameable {
    protected final int pullCost = 160;
    private LockCode lockKey = LockCode.NO_LOCK;
    private Component name;
    protected final String ITEMINV = "Items";
    protected ItemStackHandler itemHandler  = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                GGPackets.sendToClients(new ItemS2CPacket(this, worldPosition));
            }
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            switch (slot) {
                case 0:
                case 1:
                    return stack.is(GGItems.PRIMO_CARD.get());
                default:
                    return super.isItemValid(slot, stack);
            }
        }
    };
    protected String PROG = "Progress";
    protected int progress;
    protected final String MAXTM = "TimeNeeded";
    protected int maxTime = 60;
    private final ContainerData dataAccess = new ContainerData() {
        public int get(int id) {
            switch(id) {
                case 0:
                    return GatchaMachineBE.this.progress;
                case 1:
                    return GatchaMachineBE.this.maxTime;
                default:
                    return 0;
            }
        }

        public void set(int id, int value) {
            switch(id) {
                case 0:
                    GatchaMachineBE.this.progress = value;
                    break;
                case 1:
                    GatchaMachineBE.this.maxTime = value;
            }
        }

        public int getCount() {
            return 2;
        }
    };

    public GatchaMachineBE(BlockPos pos, BlockState state) {
        super(GGBlockEntities.GATCHA_MACHINE.get(), pos, state);
    }

    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new GachaMachineMenu(id, inv, this.getBlockPos(), this.dataAccess);
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.progress = tag.getInt(PROG);
        this.maxTime = tag.getInt(MAXTM);
        this.itemHandler.deserializeNBT(tag);
        itemHandler.deserializeNBT(tag.getCompound(ITEMINV));
        if (tag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(PROG, this.progress);
        tag.putInt(MAXTM, this.maxTime);
        tag.put(ITEMINV, itemHandler.serializeNBT());
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GatchaMachineBE GGBE) {
        boolean flag = false;

        int slot = hasValidCard(GGBE);
        if (slot >= 0 && GGBE.itemHandler.getStackInSlot(GachaMachineMenu.outputSlotID).isEmpty()) {
            ++GGBE.progress;
            if (GGBE.progress >= GGBE.maxTime) {
                GGBE.progress = 0;
                GGBE.roll(slot);
                flag = true;
            }
        } else if (GGBE.progress > 0) {
            GGBE.progress = 0;
        }
        if (flag) {
            setChanged(level, pos, state);
        }
    }

    private static int hasValidCard(GatchaMachineBE GGBE) {
        ItemStack stack;
        for (int i = 0; i < GGBE.itemHandler.getSlots(); ++i) {
            stack = GGBE.itemHandler.getStackInSlot(i);
            if (!stack.isEmpty() && PrimoCard.getPrimoCount(stack) >= GGBE.getPullCostForSlot(i)) {
                return i;
            }
        }
        return -1;
    }

    private int getPullCostForSlot(int slot) {
        if (slot == GachaMachineMenu.characterSlotID10 || slot == GachaMachineMenu.weaponSlotID10) {
            return this.pullCost * 10;
        }
        return this.pullCost;
    }

    private void roll(int slot) {
        PrimoCard.calulatePrimoStorage(this.itemHandler.getStackInSlot(slot), -this.getPullCostForSlot(slot));
        if (slot == GachaMachineMenu.characterSlotID10 || slot == GachaMachineMenu.weaponSlotID10) {
            this.itemHandler.insertItem(GachaMachineMenu.outputSlotID, this.populateGoodieBag((slot - 1) == 0), false);
        } else {
            this.itemHandler.insertItem(GachaMachineMenu.outputSlotID, this.getRollRewards(slot == 0), false);
        }
    }

    private ItemStack populateGoodieBag(boolean character) {
        ItemStack stack = new ItemStack(GGItems.GOODIE_BAG.get());
        for (int i = 0; i < 10; ++i) {
            ItemStack inStack = getRollRewards(character);
            GoodieBag.add(stack, inStack);
        }
        return stack;
    }

    private ItemStack getRollRewards(boolean character) {
        float starRank = this.level.random.nextFloat();
        int weaponType = this.level.random.nextInt(4);
        if (starRank > 0.95F) {
            if (character) {
                return this.rollForCharacter();
            } else {
                return this.rollForTieredWeapon(starRank, weaponType);
            }
        } else if (starRank > 0.85F) {
            if (character && this.level.random.nextInt(5) != 0) {
                return this.rollForCharacter();
            } else {
                return this.rollForTieredWeapon(starRank, weaponType);
            }
        } else if (starRank > 0.65F) {
            switch (weaponType) {
                case 0:
                    return new ItemStack(GGItems.SWORD_THREE.get());
                case 1:
                    return new ItemStack(GGItems.CLAYMORE_THREE.get());
                case 2:
                    return new ItemStack(GGItems.SPEAR_THREE.get());
                default:
                    return new ItemStack(GGItems.BOW_THREE.get());
            }
        } else if (starRank > 0.35F) {
            switch (weaponType) {
                case 0:
                    return new ItemStack(GGItems.SWORD_TWO.get());
                case 1:
                    return new ItemStack(GGItems.CLAYMORE_TWO.get());
                case 2:
                    return new ItemStack(GGItems.SPEAR_TWO.get());
                default:
                    return new ItemStack(GGItems.BOW_TWO.get());
            }
        } else {
            switch (weaponType) {
                case 0:
                    return new ItemStack(GGItems.SWORD_ONE.get());
                case 1:
                    return new ItemStack(GGItems.CLAYMORE_ONE.get());
                case 2:
                    return new ItemStack(GGItems.SPEAR_ONE.get());
                default:
                    return new ItemStack(GGItems.BOW_ONE.get());
            }
        }
    }

    private ItemStack rollForCharacter() {
        int characterID = this.level.random.nextInt(5);
        switch (characterID) {
            case 0:
                return new ItemStack(GGItems.DILUC_C0.get());
            case 1:
                return new ItemStack(GGItems.FISCHL_C0.get());
            case 2:
                return new ItemStack(GGItems.ZHONGLI_C0.get());
            default:
                return new ItemStack(GGItems.QIQI_C0.get());
        }
    }

    private ItemStack rollForTieredWeapon(float starRank, int weaponType) {
        if (starRank > 0.95F) {
            switch (weaponType) {
                case 0:
                    return new ItemStack(GGItems.SWORD_FIVE.get());
                case 1:
                    return new ItemStack(GGItems.CLAYMORE_FIVE.get());
                case 2:
                    return new ItemStack(GGItems.SPEAR_FIVE.get());
                default:
                    return new ItemStack(GGItems.BOW_FIVE.get());
            }
        } else {
            switch (weaponType) {
                case 0:
                    return new ItemStack(GGItems.SWORD_FOUR.get());
                case 1:
                    return new ItemStack(GGItems.CLAYMORE_FOUR.get());
                case 2:
                    return new ItemStack(GGItems.SPEAR_FOUR.get());
                default:
                    return new ItemStack(GGItems.BOW_FOUR.get());
            }
        }
    }

    // capabilities
    private LazyOptional<? extends IItemHandler> handlers = LazyOptional.of(() -> itemHandler);
    private Map<Direction, LazyOptional<SidedItemHandler>> directionHandler = new HashMap<>() {{
        put(Direction.UP, LazyOptional.of(() -> new SidedItemHandler(itemHandler, List.of(0), List.of())));
        put(Direction.NORTH, LazyOptional.of(() -> new SidedItemHandler(itemHandler, List.of(1), List.of())));
        put(Direction.SOUTH, LazyOptional.of(() -> new SidedItemHandler(itemHandler, List.of(1), List.of())));
        put(Direction.EAST, LazyOptional.of(() -> new SidedItemHandler(itemHandler, List.of(1), List.of())));
        put(Direction.WEST, LazyOptional.of(() -> new SidedItemHandler(itemHandler, List.of(1), List.of())));
        put(Direction.DOWN, LazyOptional.of(() -> new SidedItemHandler(itemHandler, List.of(), List.of(4))));
    }};

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return directionHandler.get(facing).cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handlers.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.handlers = LazyOptional.of(() -> itemHandler);
    }

    public static class SidedItemHandler implements IItemHandlerModifiable {
        protected final ItemStackHandler inv;
        protected final List<Integer> insertSlots;
        protected final List<Integer> extractSlots;

        public SidedItemHandler(ItemStackHandler inv, List<Integer> insertSlots, List<Integer> extractSlots) {
            this.inv = inv;
            this.insertSlots = insertSlots;
            this.extractSlots = extractSlots;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            SidedItemHandler that = (SidedItemHandler) o;
            return inv.equals(that.inv);
        }

        @Override
        public int hashCode() {
            return inv.hashCode();
        }

        @Override
        public int getSlots() {
            return inv.getSlots();
        }

        @Override
        @Nonnull
        public ItemStack getStackInSlot(int slot) {
            return inv.getStackInSlot(slot);
        }

        @Override
        @Nonnull
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (!insertSlots.contains(slot))
                return ItemStack.EMPTY;
            return inv.insertItem(slot, stack, simulate);
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            inv.setStackInSlot(slot, stack);
        }

        @Override
        @Nonnull
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (!extractSlots.contains(slot))
                return ItemStack.EMPTY;
            return inv.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return inv.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return inv.isItemValid(slot, stack);
        }
    }

    // menu stuff
    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return this.canOpen(player) ? this.createMenu(id, inv) : null;
    }

    public boolean canOpen(Player player) {
        return canUnlock(player, this.lockKey, this.getDisplayName());
    }

    public static boolean canUnlock(Player player, LockCode locker, Component comp) {
        if (!player.isSpectator() && !locker.unlocksWith(player.getMainHandItem())) {
            player.displayClientMessage(new TranslatableComponent("container.isLocked", comp), true);
            player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return false;
        } else {
            return true;
        }
    }

    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void setCustomName(Component p_58639_) {
        this.name = p_58639_;
    }

    @Override
    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    public Component getCustomName() {
        return this.name;
    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container.genshingatcha.gatcha_machine");
    }

    // container and updates
    public void dropInventory() {
        Containers.dropContents(this.level, this.worldPosition, this.createContainer());
    }

    public Container createContainer() {
        Container container = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            container.setItem(i, itemHandler.getStackInSlot(i));
        }
        return container;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.put(ITEMINV, itemHandler.serializeNBT());
        return tag;
    }

    public ItemStackHandler getItemHandler() {
        return  this.itemHandler;
    }

    public void setItemHandler(ItemStackHandler items) {
        this.itemHandler = items;
    }
}