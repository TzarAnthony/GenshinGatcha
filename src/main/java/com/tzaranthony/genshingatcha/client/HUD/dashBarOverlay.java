package com.tzaranthony.genshingatcha.client.HUD;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.capabilities.CharacterClient;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;

public class dashBarOverlay {
    private static final ResourceLocation dashBar = new ResourceLocation(GenshinGacha.MOD_ID, "textures/gui/hud/dash_bar.png");
    private static final ResourceLocation iconHolder = new ResourceLocation(GenshinGacha.MOD_ID, "textures/gui/hud/icon_holder.png");
    private static final ResourceLocation speedIcon = new ResourceLocation("minecraft", "textures/mob_effect/speed.png");

    public static final IIngameOverlay HUD_DASH = ((gui, poseStack, partialTick, width, height) -> {
        int x = (width - 33) / 10 * 9;
        int y = height / 20;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, dashBar);

        float z = CharacterClient.getDashCooldownPct() * 29.0F;
        int fillLen = (int) (z);
        GuiComponent.blit(poseStack, x + 5, y, 0, 0, 60, 14, 60, 28);
        if (fillLen > 0) {
            GuiComponent.blit(poseStack, x + 5, y, 0, 14, fillLen * 2, 14, 60, 28);
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, iconHolder);
        GuiComponent.blit(poseStack, x - 18, y - 4, 0, 0, 22, 22, 22, 22);

        RenderSystem.setShaderTexture(0, speedIcon);
        GuiComponent.blit(poseStack, x - 15, y - 1, 0, 0, 16, 16, 16, 16);
    });
}