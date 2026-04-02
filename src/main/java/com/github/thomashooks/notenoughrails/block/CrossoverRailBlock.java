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

import com.github.thomashooks.notenoughrails.block.property.AllProperties;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class CrossoverRailBlock extends AbstractRailBlock {
    public static final MapCodec<CrossoverRailBlock> CODEC = createCodec(CrossoverRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = AllProperties.FLAT_RAIL_SHAPE;

    public CrossoverRailBlock(Settings settings) {
        super(true, settings);
        this.setDefaultState(this.getDefaultState()
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
    public RailShape notEnoughRails$getRailDirection(BlockState state, World world, BlockPos pos, @Nullable AbstractMinecartEntity minecart) {
        if (minecart != null) {
            Direction direction = minecart.getMovementDirection();
            if (direction != null) {
                return switch (direction) {
                    case EAST, WEST -> RailShape.EAST_WEST;
                    default -> RailShape.NORTH_SOUTH;
                };
            }
        }
        return super.notEnoughRails$getRailDirection(state, world, pos, minecart);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, WATERLOGGED);
    }
}
