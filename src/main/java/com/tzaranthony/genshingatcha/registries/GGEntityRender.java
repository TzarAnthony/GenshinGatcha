package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.client.entityRenders.ElementalSlimeRender;
import com.tzaranthony.genshingatcha.client.entityRenders.MeteorRender;
import com.tzaranthony.genshingatcha.client.entityRenders.arrows.ElementalArrowRender;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GGEntityRender {
    public static void renderEntities() {
        EntityRenderers.register(GGEntities.ELEMENTAL_SLIME.get(), ElementalSlimeRender::new);
        EntityRenderers.register(GGEntities.ELEMENTAL_ARROW.get(), ElementalArrowRender::new);
        EntityRenderers.register(GGEntities.EXPLODING_POTION.get(), ThrownItemRenderer::new);
        EntityRenderers.register(GGEntities.FIRE_CLOUD.get(), NoopRenderer::new);
        EntityRenderers.register(GGEntities.FROST_CLOUD.get(), NoopRenderer::new);
        EntityRenderers.register(GGEntities.METEOR.get(), MeteorRender::new);
    }
}