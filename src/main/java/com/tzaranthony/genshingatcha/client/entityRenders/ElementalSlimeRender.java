package com.tzaranthony.genshingatcha.client.entityRenders;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.entities.mobs.slimes.ElementalSlime;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;

public class ElementalSlimeRender extends SlimeRenderer {
    public static final ResourceLocation CRYO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/cryo_slime.png");
    public static final ResourceLocation PYRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/pyro_slime.png");
    public static final ResourceLocation ELECTRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/electro_slime.png");
    public static final ResourceLocation GEO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/geo_slime.png");
    public static final ResourceLocation HYDRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/hydro_slime.png");
    public static final ResourceLocation DENDRO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/dendro_slime.png");
    public static final ResourceLocation ANEMO_LOCATION = new ResourceLocation(GenshinGacha.MOD_ID, "textures/entity/slimes/anemo_slime.png");

    public ElementalSlimeRender(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(Slime slime) {
        if (slime instanceof ElementalSlime es) {
            switch (es.getElement()) {
                case 0:
//                    return CRYO_LOCATION;
                    return ELECTRO_LOCATION;
                case 1:
//                    return PYRO_LOCATION;
                    return ELECTRO_LOCATION;
                case 2:
                    return ELECTRO_LOCATION;
                case 3:
//                    return GEO_LOCATION;
                    return ELECTRO_LOCATION;
                case 4:
//                    return HYDRO_LOCATION;
                    return ELECTRO_LOCATION;
                case 5:
//                    return DENDRO_LOCATION;
                    return ELECTRO_LOCATION;
                default:
//                    return ANEMO_LOCATION;
                    return ELECTRO_LOCATION;
            }
        }
        return super.getTextureLocation(slime);
    }
}