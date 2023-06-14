package com.tzaranthony.genshingatcha.core.character;

import com.tzaranthony.genshingatcha.core.capabilities.CharacterHelper;
import com.tzaranthony.genshingatcha.core.entities.elements.character.FireCloudDiluc;
import com.tzaranthony.genshingatcha.core.items.Claymore;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Diluc extends Character {
    public Diluc(int elementID, int mainCooldown, int ultCooldown) {
        super(elementID, mainCooldown, ultCooldown);
    }

    @Override
    public void performMainAttack(Player player) {
        if (player instanceof ServerPlayer sPlayer) {
            int constRank = CharacterHelper.getConstRank(sPlayer);
            boolean isSecond = false;
            if (constRank >= 4 && player.level.getEntitiesOfClass(FireCloudDiluc.class, player.getBoundingBox().inflate(8)).size() >= 4) {
                isSecond = true;
            }

            double x = player.getX();
            double y = player.getY() + 0.8D;
            double z = player.getZ();
            float rot = player.getYRot() + 180.0F;
            player.level.playSound(null, x, y, z, SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
            for (int j = -3; j < 4; ++j) {
                double k = Math.PI * ((rot + j * 20.0D) / 180.0D);
                player.swing(InteractionHand.MAIN_HAND);
                player.level.addFreshEntity(new FireCloudDiluc(player.level, x + Math.sin(k) * 2.5D, y - (((float) j) * 0.2F), z + -Math.cos(k) * 2.5D, player.getYRot(), 6 + (j * 2), player,  constRank, isSecond));
            }
            if (constRank >= 6) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2));
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 2));
            }
        }
    }

    @Override
    public int getMainCooldown(int constRank) {
        if (constRank >= 4) {
            return this.mainCooldown - 25;
        }
        return this.ultCooldown;
    }

    @Override
    public void performUltimateAttack(Player player) {
        //TODO: replace with fire slice when i get the render
    }

    @Override
    public void applyConstellationAttributes(Player player, int constRank) {
        AttributeMap map = player.getAttributes();
        if (constRank >= 1) {
            map.getInstance(Attributes.ATTACK_DAMAGE).addPermanentModifier(EntityUtil.CharacterAttributeMap.get(1.1F));
        }
        if (constRank >= 2) {
            map.getInstance(Attributes.ATTACK_SPEED).addPermanentModifier(EntityUtil.CharacterAttributeMap.get(1.2F));
        }
    }

    @Override
    public Element.E getElement() {
        return Element.E.PYRO;
    }

    @Override
    public boolean hasCorrectWeapon(ItemStack stack) {
        return stack.getItem() instanceof Claymore;
    }
}