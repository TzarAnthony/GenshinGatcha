package com.tzaranthony.genshingatcha.core.elements;

import com.tzaranthony.genshingatcha.core.capabilities.CharacterHelper;
import com.tzaranthony.genshingatcha.core.entities.elements.FallingMeteor;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.phys.Vec3;

public class Zhongli extends Character {
    public Zhongli(int elementID, int mainCooldown, int ultCooldown) {
        super(elementID, mainCooldown, ultCooldown);
    }

    @Override
    public void performMainAttack(Player player) {
        BlockPos pos = player.blockPosition().relative(player.getDirection(), 2);
        pos = EntityUtil.getFloorInRange(player.level, pos.getX(), pos.below(4).getY(), pos.above(4).getY(), pos.getZ());
        //TODO: replace with obelisk when i get the render
        //TODO: add level? CharacterHelper.getCharacter(sPlayer) >= 3
        player.level.addFreshEntity(new EvokerFangs(player.level, pos.getX(), pos.getY(), pos.getZ(), player.getYRot(), 0, player));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 3));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 4));
        if (player instanceof ServerPlayer sPlayer && CharacterHelper.getConstRank(sPlayer) >= 6) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60));
        }
    }

    @Override
    public int getMainCooldown(int constRank) {
        if (constRank >= 1) {
            return this.mainCooldown - 60;
        }
        return this.ultCooldown;
    }

    @Override
    public void performUltimateAttack(Player player) {
        Vec3 pos = player.pick(5.0D, 0.0F, false).getLocation();
        //TODO: add shield for players in area CharacterHelper.getCharacter(sPlayer) >= 2
        //TODO: increase AOE range by 20% CharacterHelper.getCharacter(sPlayer) >= 4
        //TODO: add level? CharacterHelper.getCharacter(sPlayer) >= 5
        player.level.addFreshEntity(new FallingMeteor(player.level, pos.x, pos.y, pos.z, 5.0F, player));
    }

    @Override
    public void applyConstellationAttributes(Player player, int constRank) {
    }
}