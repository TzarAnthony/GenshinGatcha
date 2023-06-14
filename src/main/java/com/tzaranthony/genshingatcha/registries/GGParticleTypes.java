package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GGParticleTypes {
    public static final DeferredRegister<ParticleType<?>> reg = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, GenshinGacha.MOD_ID);

    public static final RegistryObject<SimpleParticleType> MOVING_FIRE = reg.register("moving_fire", ()-> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> MOVING_DUST = reg.register("moving_dust", ()-> new SimpleParticleType(false));
}