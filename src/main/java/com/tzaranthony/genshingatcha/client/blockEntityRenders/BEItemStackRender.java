package com.tzaranthony.genshingatcha.client.blockEntityRenders;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class BEItemStackRender<T extends BlockEntity> extends BlockEntityWithoutLevelRenderer {
    private final Supplier<T> modelSupplier;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public BEItemStackRender(BlockEntityRenderDispatcher renderDispatcher, EntityModelSet modelSet, Supplier<T> modelSupplier) {
        super(renderDispatcher, modelSet);
        this.modelSupplier = modelSupplier;
        this.blockEntityRenderDispatcher = renderDispatcher;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType type, PoseStack pose, MultiBufferSource buff, int light, int layer) {
        this.blockEntityRenderDispatcher.renderItem(this.modelSupplier.get(), pose, buff, light, layer);
    }
}