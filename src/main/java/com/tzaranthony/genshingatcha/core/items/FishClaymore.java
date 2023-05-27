package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.core.items.util.GGToolMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class FishClaymore extends Claymore {
    public FishClaymore(float reach, Properties properties) {
        super(GGToolMaterial.FISH, reach, properties);
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        BlockPos pos = user.getOnPos().relative(user.getDirection(), 3).above(2);
        Direction perpDir = user.getDirection().getClockWise();
        for (int i = 0; i < 4; ++i) {
            LivingEntity le;
            switch (user.level.random.nextInt(4)) {
                case 0:
                    le = new Cod(EntityType.COD, user.level);
                    break;
                case 1:
                    le = new Salmon(EntityType.SALMON, user.level);
                    break;
                case 2:
                    le = new TropicalFish(EntityType.TROPICAL_FISH, user.level);
                    break;
                default:
                    le = new Pufferfish(EntityType.PUFFERFISH, user.level);
                    break;
            }
            user.level.addFreshEntity(le);
            le.moveTo(pos.relative(perpDir, user.level.random.nextInt(7) - 3), 0.0F, 0.0F);
        }
        if (user instanceof Player player) {
            target.hurt(DamageSource.playerAttack(player), Float.MAX_VALUE);
        }
        return super.hurtEnemy(stack, target, user);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("tooltip.genshingatcha.fish"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}