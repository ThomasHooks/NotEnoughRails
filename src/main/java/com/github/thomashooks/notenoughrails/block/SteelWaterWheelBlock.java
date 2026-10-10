/*
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.thomashooks.notenoughrails.block;

import com.github.thomashooks.notenoughrails.block.entity.AllBlockEntities;
import com.github.thomashooks.notenoughrails.block.entity.LazyTickingBlockEntity;
import com.github.thomashooks.notenoughrails.block.entity.SteelWaterWheelBlockEntity;
import com.github.thomashooks.notenoughrails.block.property.AllProperties;
import com.github.thomashooks.notenoughrails.energy.KinematicBlockProvider;
import com.github.thomashooks.notenoughrails.energy.MechanicalConnection;
import com.github.thomashooks.notenoughrails.util.VoxelShapeHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

public class SteelWaterWheelBlock extends Block implements BlockEntityProvider, KinematicBlockProvider {
    public static final MapCodec<SteelWaterWheelBlock> CODEC = createCodec(SteelWaterWheelBlock::new);
    public static final BooleanProperty SHAFT_IS_SHIFTED = AllProperties.SHAFT_IS_SHIFTED;
    private static final VoxelShape HUB_VOXEL_SHAPE = Block.createCuboidShape(5.0, 2.0, 5.0, 11.0, 14.0, 11.0);
    private static final VoxelShape TRIL_VOXEL_SHAPE = Block.createCuboidShape(0.0, 3.0, 0.0, 16.0, 13.0, 16.0);

    public SteelWaterWheelBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(SHAFT_IS_SHIFTED, false)
        );
    }

    @Override
    protected MapCodec<? extends Block> getCodec() { return CODEC; }

    //region Block Methods
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.isClient()) {
            return;
        }

        for (MechanicalConnection connection : getMechanicalConnections(world, pos, state)) {
            KinematicBlockProvider neighborBlockProvider = KinematicBlockProvider.getKinematicBlock(world, connection.pos());
            BlockState neighborState = world.getBlockState(connection.pos());
            if (neighborBlockProvider == null || !neighborBlockProvider.isAlignedWith(world, connection.pos(), neighborState, connection)) {
                continue;
            }

            boolean shifted = neighborState.get(SHAFT_IS_SHIFTED);
            world.setBlockState(pos, state.with(SHAFT_IS_SHIFTED, shifted));
            break;
        }
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(
                HUB_VOXEL_SHAPE,
                TRIL_VOXEL_SHAPE,
                VoxelShapeHelper.AXLE[VoxelShapeHelper.getAxisIndex(Direction.Axis.Y)]
        ).simplify();
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) { return BlockRenderType.INVISIBLE; }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(SHAFT_IS_SHIFTED); }
    //endregion

    //region Kinematic Block Provider Methods
    @Override
    public float getSpeed(@NotNull World world, @NotNull BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof SteelWaterWheelBlockEntity blockEntity) {
            return blockEntity.getSpeed();
        }
        return 0.0F;
    }

    @Override
    public void changeSpeed(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockPos driverPos, float speedIn) {
        // The steel water wheel is an engine, so it cannot be changed by other blocks
    }

    @Override
    public @NotNull ArrayList<MechanicalConnection> getMechanicalConnections(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state) {
        if (world.getBlockEntity(pos) instanceof SteelWaterWheelBlockEntity blockEntity) {
            return blockEntity.getMechanicalConnections();
        }
        return new ArrayList<>();
    }
    //endregion

    //region Block Entity Provider Methods
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return LazyTickingBlockEntity.getTicker(world);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return AllBlockEntities.WATERWHEEL_STEEL.instantiate(pos, state);
    }
    //endregion
}
