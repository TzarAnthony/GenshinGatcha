package com.tzaranthony.genshingatcha.client.blockEntityRenders;

import com.tzaranthony.genshingatcha.GenshinGacha;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;

public class GGChestRender<T extends BlockEntity & LidBlockEntity> extends ChestRenderer<T> {
    public static final ResourceLocation CHALLENGE_CHEST = new ResourceLocation(GenshinGacha.MOD_ID, "entity/chest/challenge");

    public GGChestRender(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Material getMaterial(T blockEntity, ChestType chestType) {
        return new Material(Sheets.CHEST_SHEET, CHALLENGE_CHEST);
    }
}