package com.tzaranthony.genshingatcha.core.entities.elements.character;

import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.AbstractElementalProjectile;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.damage.GGDamageSource;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FireSliceDiluc extends AbstractElementalProjectile {
    public FireSliceDiluc(EntityType<? extends AbstractElementalProjectile> projectile, Level level) {
        super(projectile, level);
    }

    public FireSliceDiluc(EntityType<? extends AbstractElementalProjectile> projectile, Level level, Entity owner, double x, double y, double z, int constRank) {
        this(projectile, level);
        this.setOwner(owner);
        this.setPos(x, y, z);
        this.setDeltaMovement(this.random.nextGaussian() * 0.001D, 0.05D, this.random.nextGaussian() * 0.001D);
        this.constRank = constRank;
    }

    @Override
    protected void performOnEntity(LivingEntity target, Entity user) {
        float dmg = 10.0F + (this.constRank >= 5 ? 5.0F : 0.0F);
        int dur = this.constRank >= 5 ? 20 : 12;
        target.setSecondsOnFire(dur);
        target.hurt(GGDamageSource.magicElement(this, user, Element.E.PYRO.getId()).setIsFire(), dmg);
        target.addEffect(new MobEffectInstance(GGEffects.PYRO.get(), dur * 20));
    }

    @Override
    protected void performOnBlock(Entity owner, Level level, BlockPos pos) {
    }
}