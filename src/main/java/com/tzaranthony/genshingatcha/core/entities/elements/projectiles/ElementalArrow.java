package com.tzaranthony.genshingatcha.core.entities.elements.projectiles;

import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ElementalArrow extends AbstractArrowLikeElementalProjectile {
    public ElementalArrow(EntityType<? extends ElementalArrow> entityType, Level level) {
        super(entityType, level);
    }

    public ElementalArrow(LivingEntity owner, Element.E element, Level level) {
        this(owner, element.getId(), level);
    }

    public ElementalArrow(EntityType<? extends ElementalArrow> entityType, LivingEntity owner, int element, Level level) {
        super(entityType, owner, element, level);
    }

    public ElementalArrow(LivingEntity owner, int element, Level level) {
        super(GGEntities.ELEMENTAL_ARROW.get(), owner, element, level);
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            Vec3 vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            if (this.isCritArrow()) {
                for(int i = 0; i < 4; ++i) {
                    this.level.addParticle(Element.ElementGetter.get(this.getElement()).getParticle(), this.getX() + d5 * (double)i / 4.0D, this.getY() + d6 * (double)i / 4.0D, this.getZ() + d1 * (double)i / 4.0D, -d5, -d6 + 0.2D, -d1);
                }
            }
        }
    }

    protected void doPostHitEffects(BlockPos pos, Direction dir) {
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY() - 0.3D, this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaeffectcloud.setOwner((LivingEntity)entity);
        }
        areaeffectcloud.setRadius(3.5F);
        areaeffectcloud.setRadiusOnUse(-0.5F);
        areaeffectcloud.setWaitTime(5);
        areaeffectcloud.setDuration(40);
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
        areaeffectcloud.addEffect(new MobEffectInstance(Element.ElementGetter.get(this.getElement()).getEffect(), 100));
        this.level.addFreshEntity(areaeffectcloud);
    }
}