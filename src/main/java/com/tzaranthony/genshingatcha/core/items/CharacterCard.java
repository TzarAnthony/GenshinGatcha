package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.core.capabilities.CharacterHelper;
import com.tzaranthony.genshingatcha.core.util.EntityUtil;
import com.tzaranthony.genshingatcha.registries.GGCharacters;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

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
            CharacterHelper.setChar(this.characterID, this.constRank, (ServerPlayer) player);
            GGCharacters.characterMap.get(this.characterID).applyConstellationAttributes(player, this.constRank);
        } else {
            player.playSound(SoundEvents.EVOKER_CAST_SPELL, 0.75F, 1.25F);
            Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("tooltip.genshingatcha.no_drop"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}