package com.tzaranthony.genshingatcha.core.entities.elements.mobs;

import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.ElementalArrow;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.effects.ElementEffectInstance;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class PoisonDendroSeed extends ElementalArrow implements ItemSupplier {
    public PoisonDendroSeed(EntityType<? extends PoisonDendroSeed> entityType, Level level) {
        super(entityType, level);
    }

    public PoisonDendroSeed(LivingEntity owner, Level level) {
        super(GGEntities.DENDRO_SLIME_SEED.get(), owner, Element.E.DENDRO.getId(), level);
        this.setSoundEvent(SoundEvents.FUNGUS_BREAK);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity tgt) {
        tgt.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1));
        super.doPostHurtEffects(tgt);
    }

    protected void doPostHitEffects(BlockPos pos, Direction dir) {
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaeffectcloud.setOwner((LivingEntity)entity);
        }
        areaeffectcloud.setRadius(2.0F);
        areaeffectcloud.setRadiusOnUse(-0.25F);
        areaeffectcloud.setWaitTime(5);
        areaeffectcloud.setDuration(160);
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
        areaeffectcloud.addEffect(new ElementEffectInstance(this.getElement(), 150));
        areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1));
        this.level.addFreshEntity(areaeffectcloud);
    }

    public ItemStack getItem() {
        return new ItemStack(Items.SLIME_BALL);
    }
}