package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.core.items.util.IAttackReachExtending;
import net.minecraft.world.item.Tier;

public class Spear extends BaseGenshinSword implements IAttackReachExtending {
    private final float attackReach;

    public Spear(Tier tier, float reach, Properties properties) {
        super(tier, 1, -2.8F, properties);
        this.attackReach = reach;
    }

    @Override
    public float getAttackReach() {
        return this.attackReach;
    }
}