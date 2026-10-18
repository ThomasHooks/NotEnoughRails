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

import com.github.thomashooks.notenoughrails.util.VoxelShapeHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class RollingMillBlock extends Block {
    public static final MapCodec<RollingMillBlock> CODEC = createCodec(RollingMillBlock::new);
    private static final VoxelShape BASE_X_VOXEL_SHAPE = Block.createCuboidShape(2.0, 0.0, 0.0, 14.0, 15.0, 16.0);
    private static final VoxelShape BASE_Z_VOXEL_SHAPE = Block.createCuboidShape(0.0, 0.0, 2.0, 16.0, 15.0, 14.0);
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    public RollingMillBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
        );
    }

    @Override
    protected MapCodec<? extends Block> getCodec() { return CODEC; }

    //region Block Methods
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case SOUTH, NORTH -> VoxelShapes.union(
                    VoxelShapeHelper.QUARTER_BLOCK,
                    BASE_Z_VOXEL_SHAPE
            ).simplify();
            case EAST, WEST -> VoxelShapes.union(
                    VoxelShapeHelper.QUARTER_BLOCK,
                    BASE_X_VOXEL_SHAPE
            ).simplify();
            default -> VoxelShapeHelper.FULL_BLOCK;
        };
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(FACING); }
    //endregion
}
