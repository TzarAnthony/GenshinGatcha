package com.tzaranthony.genshingatcha.client.HUD;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.capabilities.CharacterClient;
import com.tzaranthony.genshingatcha.registries.GGCharacters;
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

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getIcon(GGCharacters.characterMap.get(CharacterClient.getChar()).getElement().getId()));
        GuiComponent.blit(poseStack, x - 6, y - 2, 0, 0, 18, 18, 18, 18);
    });

    protected static ResourceLocation getIcon(int elementId) {
        switch (elementId) {
            case 0:
                return new ResourceLocation(GenshinGacha.MOD_ID, "textures/mob_effect/cryo.png");
            case 1:
                return new ResourceLocation(GenshinGacha.MOD_ID, "textures/mob_effect/pyro.png");
            case 2:
                return new ResourceLocation(GenshinGacha.MOD_ID, "textures/mob_effect/electro.png");
            case 3:
                return new ResourceLocation(GenshinGacha.MOD_ID, "textures/mob_effect/geo.png");
            case 4:
                return new ResourceLocation(GenshinGacha.MOD_ID, "textures/mob_effect/hydro.png");
            case 5:
                return new ResourceLocation(GenshinGacha.MOD_ID, "textures/mob_effect/dendro.png");
            default:
                return new ResourceLocation(GenshinGacha.MOD_ID, "textures/mob_effect/anemo.png");
        }
    }
}