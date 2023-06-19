package com.tzaranthony.genshingatcha.core.items;

import com.tzaranthony.genshingatcha.client.blockEntityRenders.BEItemStackRender;
import com.tzaranthony.genshingatcha.core.blockEntities.ChallengeChestBE;
import com.tzaranthony.genshingatcha.registries.GGBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModeledBlockItem extends BlockItem {
  public ModeledBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void initializeClient(Consumer<IItemRenderProperties> consumer) {
    super.initializeClient(consumer);
    consumer.accept(new IItemRenderProperties() {
      @Override
      public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
        Supplier<BlockEntity> model = () -> new ChallengeChestBE(BlockPos.ZERO, GGBlocks.CHALLENGE_CHEST.get().defaultBlockState());
        return new BEItemStackRender(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), model);
      }
    });
  }
}