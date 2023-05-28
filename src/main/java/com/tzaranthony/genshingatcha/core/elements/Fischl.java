package com.tzaranthony.genshingatcha.core.elements;

import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
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
    }

    @Override
    public void performUltimateAttack(Player player) {
        //TODO: copy pyro ult but electro
    }

    @Override
    public void applyConstellationAttributes(Player player, int constRank) {
        AttributeMap map = player.getAttributes();
        if (constRank >= 1) {
            map.getInstance(Attributes.ATTACK_DAMAGE).addPermanentModifier(EntityUtil.CharacterAttributeMap.get(1.1F));
        }
    }
}