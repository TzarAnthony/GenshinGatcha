package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.util.effects.FreezingEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GGEffects {
    public static final DeferredRegister<MobEffect> reg = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, GenshinGacha.MOD_ID);

    public static final RegistryObject<MobEffect> FREEZING = reg.register("freezing", ()-> new FreezingEffect(MobEffectCategory.HARMFUL, 9761279)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "7b99b10e-aa3a-4b01-8191-780b0752b50c", (double) -0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
}