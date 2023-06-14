package com.tzaranthony.genshingatcha.core.character;

import com.tzaranthony.genshingatcha.core.items.ElementalBow;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Fischl extends Character {
    public Fischl(int elementID, int mainCooldown, int ultCooldown) {
        super(elementID, mainCooldown, ultCooldown);
    }

    @Override
    public void performMainAttack(Player player) {
        //TODO: bird turret
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

    @Override
    public Element.E getElement() {
        return Element.E.ELECTRO;
    }

    @Override
    public boolean hasCorrectWeapon(ItemStack stack) {
        return stack.getItem() instanceof ElementalBow;
    }
}