package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.entities.elements.mobs.Windball;
import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AnemoSlime extends AbstractElementalSlime {
    public AnemoSlime(EntityType<? extends AbstractElementalSlime> type, Level level) {
        super(type, level);
        this.setElement(Element.E.ANEMO.getId());
    }

    protected ParticleOptions getParticleType() {
        return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.CYAN_WOOL));
    }

    public boolean causeFallDamage(float p_149717_, float p_149718_, DamageSource p_149719_) {
        return false;
    }

    @Override
    protected void startCharge() {
        this.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 80));
    }

    protected boolean slimeChargeActivity(int chargeTime, LivingEntity tgt) {
        if (chargeTime >= 41 && chargeTime % 10 == 0) {
            double d1 = 4.0D;
            Vec3 vec3 = this.getViewVector(1.0F);
            double d2 = tgt.getX() - (this.getX() + vec3.x * d1);
            double d3 = tgt.getY(0.5D) - (0.5D + this.getY(0.5D));
            double d4 = tgt.getZ() - (this.getZ() + vec3.z * d1);
            if (!this.isSilent()) {
                level.levelEvent((Player)null, 1016, this.blockPosition(), 0);
            }

            Windball windball = new Windball(this, level, d2, d3, d4);
            windball.setPos(this.getX() + vec3.x * 4.0D, this.getY(0.5D) + 0.5D, windball.getZ() + vec3.z * 4.0D);
            level.addFreshEntity(windball);
        }
        return chargeTime >= 80;
    }
}