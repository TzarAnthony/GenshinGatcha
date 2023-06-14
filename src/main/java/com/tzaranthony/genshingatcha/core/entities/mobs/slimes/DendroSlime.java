package com.tzaranthony.genshingatcha.core.entities.mobs.slimes;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.ElementalArrow;
import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class DendroSlime extends AbstractElementalSlime {
    public DendroSlime(EntityType<? extends AbstractElementalSlime> type, Level level) {
        super(type, level);
        this.setElement(Element.E.DENDRO.getId());
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }

    protected ParticleOptions getParticleType() {
        return ParticleTypes.SPORE_BLOSSOM_AIR;
    }

    public boolean isShaking() {
        return this.isCharging();
    }

    protected boolean slimeChargeActivity(int chargeTime, LivingEntity tgt) {
        // need something like crossbow multishot, also should leave longer but smaller damaging AOE not just elemental AOE --- ElementalArrow.doPostHitEffects
        boolean isEnd = chargeTime >= 30;
        if (isEnd) {
            for(int i = 0; i < 3; ++i) {
                ElementalArrow arrow = new ElementalArrow(this, this.getElement(), this.level);
                double x = this.getX() + ((double) (i - 1) * 0.25D);
                double y = this.getEyeY() + 1.2F;
                double z = this.getZ() + ((double) (i - 1) * 0.25D);
                arrow.setPos(x, y, z);

                if (i == 0) {
                    shootCrossbowProjectile(tgt, arrow, 0.0F, 1.5F);
                } else if (i == 1) {
                    shootCrossbowProjectile(tgt, arrow, -10.0F, 1.5F);
                } else if (i == 2) {
                    shootCrossbowProjectile(tgt, arrow, 10.0F, 1.5F);
                }

                this.playSound(SoundEvents.SHULKER_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level.addFreshEntity(arrow);
            }
        }
        return isEnd;
    }

    protected void shootCrossbowProjectile(LivingEntity tgt, Projectile prj, float spread, float power) {
        double d0 = tgt.getX() - this.getX();
        double d1 = tgt.getZ() - this.getZ();
        double d2 = Math.sqrt(d0 * d0 + d1 * d1);
        double d3 = tgt.getY(0.3333333333333333D) - prj.getY() + d2 * (double)0.2F;
        Vector3f vector3f = this.getProjectileShotVector(new Vec3(d0, d3, d1), spread);
        prj.shoot(vector3f.x(), vector3f.y(), vector3f.z(), power, (float)(14 - this.level.getDifficulty().getId() * 4));
    }

    protected Vector3f getProjectileShotVector(Vec3 vec, float spread) {
        Vec3 vec3 = vec.normalize();
        Vec3 vec31 = vec3.cross(new Vec3(0.0D, 1.0D, 0.0D));
        if (vec31.lengthSqr() <= 1.0E-7D) {
            vec31 = vec3.cross(this.getUpVector(1.0F));
        }

        Quaternion quaternion = new Quaternion(new Vector3f(vec31), 90.0F, true);
        Vector3f vector3f = new Vector3f(vec3);
        vector3f.transform(quaternion);
        Quaternion quaternion1 = new Quaternion(vector3f, spread, true);
        Vector3f vector3f1 = new Vector3f(vec3);
        vector3f1.transform(quaternion1);
        return vector3f1;
    }
}