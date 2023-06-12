package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class DendroSlime extends AbstractElementalSlime {
    public DendroSlime(EntityType<? extends AbstractElementalSlime> type, Level level) {
        super(type, level);
        this.setElement(Element.E.DENDRO.getId());
    }

    protected ParticleOptions getParticleType() {
        return ParticleTypes.SPORE_BLOSSOM_AIR;
    }
}