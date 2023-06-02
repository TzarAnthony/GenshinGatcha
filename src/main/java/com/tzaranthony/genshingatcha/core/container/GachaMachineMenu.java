package com.tzaranthony.genshingatcha.core.container;

import com.tzaranthony.genshingatcha.core.blockEntities.GatchaMachineBE;
import com.tzaranthony.genshingatcha.registries.GGMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class GachaMachineMenu extends AbstractContainerMenu {
    protected final GatchaMachineBE blockEntity;
    protected final ItemStackHandler container;
    protected final ContainerData data;
    protected final Level level;
    protected final Inventory inventory;
    public static final int characterSlotID = 0;
    public static final int characterSlotID10 = 1;
    public static final int weaponSlotID = 2;
    public static final int weaponSlotID10 = 3;
    public static final int outputSlotID = 4;

    public GachaMachineMenu(int id, Inventory inventory, BlockPos pos) {
        this(id, inventory, pos, new SimpleContainerData(2));
    }

    public GachaMachineMenu(int id, Inventory inventory, BlockPos pos, ContainerData data) {
        super(GGMenus.GATCHA_MACHINE.get(), id);
        this.blockEntity = (GatchaMachineBE) inventory.player.level.getBlockEntity(pos);
        this.container = blockEntity.getItemHandler();
        this.data = data;
        this.inventory = inventory;
        this.level = inventory.player.level;

        checkContainerSize(inventory, 3);

        this.addInputSlot(characterSlotID, 62, 29);
        this.addInputSlot(characterSlotID10, 98, 29);
        this.addInputSlot(weaponSlotID, 62, 60);
        this.addInputSlot(weaponSlotID10, 98, 60);
        this.addResultSlot(outputSlotID, 148, 47);
        this.addInventory(8, 84);

        this.addDataSlots(this.data);
    }

    public void addInputSlot(int id, int x, int y) {
        this.addSlot(new CardSlotItemHandler(this.container, id, x, y));
    }

    public void addResultSlot(int id, int x, int y) {
        this.addSlot(new ResultSlotItemHandler(this.container, id, x, y));
    }

    public void addInventory(int x, int y) {
        // add inventory
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(this.inventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }
        // add hotbar
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(this.inventory, k, x + k * 18, y + 58));
        }
    }

    public ItemStack quickMoveStack(@NotNull Player player, int id) {
        int invSt = this.outputSlotID + 1;
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(id);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (id >= this.outputSlotID) {
                if (!this.moveItemStackTo(itemstack1, invSt, 36 + invSt, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (id > invSt) { // move from inventory
                if (id < 28 + invSt) { // move to hotbar if can't move to machine
                    if (!this.moveItemStackTo(itemstack1, 28 + invSt, 36 + invSt, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (id >= 28 + invSt && id < 36 + invSt && !this.moveItemStackTo(itemstack1, invSt, invSt + 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, invSt, 36 + invSt, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    // misc
    public GatchaMachineBE getBE() {
        return this.blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.blockEntity.stillValid(player);
    }

    public int getProcessProgress() {
        int current = this.data.get(0);
        int max = this.data.get(1);
        return max != 0 && current != 0 ? current * 24 / max : 0;
    }
}