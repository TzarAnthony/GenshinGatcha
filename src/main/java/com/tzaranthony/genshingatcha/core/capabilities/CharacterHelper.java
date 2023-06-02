package com.tzaranthony.genshingatcha.core.capabilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class CharacterHelper {
    // Server Side
    public static void tickCooldowns(ServerPlayer sPlayer) {
        sPlayer.getCapability(CharacterProvider.CHARACTER).ifPresent(ele -> {
            ele.tickCooldowns(sPlayer);
        });
    }

    public static int getCharacter(ServerPlayer sPlayer) {
        if (sPlayer.getCapability(CharacterProvider.CHARACTER).isPresent()) {
            return sPlayer.getCapability(CharacterProvider.CHARACTER).orElse(null).getCharacter();
        }
        return 0;
    }

    public static int getConstRank(ServerPlayer sPlayer) {
        if (sPlayer.getCapability(CharacterProvider.CHARACTER).isPresent()) {
            return sPlayer.getCapability(CharacterProvider.CHARACTER).orElse(null).getConstRank();
        }
        return 0;
    }

    public static void setChar(int charID, int constRank, ServerPlayer sPlayer) {
        sPlayer.getCapability(CharacterProvider.CHARACTER).ifPresent(ele -> {
            ele.setChar(charID, constRank, sPlayer);
        });
    }

    public static void setMainCooldown(int main, ServerPlayer sPlayer) {
        sPlayer.getCapability(CharacterProvider.CHARACTER).ifPresent(ele -> {
            ele.setMainCooldown(main, sPlayer);
        });
    }

    public static void setUltCooldown(int ult, ServerPlayer sPlayer) {
        sPlayer.getCapability(CharacterProvider.CHARACTER).ifPresent(ele -> {
            ele.setUltCooldown(ult, sPlayer);
        });
    }

    public static void setDashCooldown(int dash, ServerPlayer sPlayer) {
        sPlayer.getCapability(CharacterProvider.CHARACTER).ifPresent(ele -> {
            ele.setDashCooldown(dash, sPlayer);
        });
    }

    // Both Sides
    public static int getCharacter(Player player) {
        if (player instanceof ServerPlayer sPlayer) {
            return getCharacter(sPlayer);
        }
        return CharacterClient.getChar();
    }



    // WIP
    public static void syncSizeClient(Player player) {
        int charID = CharacterHelper.getCharacter(player);
        CharacterHelper.syncSize(player, charID);
    }

    public static void syncSize(Player player, int charID) {
        player.refreshDimensions();
    }
}