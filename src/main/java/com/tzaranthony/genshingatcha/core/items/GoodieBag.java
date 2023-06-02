package com.tzaranthony.genshingatcha.core.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GoodieBag extends Item {
    private static final String ITEMS = "Items";
    private static final int BUNDLE_IN_BUNDLE_WEIGHT = 4;
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public GoodieBag(Item.Properties properties) {
        super(properties);
    }

    public static float getFullnessDisplay(ItemStack stack) {
        return (float) getContentWeight(stack) / 10.0F;
    }

    public boolean overrideStackedOnOther(ItemStack bagStack, Slot slot, ClickAction click, Player player) {
        if (click != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack inStack = slot.getItem();
            if (inStack.isEmpty()) {
                this.playRemoveOneSound(player);
                removeOne(bagStack).ifPresent((p_150740_) -> {
                    add(bagStack, slot.safeInsert(p_150740_));
                });
            }
            return true;
        }
    }

    public boolean overrideOtherStackedOnMe(ItemStack bagStack, ItemStack inStack, Slot slot, ClickAction click, Player player, SlotAccess access) {
        if (click == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (inStack.isEmpty()) {
                removeOne(bagStack).ifPresent((p_186347_) -> {
                    this.playRemoveOneSound(player);
                    access.set(p_186347_);
                });
            }
            return true;
        } else {
            return false;
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (dropContents(itemstack, player)) {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public void inventoryTick(ItemStack stack, Level level, Entity owner, int tick, boolean selected) {
        if (getContentWeight(stack) == 0) {
            stack.shrink(1);
        }
    }

    public boolean isBarVisible(ItemStack p_150769_) {
        return getContentWeight(p_150769_) > 0;
    }

    public int getBarWidth(ItemStack p_150771_) {
        return Math.min(1 + 12 * getContentWeight(p_150771_) / 10, 13);
    }

    public int getBarColor(ItemStack p_150773_) {
        return BAR_COLOR;
    }

    public static int add(ItemStack bagStack, ItemStack inStack) {
        if (!inStack.isEmpty() && inStack.getItem().canFitInsideContainerItems()) {
            CompoundTag compoundtag = bagStack.getOrCreateTag();
            if (!compoundtag.contains(ITEMS)) {
                compoundtag.put(ITEMS, new ListTag());
            }

            int i = getContentWeight(bagStack);
            int j = getWeight(inStack);
            int k = Math.min(inStack.getCount(), (10 - i) / j);
            if (k == 0) {
                return 0;
            } else {
                ListTag listtag = compoundtag.getList(ITEMS, 10);
                Optional<CompoundTag> optional = getMatchingItem(inStack, listtag);
                if (optional.isPresent()) {
                    CompoundTag compoundtag1 = optional.get();
                    ItemStack itemstack = ItemStack.of(compoundtag1);
                    itemstack.grow(k);
                    itemstack.save(compoundtag1);
                    listtag.remove(compoundtag1);
                    listtag.add(0, (Tag)compoundtag1);
                } else {
                    ItemStack itemstack1 = inStack.copy();
                    itemstack1.setCount(k);
                    CompoundTag compoundtag2 = new CompoundTag();
                    itemstack1.save(compoundtag2);
                    listtag.add(0, (Tag)compoundtag2);
                }

                return k;
            }
        } else {
            return 0;
        }
    }

    private static Optional<CompoundTag> getMatchingItem(ItemStack p_150757_, ListTag p_150758_) {
        return p_150757_.is(Items.BUNDLE) ? Optional.empty() : p_150758_.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter((p_186350_) -> {
            return ItemStack.isSameItemSameTags(ItemStack.of(p_186350_), p_150757_);
        }).findFirst();
    }

    private static int getWeight(ItemStack inStack) {
        return 1;
    }

    private static int getContentWeight(ItemStack bagStack) {
        return getContents(bagStack).mapToInt((contentStack) -> {
            return getWeight(contentStack) * contentStack.getCount();
        }).sum();
    }

    private static Optional<ItemStack> removeOne(ItemStack bagStack) {
        CompoundTag compoundtag = bagStack.getOrCreateTag();
        if (!compoundtag.contains(ITEMS)) {
            return Optional.empty();
        } else {
            ListTag listtag = compoundtag.getList(ITEMS, 10);
            if (listtag.isEmpty()) {
                return Optional.empty();
            } else {
                int i = 0;
                CompoundTag compoundtag1 = listtag.getCompound(0);
                ItemStack itemstack = ItemStack.of(compoundtag1);
                listtag.remove(0);
                if (listtag.isEmpty()) {
                    bagStack.removeTagKey(ITEMS);
                }

                return Optional.of(itemstack);
            }
        }
    }

    private static boolean dropContents(ItemStack bagStack, Player player) {
        CompoundTag compoundtag = bagStack.getOrCreateTag();
        if (!compoundtag.contains(ITEMS)) {
            return false;
        } else {
            if (player instanceof ServerPlayer) {
                ListTag listtag = compoundtag.getList(ITEMS, 10);

                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundtag1 = listtag.getCompound(i);
                    ItemStack itemstack = ItemStack.of(compoundtag1);
                    player.drop(itemstack, true);
                }
            }
            bagStack.removeTagKey(ITEMS);
            return true;
        }
    }

    private static Stream<ItemStack> getContents(ItemStack bagStack) {
        CompoundTag compoundtag = bagStack.getTag();
        if (compoundtag == null) {
            return Stream.empty();
        } else {
            ListTag listtag = compoundtag.getList(ITEMS, 10);
            return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack bagStack) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        getContents(bagStack).forEach(nonnulllist::add);
        return Optional.of(new BundleTooltip(nonnulllist, getContentWeight(bagStack)));
    }

    public void appendHoverText(ItemStack bagStack, Level level, List<Component> components, TooltipFlag flag) {
        components.add((new TranslatableComponent("item.minecraft.bundle.fullness", getContentWeight(bagStack), 64)).withStyle(ChatFormatting.GRAY));
    }

    public void onDestroyed(ItemEntity ie) {
        ItemUtils.onContainerDestroyed(ie, getContents(ie.getItem()));
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }
}