package com.tzaranthony.genshingatcha.core.blocks;

import com.tzaranthony.genshingatcha.core.blockEntities.GatchaMachineBE;
import com.tzaranthony.genshingatcha.registries.GGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class GatchaMachine extends Block implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public GatchaMachine(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GatchaMachineBE(pos, state);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof GatchaMachineBE) {
                NetworkHooks.openGui((ServerPlayer) player, (GatchaMachineBE) blockentity, pos);
            }
            return InteractionResult.CONSUME;
        }
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int event, int event1) {
        super.triggerEvent(state, level, pos, event, event1);
        BlockEntity blockentity = level.getBlockEntity(pos);
        return blockentity != null && blockentity.triggerEvent(event, event1);
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state1, boolean huh) {
        if (!state.is(state1.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof GatchaMachineBE be) {
                if (level instanceof ServerLevel) {
                    be.dropInventory();
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, state1, huh);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
        state.add(FACING);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, GGBlockEntities.GATCHA_MACHINE.get(), GatchaMachineBE::tick);
    }

    @javax.annotation.Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> checktype, BlockEntityType<E> validType, BlockEntityTicker<? super E> ticker) {
        return validType == checktype ? (BlockEntityTicker<A>) ticker : null;
    }
}