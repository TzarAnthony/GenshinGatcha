package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ElectroSlime extends AbstractElementalSlime {
    public ElectroSlime(EntityType<? extends AbstractElementalSlime> type, Level level) {
        super(type, level);
        this.setElement(Element.E.ELECTRO.getId());
    }

    protected ParticleOptions getParticleType() {
        return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.CRYING_OBSIDIAN));
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected void addExtraEffects(LivingEntity le) {
        le.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
    }

    protected boolean slimeChargeActivity(int chargeTime, LivingEntity tgt) {
        return true;
    }
}