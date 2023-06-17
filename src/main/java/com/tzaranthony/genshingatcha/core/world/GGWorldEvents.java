package com.tzaranthony.genshingatcha.core.world;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.world.gen.GGEntityGen;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenshinGacha.MOD_ID)
public class GGWorldEvents {
    @SubscribeEvent
    public static void onBiomeLoadEvent(final BiomeLoadingEvent event) {
        GGEntityGen.addEntitiesToBiomes(event);
    }
}