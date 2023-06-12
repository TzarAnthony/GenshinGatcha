package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.Level;

public class CryoSlime extends AbstractElementalSlime {
    public CryoSlime(EntityType<? extends AbstractElementalSlime> type, Level level) {
        super(type, level);
        this.setElement(Element.E.CRYO.getId());
    }

    protected ParticleOptions getParticleType() {
        return ParticleTypes.ITEM_SNOWBALL;
    }

    public boolean canFreeze() {
        return false;
    }

    @Override
    protected void onChangedBlock(BlockPos pos) {
        if (this.getElement() == Element.E.CRYO.getId()) {
            FrostWalkerEnchantment.onEntityMoved(this, this.level, pos, 1);
        }
    }
}