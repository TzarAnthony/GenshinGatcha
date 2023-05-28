package com.tzaranthony.genshingatcha.core.container;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ResultSlotItemHandler extends SlotItemHandler {
    public ResultSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack p_39553_) {
        return false;
    }
}