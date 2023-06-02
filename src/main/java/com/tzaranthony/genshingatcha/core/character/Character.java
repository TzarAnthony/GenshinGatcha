package com.tzaranthony.genshingatcha.core.character;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

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

    public abstract Element getElement();

    public static final Map<Integer, Element> ElementGetter = new HashMap<>() {{
        put(Element.CRYO.getId(), Element.CRYO);
        put(Element.PYRO.getId(), Element.PYRO);
        put(Element.ELECTRO.getId(), Element.ELECTRO);
        put(Element.GEO.getId(), Element.GEO);
        put(Element.HYDRO.getId(), Element.HYDRO);
        put(Element.DENDRO.getId(), Element.DENDRO);
        put(Element.ANEMO.getId(), Element.ANEMO);
    }};

    public static enum Element {
        CRYO(0),
        PYRO(1),
        ELECTRO(2),
        GEO(3),
        HYDRO(4),
        DENDRO(5),
        ANEMO(6);

        private final int id;

        Element(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}