package com.tzaranthony.genshingatcha.client.entityRenders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.entities.mobs.slimes.AbstractElementalSlime;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ElementalSlimeRender extends MobRenderer<AbstractElementalSlime, SlimeModel<AbstractElementalSlime>> {
    public static final ResourceLocation CRYO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/cryo_slime.png");
    public static final ResourceLocation PYRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/pyro_slime.png");
    public static final ResourceLocation ELECTRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/electro_slime.png");
    public static final ResourceLocation GEO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/geo_slime.png");
    public static final ResourceLocation HYDRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/hydro_slime.png");
    public static final ResourceLocation DENDRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/dendro_slime.png");
    public static final ResourceLocation ANEMO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/anemo_slime.png");

    public ElementalSlimeRender(EntityRendererProvider.Context context) {
        super(context, new SlimeModel<>(context.bakeLayer(ModelLayers.SLIME)), 0.25F);
        this.addLayer(new SlimeOuterLayer<>(this, context.getModelSet()));
    }

    public void render(AbstractElementalSlime slime, float v, float pTick, PoseStack pose, MultiBufferSource buff, int lighting) {
        this.shadowRadius = 0.25F * (float) slime.getSize();
        super.render(slime, v, pTick, pose, buff, lighting);
    }

    protected void scale(AbstractElementalSlime slime, PoseStack pose, float pTick) {
        float f = 0.999F;
        pose.scale(0.999F, 0.999F, 0.999F);
        pose.translate(0.0D, (double)0.001F, 0.0D);
        float f1 = (float)slime.getSize();
        float f2 = Mth.lerp(pTick, slime.oSquish, slime.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        pose.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    public ResourceLocation getTextureLocation(AbstractElementalSlime slime) {
        switch (slime.getElement()) {
            case 0:
                return CRYO_LOCATION;
            case 1:
                return PYRO_LOCATION;
            case 2:
                return ELECTRO_LOCATION;
            case 3:
                return GEO_LOCATION;
            case 4:
                return HYDRO_LOCATION;
            case 5:
                return DENDRO_LOCATION;
            default:
                return ANEMO_LOCATION;
        }
    }

    protected boolean isShaking(AbstractElementalSlime slime) {
        return slime.isShaking() || super.isShaking(slime);
    }
}