package com.tzaranthony.genshingatcha.registries;

import com.google.common.collect.ImmutableSet;
import com.tzaranthony.genshingatcha.GenshinGacha;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = GenshinGacha.MOD_ID)
public class GGVillagers {
    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, GenshinGacha.MOD_ID);
    public static final DeferredRegister<VillagerProfession> profession = DeferredRegister.create(ForgeRegistries.PROFESSIONS, GenshinGacha.MOD_ID);

    public static final RegistryObject<PoiType> GATCHA_GUY_POI = POI.register("gatcha_guy_poi"
            , () -> new PoiType("gatcha_guy_poi", PoiType.getBlockStates(GGBlocks.GATCHA_MACHINE.get()), 1, 1));
    public static final RegistryObject<VillagerProfession> GATCHA_GUY = profession.register("gatcha_guy"
            , () -> new VillagerProfession("gatcha_guy", GATCHA_GUY_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_SHEPHERD));

    public static void registerPOIs() {
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class)
                    .invoke(null, GATCHA_GUY_POI.get());
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public static void register(IEventBus eventBus) {
        POI.register(eventBus);
        profession.register(eventBus);
    }

    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        if(event.getType() == GGVillagers.GATCHA_GUY.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add(new VillagerBuys(Items.COBBLESTONE, 16, 1, 1000, 1));
            trades.get(1).add(new VillagerBuys(Items.COBBLED_DEEPSLATE, 24, 2, 1000, 1));

            trades.get(2).add(new VillagerBuys(Items.COPPER_INGOT, 16, 3, 10));
            trades.get(2).add(new VillagerBuys(Items.IRON_INGOT, 8, 5, 10));
            trades.get(2).add(new VillagerBuys(Items.EMERALD, 2, 1, 128, 10));

            trades.get(3).add(new VillagerBuys(Items.LAPIS_LAZULI, 8, 3, 15));
            trades.get(3).add(new VillagerBuys(Items.GOLD_INGOT, 4, 5, 15));

            trades.get(4).add(new VillagerBuys(Items.REDSTONE, 8, 10, 20));
            trades.get(4).add(new VillagerBuys(Items.QUARTZ, 8, 10, 20));

            trades.get(5).add(new VillagerBuys(Items.DIAMOND, 1, 16, 30));
            trades.get(5).add(new VillagerBuys(Items.ANCIENT_DEBRIS, 1, 64, 30));
        }
    }

    static class VillagerBuys implements VillagerTrades.ItemListing {
        private final Item item;
        private final int amount;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public VillagerBuys(ItemLike wants, int amount, int price, int xp) {
            this(wants, amount, price, 64, xp);
        }

        public VillagerBuys(ItemLike wants, int amount, int price, int uses, int xp) {
            this.item = wants.asItem();
            this.amount = amount;
            this.cost = price;
            this.maxUses = uses;
            this.villagerXp = xp;
            this.priceMultiplier = 0.05F;
        }

        public MerchantOffer getOffer(Entity p_35662_, Random p_35663_) {
            return new MerchantOffer(new ItemStack(this.item, this.amount), new ItemStack(GGItems.PRIMOGEM.get(), this.cost), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
}