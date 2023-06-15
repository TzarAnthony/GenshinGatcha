package com.tzaranthony.genshingatcha.client.HUD;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.capabilities.CharacterClient;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;

public class dashBarOverlay {
    private static ResourceLocation dashBar = new ResourceLocation(GenshinGacha.MOD_ID, "textures/gui/hud/dash_bar.png");
    private static ResourceLocation speedIcon = new ResourceLocation("minecraft", "textures/mob_effect/speed.png");

    public static final IIngameOverlay HUD_DASH = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 12;
        int y = height - 87;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, dashBar);

        float z = CharacterClient.getDashCooldownPct() * 29.0F;
        int fillLen = (int) (z);
        GuiComponent.blit(poseStack, x, y, 0, 0, 80, 14, 80, 28);
        if (fillLen > 0) {
            GuiComponent.blit(poseStack, x, y, 0, 14, (fillLen + 6) * 2, 14, 80, 28);
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, speedIcon);
        GuiComponent.blit(poseStack, x - 6, y - 2, 0, 0, 18, 18, 18, 18);
    });
}