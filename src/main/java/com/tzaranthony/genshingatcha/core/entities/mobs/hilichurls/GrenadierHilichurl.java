package com.tzaranthony.genshingatcha.core.entities.mobs.hilichurls;

import com.tzaranthony.genshingatcha.core.entities.projectiles.ExplodingThrownPotion;
import com.tzaranthony.genshingatcha.core.util.Element;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GrenadierHilichurl extends AbstractHilichurl implements RangedAttackMob {
    public GrenadierHilichurl(EntityType<? extends AbstractHilichurl> type, Level level) {
        super(type, level);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.FOLLOW_RANGE, 35.0D);
    }

    @Override
    public void performRangedAttack(LivingEntity tgt, float dmg) {
        Vec3 vec3 = tgt.getDeltaMovement();
        double d0 = tgt.getX() + vec3.x - this.getX();
        double d1 = tgt.getEyeY() - (double)1.1F - this.getY();
        double d2 = tgt.getZ() + vec3.z - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        Potion potion = Element.ElementGetter.get(this.getElement()).getPotion();
        ExplodingThrownPotion etp = new ExplodingThrownPotion(this.level, this, this.getElement());
        etp.setItem(PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), potion));
        etp.setXRot(etp.getXRot() - -20.0F);
        etp.shoot(d0, d1 + d3 * 0.2D, d2, 0.75F, 8.0F);
        if (!this.isSilent()) {
            this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        }
        this.level.addFreshEntity(etp);
    }
}