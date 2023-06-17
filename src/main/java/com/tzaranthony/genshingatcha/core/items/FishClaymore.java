package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.core.items.util.GGToolMaterial;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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
        EntityType type = switch (user.level.random.nextInt(4)) {
            case 0 -> EntityType.COD;
            case 1 -> EntityType.SALMON;
            case 2 -> EntityType.TROPICAL_FISH;
            default -> EntityType.PUFFERFISH;
        };

        if (target instanceof Mob tgt) {
            tgt.convertTo(type, false);
        } else {
            Entity fish = type.create(user.level);
            user.level.addFreshEntity(fish);
            fish.moveTo(target.position());
            if (user instanceof Player player) {
                target.hurt(DamageSource.playerAttack(player), Float.MAX_VALUE);
            }
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