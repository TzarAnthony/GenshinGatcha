package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.networks.CharacterC2SPacket;
import com.tzaranthony.genshingatcha.core.networks.CharacterS2CPacket;
import com.tzaranthony.genshingatcha.core.networks.ExtendAttackRangeC2SPacket;
import com.tzaranthony.genshingatcha.core.networks.ItemS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Iterator;

public class GGPackets {
    private static SimpleChannel net;
    private static int id = 0;
    private static int nextId() {
        return id++;
    }

    public static void registerPackets() {
        net = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(GenshinGacha.MOD_ID, "network"), () -> "1.0", v -> true, v -> true
        );

        net.messageBuilder(CharacterC2SPacket.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CharacterC2SPacket::new)
                .encoder(CharacterC2SPacket::write)
                .consumer(CharacterC2SPacket::handle)
                .add();

        net.messageBuilder(ExtendAttackRangeC2SPacket.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ExtendAttackRangeC2SPacket::new)
                .encoder(ExtendAttackRangeC2SPacket::write)
                .consumer(ExtendAttackRangeC2SPacket::handle)
                .add();

        net.messageBuilder(CharacterS2CPacket.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CharacterS2CPacket::new)
                .encoder(CharacterS2CPacket::write)
                .consumer(CharacterS2CPacket::handle)
                .add();

        net.messageBuilder(ItemS2CPacket.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemS2CPacket::new)
                .encoder(ItemS2CPacket::write)
                .consumer(ItemS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        net.sendToServer(message);
    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        Iterator players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().iterator();

        while(players.hasNext()) {
            ServerPlayer player = (ServerPlayer) players.next();
            sendToPlayer(message, player);
        }
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        net.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendToClients(MSG message) {
        net.send(PacketDistributor.ALL.noArg(), message);
    }
}