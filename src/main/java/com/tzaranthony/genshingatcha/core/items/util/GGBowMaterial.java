package com.tzaranthony.genshingatcha.core.items.util;

public enum GGBowMaterial {
    ONE(3072, 4.0F, 15, 20),
    TWO(4096, 6.0F, 17, 25),
    THREE(5120, 8.0F, 20, 30),
    FOUR(6144, 10.0F, 25, 35),
    FIVE(7168, 12.0F, 30, 40);

    private final int maxUses;
    private final float drawSpeed;
    private final float attackDamage;
    private final int range;

    GGBowMaterial(int maxUses, float attackDamage, int drawSpeed, int range) {
        this.maxUses = maxUses;
        this.attackDamage = attackDamage;
        this.drawSpeed = drawSpeed;
        this.range = range;
    }

    public int getUses() {
        return maxUses;
    }

    public float getAttackDamageBonus() {
        return attackDamage;
    }

    public float getSpeed() {
        return drawSpeed;
    }

    public int getRange() {
        return range;
    }
}