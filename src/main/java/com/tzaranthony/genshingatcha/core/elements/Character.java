package com.tzaranthony.genshingatcha.core.elements;

import net.minecraft.world.entity.player.Player;

public abstract class Character {
    protected final int characterID;
    protected final int mainCooldown;
    protected final int ultCooldown;

    protected Character(int elementID, int mainCooldown, int ultCooldown) {
        this.characterID = elementID;
        this.mainCooldown = mainCooldown;
        this.ultCooldown = ultCooldown;
    }

    public int getCharacterID() {
        return this.characterID;
    }

    public int getMainCooldown(int constRank) {
        return this.mainCooldown;
    }

    public abstract void performMainAttack(Player player);

    public int getUltCooldown(int constRank) {
        return this.ultCooldown;
    }

    public abstract void performUltimateAttack(Player player);

    public abstract void applyConstellationAttributes(Player player, int constRank);
}