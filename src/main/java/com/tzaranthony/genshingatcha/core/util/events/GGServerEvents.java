package com.tzaranthony.genshingatcha.core.util.events;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.capabilities.CharacterHelper;
import com.tzaranthony.genshingatcha.core.capabilities.CharacterProvider;
import com.tzaranthony.genshingatcha.core.capabilities.CharacterServer;
import com.tzaranthony.genshingatcha.core.networks.ExtendAttackRangeC2SPacket;
import com.tzaranthony.genshingatcha.core.util.tags.GGItemTags;
import com.tzaranthony.genshingatcha.registries.GGPackets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenshinGacha.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GGServerEvents {
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(CharacterServer.class);
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(CharacterProvider.CHARACTER).isPresent()) {
                event.addCapability(new ResourceLocation(GenshinGacha.MOD_ID, "element"), new CharacterProvider());

            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if(!event.getWorld().isClientSide() && event.getEntity() instanceof ServerPlayer sPlayer) {
            sPlayer.getCapability(CharacterProvider.CHARACTER).ifPresent(ele -> {
                ele.sendPacket(sPlayer);
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath() && event.getPlayer() instanceof ServerPlayer sPlayer) {
            event.getOriginal().getCapability(CharacterProvider.CHARACTER).ifPresent(oldEle -> {
                event.getOriginal().getCapability(CharacterProvider.CHARACTER).ifPresent(newEle -> {
                    newEle.setAll(oldEle.getCharacter(), oldEle.getConstRank(), oldEle.getMainTicks(), oldEle.getUltTicks(), oldEle.getDashTicks(), sPlayer);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer sPlayer) {
            CharacterHelper.tickCooldowns(sPlayer);
        }
    }

    @SubscribeEvent
    public static void onAttackEntityEvent(AttackEntityEvent event) {
        Entity attacker = event.getEntity();
        Entity target = event.getTarget();
        if (!attacker.level.isClientSide() || !(attacker instanceof Player) || attacker.distanceToSqr(target) < 36) {
            return;
        }
        Player player = (Player) attacker;
        ItemStack holding = player.getMainHandItem();
        if ((holding.is(GGItemTags.CLAYMORES) || holding.is(GGItemTags.SPEARS)))
            GGPackets.sendToServer(new ExtendAttackRangeC2SPacket(target.getId()));
    }
}