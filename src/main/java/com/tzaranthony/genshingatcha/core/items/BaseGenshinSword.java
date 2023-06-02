package com.tzaranthony.genshingatcha.core.items;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class BaseGenshinSword extends SwordItem {
    protected static final String ELEMENT_COOLDOWN = "elemental_attack_charge";

    public BaseGenshinSword(Tier tier, int dmg, float cooldown, Properties properties) {
        super(tier, dmg, cooldown, properties);
    }
}