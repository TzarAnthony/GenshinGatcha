package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GGPotions {
    public static final DeferredRegister<Potion> reg = DeferredRegister.create(ForgeRegistries.POTIONS, GenshinGacha.MOD_ID);

    public static final RegistryObject<Potion> CRYO = reg.register("cryo", () -> new Potion(new MobEffectInstance(GGEffects.CRYO.get(), 600)));
    public static final RegistryObject<Potion> PYRO = reg.register("pyro", () -> new Potion(new MobEffectInstance(GGEffects.PYRO.get(), 600)));
    public static final RegistryObject<Potion> ELECTRO = reg.register("electro", () -> new Potion(new MobEffectInstance(GGEffects.ELECTRO.get(), 600)));
    public static final RegistryObject<Potion> GEO = reg.register("geo", () -> new Potion(new MobEffectInstance(GGEffects.GEO.get(), 600)));
    public static final RegistryObject<Potion> HYDRO = reg.register("hydro", () -> new Potion(new MobEffectInstance(GGEffects.HYDRO.get(), 600)));
    public static final RegistryObject<Potion> DENDRO = reg.register("dendro", () -> new Potion(new MobEffectInstance(GGEffects.DENDRO.get(), 600)));
    public static final RegistryObject<Potion> ANEMO = reg.register("anemo", () -> new Potion(new MobEffectInstance(GGEffects.ANEMO.get(), 600)));
}