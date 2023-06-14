package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.core.capabilities.CharacterHelper;
import com.tzaranthony.genshingatcha.core.character.Character;
import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.ElementalArrow;
import com.tzaranthony.genshingatcha.core.entities.mobs.ElementalMob;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.registries.GGCharacters;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ElementalArrowItem extends ArrowItem {
    public ElementalArrowItem(Item.Properties properties) {
        super(properties);
    }

    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity le) {
        if (le instanceof ServerPlayer sPlayer) {
            Character character = GGCharacters.characterMap.get(CharacterHelper.getCharacter(sPlayer));
            if (character != null) {
                Element.E element = character.getElement();
                return new ElementalArrow(sPlayer, element, level);
            }
        }
        if (le instanceof ElementalMob em) {
            return new ElementalArrow(le, em.getElement(), level);
        }
        return super.createArrow(level, stack, le);
    }
}