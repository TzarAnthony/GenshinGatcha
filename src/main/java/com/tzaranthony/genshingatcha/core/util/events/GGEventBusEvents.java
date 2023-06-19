package com.tzaranthony.genshingatcha.core.util.events;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.client.blockEntityRenders.GGChestRender;
import com.tzaranthony.genshingatcha.client.particles.MovingParticle;
import com.tzaranthony.genshingatcha.registries.GGParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenshinGacha.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GGEventBusEvents {
    @SubscribeEvent
    public static void registerParticleFactories(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(GGParticleTypes.MOVING_FIRE.get(), MovingParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(GGParticleTypes.MOVING_DUST.get(), MovingParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerAtlasTextures(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
            event.addSprite(GGChestRender.CHALLENGE_CHEST);
        }
    }
}