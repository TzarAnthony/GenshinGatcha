package com.tzaranthony.genshingatcha.core.container;

import com.tzaranthony.genshingatcha.registries.GGItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CardSlotItemHandler extends SlotItemHandler {
    public CardSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(GGItems.PRIMO_CARD.get());
    }
}