package com.tzaranthony.genshingatcha.core.util;

import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import com.tzaranthony.genshingatcha.core.util.tags.GGEntityTags;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import com.tzaranthony.genshingatcha.registries.GGPotions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potion;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Element {
    public static final Map<Integer, E> ElementGetter = new HashMap<>() {{
        put(E.CRYO.getId(), E.CRYO);
        put(E.PYRO.getId(), E.PYRO);
        put(E.ELECTRO.getId(), E.ELECTRO);
        put(E.GEO.getId(), E.GEO);
        put(E.HYDRO.getId(), E.HYDRO);
        put(E.DENDRO.getId(), E.DENDRO);
        put(E.ANEMO.getId(), E.ANEMO);
    }};

    public enum E {
        CRYO(0, GGDamageSource.CRYO, GGEffects.CRYO, GGPotions.CRYO, ParticleTypes.SNOWFLAKE, GGEntityTags.NO_CRYO),
        PYRO(1, GGDamageSource.PYRO, GGEffects.PYRO, GGPotions.PYRO, ParticleTypes.LAVA, GGEntityTags.NO_PYRO),
        ELECTRO(2, GGDamageSource.ELECTRO, GGEffects.ELECTRO, GGPotions.ELECTRO, ParticleTypes.ELECTRIC_SPARK, GGEntityTags.NO_ELECTRO),
        GEO(3, GGDamageSource.GEO, GGEffects.GEO, GGPotions.GEO, ParticleTypes.MYCELIUM, GGEntityTags.NO_GEO),
        HYDRO(4, GGDamageSource.HYDRO, GGEffects.HYDRO, GGPotions.HYDRO, ParticleTypes.BUBBLE, GGEntityTags.NO_HYDRO),
        DENDRO(5, GGDamageSource.DENDRO, GGEffects.DENDRO, GGPotions.DENDRO, ParticleTypes.COMPOSTER, GGEntityTags.NO_DENDRO),
        ANEMO(6, GGDamageSource.ANEMO, GGEffects.ANEMO, GGPotions.ANEMO, ParticleTypes.SPIT, GGEntityTags.NO_ANEMO);

        private final int id;
        private final DamageSource source;
        private final Supplier<MobEffect> effect;
        private final Supplier<Potion> potion;
        private final SimpleParticleType particle;
        private final TagKey<EntityType<?>> immuneEntities;

        E(int id, DamageSource source, Supplier<MobEffect> effect, Supplier<Potion> potion, SimpleParticleType particle, TagKey<EntityType<?>> immuneEntities) {
            this.id = id;
            this.source = source;
            this.effect = effect;
            this.potion = potion;
            this.particle = particle;
            this.immuneEntities = immuneEntities;
        }

        public int getId() {
            return id;
        }

        public DamageSource getDamage() {
            return source;
        }

        public MobEffect getEffect() {
            return effect.get();
        }

        public Potion getPotion() {
            return potion.get();
        }

        public SimpleParticleType getParticle() {
            return particle;
        }

        public boolean isEntityImmune(LivingEntity le) {
            return le.getType().is(immuneEntities);
        }
    }
}