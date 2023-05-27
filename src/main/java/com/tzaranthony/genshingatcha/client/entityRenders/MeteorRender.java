package com.tzaranthony.genshingatcha.client.entityRenders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tzaranthony.genshingatcha.core.entities.elements.FallingMeteor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MeteorRender extends EntityRenderer<FallingMeteor> {
    public MeteorRender(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
    }

    public void render(FallingMeteor meteor, float p_116178_, float p_116179_, PoseStack pose, MultiBufferSource buffer, int light) {
        pose.pushPose();
        float size = meteor.getSize();
        pose.scale(size, size, size);
        pose.mulPose(Vector3f.YP.rotationDegrees(meteor.getRotation()));
        pose.mulPose(Vector3f.XP.rotationDegrees(-meteor.getRotation()));
        pose.translate(-0.5D, -0.8D, -0.5D);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.MAGMA_BLOCK.defaultBlockState(), pose, buffer, light, OverlayTexture.NO_OVERLAY);
        pose.popPose();
        super.render(meteor, p_116178_, p_116179_, pose, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(FallingMeteor p_114482_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}