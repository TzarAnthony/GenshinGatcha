package com.tzaranthony.genshingatcha.client.HUD;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.capabilities.CharacterClient;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;

public class mainBarOverlay {
    private static ResourceLocation mainBar = new ResourceLocation(GenshinGacha.MOD_ID, "textures/gui/hud/main_bar.png");

    public static final IIngameOverlay HUD_MAIN = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 12;
        int y = height - 58;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, mainBar);

        float z = CharacterClient.getMainCooldownPct() * 29.0F;
        int fillLen = (int) (z);
        GuiComponent.blit(poseStack, x, y, 0, 0, 80, 14, 80, 28);
        if (fillLen > 0) {
            GuiComponent.blit(poseStack, x, y, 0, 14, (fillLen + 6) * 2, 14, 80, 28);
        }
    });
}