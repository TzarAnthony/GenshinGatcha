package com.tzaranthony.genshingatcha.core.util.events;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.networks.ExtendAttackRangeC2SPacket;
import com.tzaranthony.genshingatcha.core.util.tags.GGItemTags;
import com.tzaranthony.genshingatcha.registries.GGPackets;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenshinGacha.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GGServerEvents {

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