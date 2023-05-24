package com.tzaranthony.genshingatcha;

import com.tzaranthony.genshingatcha.registries.GGItems;
import com.tzaranthony.spellbook.core.crafting.SBBrewingRecipe;
import com.tzaranthony.spellbook.core.util.events.SBClientEvents;
import com.tzaranthony.spellbook.core.util.events.SBServerEvents;
import com.tzaranthony.spellbook.core.world.features.SBFeatures;
import com.tzaranthony.spellbook.registries.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod("spellbook")
public class GenshinGacha {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "genshingatcha";
    public static boolean isUsingMixin;

    public GenshinGacha() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::CommonSetup);
        bus.addListener(this::ClientSetup);
        bus.addListener(this::CompleteSetup);

        SBBlocks.reg.register(bus);
//        SBEntities.reg.register(bus);
        SBItems.reg.register(bus);
//        SBBlockEntities.reg.register(bus);
//        SBSounds.reg.register(bus);
//        SBParticleTypes.reg.register(bus);
//        SBEffects.reg.register(bus);
//        SBMenus.reg.register(bus);
//        SBVillagers.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
//        MinecraftForge.EVENT_BUS.register(new SBServerEvents());

//        GeckoLib.initialize();
    }

    private void CommonSetup(final FMLCommonSetupEvent event) {
//        SBPackets.registerPackets();
    }

    private void ClientSetup(final FMLClientSetupEvent event) {
//        SBKeybinds.registerKeybinds();
//        SBBlocksRender.renderBlocks();
//        SBBlockEntityRender.renderBlockEntities();
//        SBEntityRender.renderEntities();
//        SBItemsRender.renderItemProperties();
//        SBScreenRender.renderScreens();
//        MinecraftForge.EVENT_BUS.register(new SBClientEvents());
    }

    private void CompleteSetup(final FMLLoadCompleteEvent event) {
//        SBVillagers.registerPOIs();
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("SpellBook") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(GGItems.LIBER_EXPONENTIA_CREATIVE.get());
        }
    };
}