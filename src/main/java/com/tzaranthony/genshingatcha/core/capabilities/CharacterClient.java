package com.tzaranthony.genshingatcha.core.capabilities;

import com.tzaranthony.genshingatcha.core.networks.CharacterC2SPacket;
import com.tzaranthony.genshingatcha.registries.GGCharacters;
import com.tzaranthony.genshingatcha.registries.GGPackets;

public class CharacterClient {
    private static int characterID;
    private static int constRank;
    private static int mainTicks;
    private static int ultTicks;
    private static int dashTicks;

    public static void setElementFromServer(int charID) {
        CharacterClient.characterID = charID;
    }

    public static void setConstRankFromServer(int constRank) {
        CharacterClient.constRank = constRank;
    }

    public static void setMainFromServer(int mainTicks) {
        CharacterClient.mainTicks = mainTicks;
    }

    public static void setUltFromServer(int ultTicks) {
        CharacterClient.ultTicks = ultTicks;
    }

    public static void setDashFromServer(int dashTicks) {
        CharacterClient.dashTicks = dashTicks;
    }

    public static int getElement() {
        return characterID;
    }

    public static int getMainTicks() {
        return mainTicks;
    }

    public static int getUltTicks() {
        return ultTicks;
    }

    public static int getDashTicks() {
        return dashTicks;
    }

    public static void resetMainFromUse() {
        CharacterClient.mainTicks = GGCharacters.characterMap.get(characterID).getMainCooldown(constRank);
        GGPackets.sendToServer(new CharacterC2SPacket(CharacterClient.mainTicks, -1, -1));
    }

    public static void resetUltFromUse() {
        CharacterClient.ultTicks = GGCharacters.characterMap.get(characterID).getUltCooldown(constRank);
        GGPackets.sendToServer(new CharacterC2SPacket(-1, CharacterClient.ultTicks, -1));
    }

    public static void resetDashFromUse() {
        CharacterClient.dashTicks = 60;
        GGPackets.sendToServer(new CharacterC2SPacket(-1, -1, CharacterClient.dashTicks));
    }
}