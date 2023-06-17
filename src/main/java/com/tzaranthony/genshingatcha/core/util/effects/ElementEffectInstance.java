package com.tzaranthony.genshingatcha.core.util.effects;

import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class ElementEffectInstance extends MobEffectInstance {
    public ElementEffectInstance(MobEffect effect) {
        this(effect, 200, 0);
    }

    public ElementEffectInstance(int id) {
        this(id, 200);
    }

    public ElementEffectInstance(int id, int duration) {
        this(Element.ElementGetter.get(id).getEffect(), duration, 0);
    }

    public ElementEffectInstance(MobEffect effect, int duration, int amplifier) {
        super(effect, duration, amplifier);
    }
}