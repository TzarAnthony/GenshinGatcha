package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.core.capabilities.CharacterHelper;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.registries.GGCharacters;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CharacterCard extends Item {
    protected final int characterID;
    protected final int constRank;

    public CharacterCard(int charID, int constRank, Properties properties) {
        super(properties);
        this.characterID = charID;
        this.constRank = constRank;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.getCooldowns().addCooldown(this, 200);
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            player.getAttributes().removeAttributeModifiers(EntityUtil.getAttributeModifiers());
            CharacterHelper.setElement(this.characterID, this.constRank, (ServerPlayer) player);
            GGCharacters.characterMap.get(this.characterID).applyConstellationAttributes(player, this.constRank);
            return InteractionResultHolder.consume(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }


}