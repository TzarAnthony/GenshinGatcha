package com.tzaranthony.genshingatcha.core.items;

import net.minecraft.world.item.Tier;

public class ElementalSword extends BaseGenshinSword {
    public ElementalSword(Tier tier, float cooldown, Properties properties) {
        super(tier, 3, -2.4F + cooldown, properties);
    }
}