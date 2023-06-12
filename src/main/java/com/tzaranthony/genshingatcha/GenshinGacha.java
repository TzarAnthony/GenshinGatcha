package com.tzaranthony.genshingatcha;

import com.tzaranthony.genshingatcha.core.util.events.GGClientEvents;
import com.tzaranthony.genshingatcha.core.util.events.GGServerEvents;
import com.tzaranthony.genshingatcha.registries.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("genshingatcha")
public class GenshinGacha {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "genshingatcha";
    public static boolean isUsingMixin;

    public GenshinGacha() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::CommonSetup);
        bus.addListener(this::ClientSetup);
        bus.addListener(this::CompleteSetup);

        GGBlocks.reg.register(bus);
        GGEntities.reg.register(bus);
        GGItems.reg.register(bus);
        GGBlockEntities.reg.register(bus);
//        GGSounds.reg.register(bus);
//        GGParticleTypes.reg.register(bus);
        GGEffects.reg.register(bus);
        GGPotions.reg.register(bus);
        GGMenus.reg.register(bus);
        GGVillagers.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new GGServerEvents());

//        GeckoLib.initialize();
    }

    private void CommonSetup(final FMLCommonSetupEvent event) {
        GGPackets.registerPackets();
    }

    private void ClientSetup(final FMLClientSetupEvent event) {
        GGKeybinds.registerKeybinds();
        GGEntityRender.renderEntities();
        GGItemsRender.renderItemProperties();
        GGScreenRender.renderScreens();
        MinecraftForge.EVENT_BUS.register(new GGClientEvents());
    }

    private void CompleteSetup(final FMLLoadCompleteEvent event) {
        GGVillagers.registerPOIs();
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("GenshinGatcha") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(GGItems.PRIMOGEM.get());
        }
    };
}