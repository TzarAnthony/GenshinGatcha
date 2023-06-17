package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.client.entityRenders.ElementalSlimeRender;
import com.tzaranthony.genshingatcha.client.entityRenders.MeteorRender;
import com.tzaranthony.genshingatcha.client.entityRenders.projectiles.ElementalArrowRender;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GGEntityRender {
    public static void renderEntities() {
        EntityRenderers.register(GGEntities.CRYO_SLIME.get(), ElementalSlimeRender::new);
        EntityRenderers.register(GGEntities.PYRO_SLIME.get(), ElementalSlimeRender::new);
        EntityRenderers.register(GGEntities.ELECTRO_SLIME.get(), ElementalSlimeRender::new);
        EntityRenderers.register(GGEntities.GEO_SLIME.get(), ElementalSlimeRender::new);
        EntityRenderers.register(GGEntities.HYDRO_SLIME.get(), ElementalSlimeRender::new);
        EntityRenderers.register(GGEntities.DENDRO_SLIME.get(), ElementalSlimeRender::new);
        EntityRenderers.register(GGEntities.ANEMO_SLIME.get(), ElementalSlimeRender::new);

        EntityRenderers.register(GGEntities.EXPLODING_POTION.get(), ThrownItemRenderer::new);
        EntityRenderers.register(GGEntities.ELEMENTAL_ARROW.get(), ElementalArrowRender::new);
        EntityRenderers.register(GGEntities.DENDRO_SLIME_SEED.get(), ThrownItemRenderer::new);
        EntityRenderers.register(GGEntities.PYROBALL.get(), ThrownItemRenderer::new);
        EntityRenderers.register(GGEntities.GEO_ROCK.get(), ThrownItemRenderer::new);
        EntityRenderers.register(GGEntities.WATERBALL.get(), ThrownItemRenderer::new);
        EntityRenderers.register(GGEntities.WIND_BALL.get(), ThrownItemRenderer::new);
        EntityRenderers.register(GGEntities.SLIME_TOSS.get(), ThrownItemRenderer::new);

        EntityRenderers.register(GGEntities.FIRE_PLUME.get(), NoopRenderer::new);
        EntityRenderers.register(GGEntities.LIGHTNING_SUMMONS.get(), NoopRenderer::new);
        EntityRenderers.register(GGEntities.ELECTRO_LIGHTNING.get(), LightningBoltRenderer::new);
        EntityRenderers.register(GGEntities.FIRE_CLOUD.get(), NoopRenderer::new);
        EntityRenderers.register(GGEntities.FROST_CLOUD.get(), NoopRenderer::new);
        EntityRenderers.register(GGEntities.METEOR.get(), MeteorRender::new);
    }
}