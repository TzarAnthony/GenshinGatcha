package com.tzaranthony.genshingatcha.core.items.util;

public enum GGBowMaterial {
    ONE(3072, 0.0F, 30.0F, 160),
    TWO(4096, 1.0F, 25.0F, 120),
    THREE(5120, 2.0F, 20.0F, 80),
    FOUR(6144, 4.0F, 15.0F, 60),
    FIVE(7168, 6.0F, 10.0F, 40);

    private final int maxUses;
    private final float drawSpeed;
    private final float attackDamage;
    private final int cooldown;

    GGBowMaterial(int maxUses, float attackDamage, float drawSpeed, int cooldown) {
        this.maxUses = maxUses;
        this.attackDamage = attackDamage;
        this.drawSpeed = drawSpeed;
        this.cooldown = cooldown;
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

    public int getCooldown() {
        return cooldown;
    }
}