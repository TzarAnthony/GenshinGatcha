package com.tzaranthony.genshingatcha.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.container.GachaMachineMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GatchaMachineScreen extends AbstractContainerScreen<GachaMachineMenu> {
    public static final ResourceLocation MENU = new ResourceLocation(GenshinGacha.MOD_ID, "textures/gui/gatcha_machine.png");
    protected final Component char_banner = new TranslatableComponent("container.genshingatcha.character_banner");
    protected int charLabelX = 8;
    protected int charLabelY = 18;
    protected final Component weapon_banner = new TranslatableComponent("container.genshingatcha.weapon_banner");
    protected int weaponLabelX = 8;
    protected int weaponLabelY = 49;

    public GatchaMachineScreen(GachaMachineMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    public void render(PoseStack poseStack, int x, int y, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, x, y, partialTick);
        this.renderTooltip(poseStack, x, y);
    }

    protected void renderLabels(PoseStack poseStack, int x, int y) {
        super.renderLabels(poseStack, x, y);
        this.font.draw(poseStack, this.char_banner, (float) this.charLabelX, (float) this.charLabelY, 4210752);
        this.font.draw(poseStack, this.weapon_banner, (float) this.weaponLabelX, (float) this.weaponLabelY, 4210752);
    }

    protected void renderBg(PoseStack poseStack, float partialTick, int x, int y) {
        int progX = 117;
        int progY = 44;
        int progXo = 176;
        int progYo = 0;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MENU);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(poseStack, i + progX, j + progY, progXo, progYo, this.menu.getProcessProgress(), 16);
    }
}