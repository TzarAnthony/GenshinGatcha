package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.client.blockEntityRenders.GGChestRender;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class GGBlockEntityRender {
    public static void renderBlockEntities() {
        BlockEntityRenderers.register(GGBlockEntities.CHALLENGE_CHEST_BE.get(), GGChestRender::new);
    }
}