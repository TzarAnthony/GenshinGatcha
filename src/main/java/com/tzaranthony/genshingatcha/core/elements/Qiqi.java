package com.tzaranthony.genshingatcha.core.elements;

import com.tzaranthony.genshingatcha.core.capabilities.CharacterHelper;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.registries.GGEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Qiqi extends Character {
    public Qiqi(int elementID, int mainCooldown, int ultCooldown) {
        super(elementID, mainCooldown, ultCooldown);
    }

    @Override
    public void performMainAttack(Player player) {
        //TODO: add level? CharacterHelper.getCharacter(sPlayer) >= 3 (idk if this is the fortune one)
        for(Player player1 : player.level.getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(4))) {
            player1.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100));
        }
        boolean hasC4 = player instanceof ServerPlayer sPlayer && CharacterHelper.getCharacter(sPlayer) == 4;
        for(Mob mob : player.level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(10))) {
            mob.addEffect(new MobEffectInstance(GGEffects.CRYO.get(), 100));
            if (hasC4) {
                mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 2));
            }
        }
    }

    @Override
    public void performUltimateAttack(Player player) {
        // Life steal off affected enemies?
        //TODO: add level? CharacterHelper.getCharacter(sPlayer) >= 5 (idk if this is the herald one)
        for(Player player1 : player.level.getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(10))) {
            player1.heal(6.0F);
            if (player instanceof ServerPlayer sPlayer && CharacterHelper.getCharacter(sPlayer) == 6) {
                player1.addItem(new ItemStack(Items.TOTEM_OF_UNDYING));
            }
        }

        for(Mob mob : player.level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(10))) {
            mob.addEffect(new MobEffectInstance(GGEffects.CRYO.get(), 200));
        }
    }

    @Override
    public int getUltCooldown(int constRank) {
        //TODO: should this be only reduced when the main ability is used?
        if (constRank >= 1) {
            return this.ultCooldown - 60;
        }
        return this.ultCooldown;
    }

    @Override
    public void applyConstellationAttributes(Player player, int constRank) {
        AttributeMap map = player.getAttributes();
        if (constRank >= 2) {
            map.getInstance(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(EntityUtil.CharacterAttributeMap.get(Attributes.ATTACK_DAMAGE), "qiqi_c1", 0.10D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }
}