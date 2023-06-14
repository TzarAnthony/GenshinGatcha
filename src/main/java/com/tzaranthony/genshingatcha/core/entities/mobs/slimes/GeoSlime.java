package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.ElementalArrow;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.registries.GGParticleTypes;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class GeoSlime extends AbstractElementalSlime {
    public GeoSlime(EntityType<? extends AbstractElementalSlime> type, Level level) {
        super(type, level);
        this.setElement(Element.E.GEO.getId());
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isCharging()) {
            if (this.level.isClientSide) {
                for(int i = 0; i < 5; ++i) {
                    double d0 = this.getX() + this.random.nextDouble();
                    double d1 = this.getY() + this.random.nextDouble();
                    double d2 = this.getZ() + this.random.nextDouble();
                    double d3 = (this.random.nextDouble() - 0.5D) * 0.5D;
                    double d4 = (this.random.nextDouble() - 0.5D) * 0.5D;
                    double d5 = (this.random.nextDouble() - 0.5D) * 0.5D;
                    int k = this.random.nextInt(2) * 2 - 1;
                    if (this.random.nextBoolean()) {
                        d2 = this.getZ() + 0.5D + 0.25D * (double)k;
                        d5 =(this.random.nextFloat() * 2.0F * (float)k);
                    } else {
                        d0 = this.getX() + 0.5D + 0.25D * (double)k;
                        d3 = (this.random.nextFloat() * 2.0F * (float)k);
                    }

                    this.level.addParticle(GGParticleTypes.MOVING_DUST.get(), d0, d1, d2, d3, d4, d5);
                }
            }
        }
    }

    protected ParticleOptions getParticleType() {
        return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.TUFF));
    }

    protected void setSize(int size, boolean resetHealth) {
        super.setSize(size, resetHealth);
        this.getAttribute(Attributes.ARMOR).setBaseValue((size * 5) + 2.0D);
    }

    protected boolean slimeChargeActivity(int chargeTime, LivingEntity tgt) {
        boolean isEnd = chargeTime >= 40;
        if (isEnd) {
            // replace with a rock?
            ElementalArrow arrow = new ElementalArrow(this, this.getElement(), this.level);
            arrow.setPos(this.getX(), this.getEyeY() + 1.0F, this.getZ());
            double d0 = tgt.getX() - this.getX();
            double d1 = tgt.getY(0.3333333333333333D) - arrow.getY();
            double d2 = tgt.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            arrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(13 - this.level.getDifficulty().getId() * 4));
            this.playSound(SoundEvents.SHULKER_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(arrow);
        }
        return isEnd;
    }
}