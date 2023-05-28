package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.registries.GGItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class PrimoCard extends Item {
    public static final String STORAGE = "PrimosStored";
    private static final int maxPrimos = 131072;
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public PrimoCard(Item.Properties properties) {
        super(properties);
    }

    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack inStack = slot.getItem();
            if (inStack.is(GGItems.PRIMOGEM.get())) {
                int i = calulatePrimoStorage(stack, inStack.getCount());
                if (i > 0) {
                    this.playInsertSound(player);
                    inStack.shrink(i);
                }
            }
            return true;
        }
    }

    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack inStack, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (inStack.is(GGItems.PRIMOGEM.get())) {
                int i = calulatePrimoStorage(stack, inStack.getCount());
                if (i > 0) {
                    this.playInsertSound(player);
                    inStack.shrink(i);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isBarVisible(ItemStack stack) {
        return getPrimoCount(stack) > 0;
    }

    public int getBarWidth(ItemStack stack) {
        return Math.min(1 + 12 * getPrimoCount(stack) / maxPrimos, 13);
    }

    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    public static int getPrimoCount(ItemStack stack) {
        return stack.getOrCreateTag().getInt(STORAGE);
    }

    public static int calulatePrimoStorage(ItemStack stack, int change) {
        CompoundTag tag = stack.getOrCreateTag();
        int cPrimos = tag.getInt(STORAGE);
        if (cPrimos + change > maxPrimos) {
            tag.putInt(STORAGE, maxPrimos);
            return maxPrimos - cPrimos;
        }
        tag.putInt(STORAGE, cPrimos + change);
        return change;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TextComponent(String.valueOf(getPrimoCount(stack))).append(new TranslatableComponent("tooltip.genshingatcha.primo_card")));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    private void playInsertSound(Entity p_186352_) {
        p_186352_.playSound(SoundEvents.ARMOR_EQUIP_CHAIN, 0.8F, 0.8F + p_186352_.getLevel().getRandom().nextFloat() * 0.4F);
    }
}