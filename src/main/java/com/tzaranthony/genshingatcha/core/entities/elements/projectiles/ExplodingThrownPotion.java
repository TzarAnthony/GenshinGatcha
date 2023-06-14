package com.tzaranthony.genshingatcha.core.entities.elements.projectiles;

import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ExplodingThrownPotion extends ThrownPotion {
    protected int element;

    public ExplodingThrownPotion(EntityType<? extends ThrownPotion> type, Level level) {
        super(type, level);
    }

    public ExplodingThrownPotion(Level level, LivingEntity entity, int element) {
        super(GGEntities.EXPLODING_POTION.get(), level);
        setOwner(entity);
        this.element = element;
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.element = tag.getInt("Element");
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Element", this.element);
    }

    // add elemental damage?
    protected void onHitBlock(BlockHitResult result) {
        this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 3.0F, Explosion.BlockInteraction.NONE);
        super.onHitBlock(result);
    }

    protected void onHit(HitResult result) {
        this.doElementalExplosion();
        this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 3.0F, Explosion.BlockInteraction.NONE);
        super.onHit(result);
    }

    public void doElementalExplosion() {
        float f = 8.0F;
        float distance = 3.0F;
        Vec3 vec3 = this.position();

        for(Mob le : this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(distance))) {
            if (!(this.distanceToSqr(le) > 25.0D)) {
                boolean flag = false;

                for(int i = 0; i < 2; ++i) {
                    Vec3 vec31 = new Vec3(le.getX(), le.getY(0.5D * (double)i), le.getZ());
                    HitResult hitresult = this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    if (hitresult.getType() == HitResult.Type.MISS) {
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    float f1 = f * (float)Math.sqrt((distance - (double) this.distanceTo(le)) / distance);
                    le.hurt(Element.ElementGetter.get(this.element).getDamage(), f1);
                }
            }
        }
    }
}