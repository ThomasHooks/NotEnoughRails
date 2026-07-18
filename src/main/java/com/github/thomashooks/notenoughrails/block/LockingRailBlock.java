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

import com.github.thomashooks.notenoughrails.block.entity.LockingRailBlockEntity;
import com.github.thomashooks.notenoughrails.block.property.AllProperties;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

// TODO add some kind of redstone comparator behavior
public class LockingRailBlock extends AbstractRailBlock implements BlockEntityProvider {
    public static final MapCodec<LockingRailBlock> CODEC = createCodec(LockingRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = AllProperties.FLAT_RAIL_SHAPE;
    public static final BooleanProperty POWERED = Properties.POWERED;

    public LockingRailBlock(Settings settings) {
        super(true, settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(POWERED, false)
                .with(SHAPE, RailShape.NORTH_SOUTH)
                .with(WATERLOGGED, false)
        );
    }

    @Override
    protected MapCodec<? extends AbstractRailBlock> getCodec() { return CODEC; }

    @Override
    public Property<RailShape> getShapeProperty() { return SHAPE; }

    @Override
    public boolean notEnoughRails$isFlatRail(BlockState state, World world, BlockPos pos) { return true; }

    @Override
    public void notEnoughRails$onMinecartPass(BlockState state, ServerWorld world, BlockPos pos, AbstractMinecartEntity minecart) {
        if (!state.get(POWERED)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (minecart != null && blockEntity instanceof LockingRailBlockEntity lockingRailBlockEntity) {
                lockingRailBlockEntity.lockMinecart(minecart);
            }
        }
    }

    @Override
    protected void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor) {
        boolean isReceivingRedstonePower = world.isReceivingRedstonePower(pos);
        if (isReceivingRedstonePower != state.get(POWERED)) {
            world.setBlockState(pos, state.with(POWERED, isReceivingRedstonePower), Block.NOTIFY_ALL);
            world.updateNeighbors(pos.down(), this);
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (state.get(POWERED) && blockEntity instanceof LockingRailBlockEntity lockingRailBlockEntity) {
            lockingRailBlockEntity.unlockMinecart();
        }
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LockingRailBlockEntity(pos, state);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        RailShape railShape = state.get(SHAPE);
        RailShape railShape2 = this.rotateShape(railShape, rotation);
        return state.with(SHAPE, railShape2);
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        RailShape railShape = state.get(SHAPE);
        RailShape railShape2 = this.mirrorShape(railShape, mirror);
        return state.with(SHAPE, railShape2);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED, SHAPE, WATERLOGGED);
    }
}
