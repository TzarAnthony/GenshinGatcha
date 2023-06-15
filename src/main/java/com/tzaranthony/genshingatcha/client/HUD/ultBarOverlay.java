package com.tzaranthony.genshingatcha.client.HUD;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.capabilities.CharacterClient;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.gui.IIngameOverlay;

public class ultBarOverlay {
    private static final ResourceLocation ultBar = new ResourceLocation(GenshinGacha.MOD_ID, "textures/gui/hud/ult_bar.png");
    private static final ResourceLocation iconHolder = new ResourceLocation(GenshinGacha.MOD_ID, "textures/gui/hud/icon_holder.png");

    public static final IIngameOverlay HUD_ULT = ((gui, poseStack, partialTick, width, height) -> {
        int x = (width - 33) / 10 * 9;
        int y = height / 20 + 40;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ultBar);

        float z = CharacterClient.getUltCooldownPct() * 29.0F;
        int fillLen = (int) (z);
        GuiComponent.blit(poseStack, x + 5, y, 0, 0, 60, 14, 60, 28);
        if (fillLen > 0) {
            GuiComponent.blit(poseStack, x + 5, y, 0, 14, fillLen * 2, 14, 60, 28);
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, iconHolder);
        GuiComponent.blit(poseStack, x - 18, y - 4, 0, 0, 22, 22, 22, 22);

        RenderSystem.setShaderTexture(0, getIcon(CharacterClient.getChar()));
        GuiComponent.blit(poseStack, x - 15, y - 1, 0, 0, 16, 16, 16, 16);
    });

    protected static ResourceLocation getIcon(int characterId) {
        switch (characterId) {
            case 1:
                return new ResourceLocation(GenshinGacha.MOD_ID, "textures/item/diluc_c6.png");
            case 2:
                return new ResourceLocation(GenshinGacha.MOD_ID, "textures/item/fischl_c6.png");
            case 3:
                return new ResourceLocation(GenshinGacha.MOD_ID, "textures/item/zhongli_c6.png");
            case 4:
                return new ResourceLocation(GenshinGacha.MOD_ID, "textures/item/qiqi_c6.png");
            default:
                return Items.AIR.getRegistryName();
        }
    }
}