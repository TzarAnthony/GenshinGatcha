package com.tzaranthony.genshingatcha.core.entities.mobs;

import net.minecraft.world.entity.SpawnGroupData;

public class ElementalGroupData implements SpawnGroupData {
    final int element;

    public ElementalGroupData(int element) {
        this.element = element;
    }
}