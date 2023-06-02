package com.tzaranthony.genshingatcha.core.capabilities;

import com.tzaranthony.genshingatcha.core.character.Character;
import com.tzaranthony.genshingatcha.core.networks.CharacterS2CPacket;
import com.tzaranthony.genshingatcha.registries.GGCharacters;
import com.tzaranthony.genshingatcha.registries.GGPackets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class CharacterServer {
    private final String CHARACTER_ID = "element_id";
    private int charID;
    private final String CONST_RANK = "constellation_rank";
    private int constRank;
    private final String MAIN_TICKS = "main_cooldown";
    private int mainTicks;
    private final String ULT_TICKS = "ult_cooldown";
    private int ultTicks;
    private final String DASH_TICKS = "dash_cooldown";
    private int dashTicks;

    public int getCharacter() {
        return charID;
    }

    public int getConstRank() {
        return constRank;
    }

    public int getMainTicks() {
        return mainTicks;
    }

    public int getUltTicks() {
        return ultTicks;
    }

    public int getDashTicks() {
        return dashTicks;
    }

    public void setAll(int charID, int constRank, int mainTicks, int ultTicks, int dashTicks, ServerPlayer player) {
        this.charID = Math.max(charID, 0);
        this.constRank = Math.max(constRank, 0);
        this.mainTicks = Math.max(mainTicks, 0);
        this.ultTicks = Math.max(ultTicks, 0);
        this.dashTicks = Math.max(dashTicks, 0);
        sendPacket(player);
    }

    public void setChar(int charID, int constRank, ServerPlayer player) {
        this.charID = Math.max(charID, 0);
        this.constRank = Math.max(constRank, 0);
        Character e = GGCharacters.characterMap.get(this.charID);
        this.mainTicks = e.getMainCooldown(this.constRank);
        this.ultTicks = e.getUltCooldown(this.constRank);
        sendPacket(player);
    }

    public void setMainCooldown(int mainTicks, ServerPlayer player) {
        this.mainTicks = Math.max(mainTicks, 0);
        sendPacket(player);
    }

    public void setUltCooldown(int ultTicks, ServerPlayer player) {
        this.ultTicks = Math.max(ultTicks, 0);
        sendPacket(player);
    }

    public void setDashCooldown(int dashTicks, ServerPlayer player) {
        this.dashTicks = Math.max(dashTicks, 0);
        sendPacket(player);
    }

    public void tickCooldowns(ServerPlayer player) {
        tickMainCooldown();
        tickUltCooldown();
        tickDashCooldown();
        sendPacket(player);
    }

    protected void tickMainCooldown() {
        if (this.mainTicks > 0) {
            --this.mainTicks;
        }
    }

    protected void tickUltCooldown() {
        if (this.ultTicks > 0) {
            --this.ultTicks;
        }
    }

    protected void tickDashCooldown() {
        if (this.dashTicks > 0) {
            --this.dashTicks;
        }
    }

    public void sendPacket(ServerPlayer player) {
        GGPackets.sendToPlayer(new CharacterS2CPacket(this.charID, this.constRank, this.mainTicks, this.ultTicks, this.dashTicks), player);
    }

    public void saveElementInfo(CompoundTag tag) {
        tag.putInt(CHARACTER_ID, charID);
        tag.putInt(CONST_RANK, constRank);
        tag.putInt(MAIN_TICKS, mainTicks);
        tag.putInt(ULT_TICKS, ultTicks);
        tag.putInt(DASH_TICKS, dashTicks);
    }

    public void loadElementInfo(CompoundTag tag) {
        this.charID = tag.getInt(CHARACTER_ID);
        this.constRank = tag.getInt(CONST_RANK);
        this.mainTicks = tag.getInt(MAIN_TICKS);
        this.ultTicks = tag.getInt(ULT_TICKS);
        this.dashTicks = tag.getInt(DASH_TICKS);
    }
}