package com.tzaranthony.genshingatcha.client.entityRenders.arrows;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.entities.projectiles.ElementalArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ElementalArrowRender extends ArrowRenderer<ElementalArrow> {
    public static final ResourceLocation CRYO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/projectiles/cryo_arrow.png");
    public static final ResourceLocation PYRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/projectiles/pyro_arrow.png");
    public static final ResourceLocation ELECTRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/projectiles/electro_arrow.png");
    public static final ResourceLocation GEO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/projectiles/geo_arrow.png");
    public static final ResourceLocation HYDRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/projectiles/hydro_arrow.png");
    public static final ResourceLocation DENDRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/projectiles/dendro_arrow.png");
    public static final ResourceLocation ANEMO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/projectiles/anemo_arrow.png");

    public ElementalArrowRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalArrow arrow) {
        switch (arrow.getElement()) {
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
}