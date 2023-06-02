package com.tzaranthony.genshingatcha.core.character;

import com.tzaranthony.genshingatcha.core.capabilities.CharacterHelper;
import com.tzaranthony.genshingatcha.core.entities.elements.AreaFrostCloud;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class Qiqi extends Character {
    public Qiqi(int elementID, int mainCooldown, int ultCooldown) {
        super(elementID, mainCooldown, ultCooldown);
    }

    @Override
    public void performMainAttack(Player player) {
        double x = player.getX();
        double y = player.getY() + 0.8D;
        double z = player.getZ();
        if (player instanceof ServerPlayer sPlayer) {
            player.level.addFreshEntity(new AreaFrostCloud(player.level, x, y, z, player.getYRot(), player, CharacterHelper.getConstRank(sPlayer), false));
        }
        player.level.playSound(null, x, y, z, SoundEvents.PLAYER_HURT_FREEZE, player.getSoundSource(), 1.0F, 1.0F);
        player.swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public void performUltimateAttack(Player player) {
        double x = player.getX();
        double y = player.getY() + 0.8D;
        double z = player.getZ();
        if (player instanceof ServerPlayer sPlayer) {
            player.level.addFreshEntity(new AreaFrostCloud(player.level, x, y, z, player.getYRot(), player, CharacterHelper.getConstRank(sPlayer), true));
        }
        player.level.playSound(null, x, y, z, SoundEvents.PLAYER_HURT_FREEZE, player.getSoundSource(), 1.0F, 1.0F);
        player.swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public int getUltCooldown(int constRank) {
        if (constRank >= 1) {
            return this.ultCooldown - 60;
        }
        return this.ultCooldown;
    }

    @Override
    public void applyConstellationAttributes(Player player, int constRank) {
        //TODO: height changes???
        //I think what I need to do is call player.refreshDimensions() then intercept the EntityEvent.Size on refresh and check if the player now has Qiqi (4) then resize to a .5?
        //looks like I also have to deal with the hit box
        AttributeMap map = player.getAttributes();
        if (constRank >= 2) {
            map.getInstance(Attributes.ATTACK_DAMAGE).addPermanentModifier(EntityUtil.CharacterAttributeMap.get(1.2F));
        }
    }

    @Override
    public Element getElement() {
        return Element.CRYO;
    }
}