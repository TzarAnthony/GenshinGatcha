package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.client.entityRenders.MeteorRender;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GGEntityRender {
    public static void renderEntities() {
        EntityRenderers.register(GGEntities.FIRE_CLOUD.get(), NoopRenderer::new);
        EntityRenderers.register(GGEntities.METEOR.get(), MeteorRender::new);
    }
}