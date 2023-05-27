package com.tzaranthony.genshingatcha.core.elements;

import com.tzaranthony.genshingatcha.core.entities.elements.AreaFireCloud;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class Fischl extends Character {
    public Fischl(int elementID, int mainCooldown, int ultCooldown) {
        super(elementID, mainCooldown, ultCooldown);
    }

    @Override
    public void performMainAttack(Player player) {
        // bird turret
        //TODO: add extra damage and range CharacterHelper.getCharacter(sPlayer) >= 2
        //TODO: add level? CharacterHelper.getCharacter(sPlayer) >= 3
        //TODO: add extra summons time CharacterHelper.getCharacter(sPlayer) >= 6
        double x = player.getX();
        double y = player.getY() + 0.8D;
        double z = player.getZ();
        float rot = player.getYRot() + 180.0F;
        player.level.playSound(null, x, y, z, SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
        for (int j = -3; j < 4; ++j) {
            double k = Math.PI * ((rot + j * 20.0D) / 180.0D);
            player.level.addFreshEntity(new AreaFireCloud(player.level, x + Math.sin(k) * 2.5D, y - (((float) j) * 0.2F), z + -Math.cos(k) * 2.5D, player.getYRot(), 6 + (j * 2), player));
        }
    }

    @Override
    public void performUltimateAttack(Player player) {
        // copy pyro ult but electro
        //TODO: add extra damage & heal 4 HP when discarded CharacterHelper.getCharacter(sPlayer) >= 4
        //TODO: add level? CharacterHelper.getCharacter(sPlayer) >= 5
        double x = player.getX();
        double y = player.getY() + 0.8D;
        double z = player.getZ();
        float rot = player.getYRot() + 180.0F;
        for (int j = -3; j < 4; ++j) {
            double k = Math.PI * ((rot + j * 20.0D) / 180.0D);
            player.level.addFreshEntity(new AreaFireCloud(player.level, x + Math.sin(k) * 2.5D, y - (((float) -j) * 0.2F), z + -Math.cos(k) * 2.5D, player.getYRot(), 6 + (j * 2), player));
        }
    }

    @Override
    public void applyConstellationAttributes(Player player, int constRank) {
        AttributeMap map = player.getAttributes();
        if (constRank >= 1) {
            map.getInstance(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(EntityUtil.CharacterAttributeMap.get(Attributes.ATTACK_DAMAGE), "diluc_c1", 0.20D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }
}