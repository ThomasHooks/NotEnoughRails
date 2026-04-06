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

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class CheckRailBlock extends PoweredRailBlock {
    public static final EnumProperty<Direction> HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;
    public static final double BOOSTING_FACTOR = 0.06D;
    public static final double LAUNCHING_RATE = 0.02D;

    public CheckRailBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(POWERED, false)
                .with(SHAPE, RailShape.NORTH_SOUTH)
                .with(WATERLOGGED, false)
                .with(HORIZONTAL_FACING, Direction.NORTH)
        );
    }

    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        super.onStateReplaced(state, world, pos, moved);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (oldState.isOf(state.getBlock()) && oldState.get(SHAPE) != state.get(SHAPE)) {
            updateDirectionFacing(state, world, pos, oldState);
        }
    }

    private void updateDirectionFacing(BlockState state, World world, BlockPos pos, BlockState oldState) {
        if ((oldState.get(SHAPE) == RailShape.NORTH_SOUTH && state.get(SHAPE) == RailShape.ASCENDING_NORTH)
                || (oldState.get(SHAPE) == RailShape.NORTH_SOUTH && state.get(SHAPE) == RailShape.ASCENDING_SOUTH)
                || (oldState.get(SHAPE) == RailShape.EAST_WEST && state.get(SHAPE) == RailShape.ASCENDING_EAST)
                || (oldState.get(SHAPE) == RailShape.EAST_WEST && state.get(SHAPE) == RailShape.ASCENDING_WEST)) {
            return;
        }

        boolean connectedToNorth = AbstractRailBlock.isRail(world, pos.north());
        boolean connectedToNorthAscending = AbstractRailBlock.isRail(world, pos.north().up());
        boolean connectedToEast = AbstractRailBlock.isRail(world, pos.east());
        boolean connectedToEastAscending = AbstractRailBlock.isRail(world, pos.east().up());
        boolean connectedToSouth = AbstractRailBlock.isRail(world, pos.south());
        boolean connectedToSouthAscending = AbstractRailBlock.isRail(world, pos.south().up());
        boolean connectedToWest = AbstractRailBlock.isRail(world, pos.west());
        boolean connectedToWestAscending = AbstractRailBlock.isRail(world, pos.west().up());
        switch (oldState.get(HORIZONTAL_FACING)) {
            case NORTH -> {
                if (connectedToEast || connectedToEastAscending)
                    world.setBlockState(pos, state.with(HORIZONTAL_FACING, oldState.get(HORIZONTAL_FACING).rotateYClockwise()), 3);
                else if (connectedToWest || connectedToWestAscending)
                    world.setBlockState(pos, state.with(HORIZONTAL_FACING, oldState.get(HORIZONTAL_FACING).rotateYCounterclockwise()), 3);
            }
            case SOUTH -> {
                if (connectedToEast || connectedToEastAscending)
                    world.setBlockState(pos, state.with(HORIZONTAL_FACING, oldState.get(HORIZONTAL_FACING).rotateYCounterclockwise()), 3);
                else if (connectedToWest || connectedToWestAscending)
                    world.setBlockState(pos, state.with(HORIZONTAL_FACING, oldState.get(HORIZONTAL_FACING).rotateYClockwise()), 3);
            }
            case EAST -> {
                if (connectedToNorth || connectedToNorthAscending)
                    world.setBlockState(pos, state.with(HORIZONTAL_FACING, oldState.get(HORIZONTAL_FACING).rotateYCounterclockwise()), 3);
                else if (connectedToSouth || connectedToSouthAscending)
                    world.setBlockState(pos, state.with(HORIZONTAL_FACING, oldState.get(HORIZONTAL_FACING).rotateYClockwise()), 3);
            }
            case WEST -> {
                if (connectedToNorth || connectedToNorthAscending)
                    world.setBlockState(pos, state.with(HORIZONTAL_FACING, oldState.get(HORIZONTAL_FACING).rotateYClockwise()), 3);
                else if (connectedToSouth || connectedToSouthAscending)
                    world.setBlockState(pos, state.with(HORIZONTAL_FACING, oldState.get(HORIZONTAL_FACING).rotateYCounterclockwise()), 3);
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return Objects.requireNonNull(super.getPlacementState(ctx)).with(HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public boolean notEnoughRails$isPoweredRail(BlockState state, World world, BlockPos pos) { return true; }

    @Override
    public void notEnoughRails$onMinecartPass(BlockState state, ServerWorld world, BlockPos pos, AbstractMinecartEntity minecart) {
        if (!state.get(POWERED)) {
            //When not powered the check rail acts like a normal rail
            return;
        }

        Vec3d velocity = minecart.getVelocity();
        double horizontalLength = velocity.horizontalLength();
        Direction minecartFacing = getMinecaftMovementDirection(minecart);
        Direction facing = state.get(HORIZONTAL_FACING);
        double boostFactor = facing != minecartFacing && minecartFacing != null ? -BOOSTING_FACTOR : BOOSTING_FACTOR;
        if (horizontalLength > 0.01D) {
            //Boosting minecart
            minecart.setVelocity(velocity.add(velocity.x / horizontalLength * boostFactor, 0.0D, velocity.z / horizontalLength * boostFactor));
        } else {
            //Launching minecart
            double velocityX = velocity.x;
            double velocityZ = velocity.z;
            RailShape railShape = ((AbstractRailBlock)state.getBlock()).notEnoughRails$getRailDirection(state, world ,pos, minecart);
            switch (railShape) {
                case EAST_WEST -> {
                    if (minecart.willHitBlockAt(pos.west()) || facing == Direction.EAST) {
                        velocityX = LAUNCHING_RATE;
                        //minecart.setPosition(minecart.getX() - velocityX, minecart.getY(), minecart.getZ());
                    } else if (minecart.willHitBlockAt(pos.east()) || facing == Direction.WEST) {
                        velocityX = -LAUNCHING_RATE;
                    }
                }
                case NORTH_SOUTH -> {
                    if (minecart.willHitBlockAt(pos.north()) || facing == Direction.SOUTH) {
                        velocityZ = LAUNCHING_RATE;
                    } else if (minecart.willHitBlockAt(pos.south()) || facing == Direction.NORTH) {
                        velocityZ = -LAUNCHING_RATE;
                    }
                }
                default -> { return; } //We shouldn't get here as check rails can't make turns
            }
            minecart.setVelocity(velocityX, velocity.y, velocityZ);
        }
    }

    private @Nullable Direction getMinecaftMovementDirection(AbstractMinecartEntity minecart) {
        double velocityX = minecart.getVelocity().x;
        double velocityZ = minecart.getVelocity().z;
        if (velocityX > 0.0D)
            return Direction.EAST;
        else if (velocityX < 0.0D)
            return Direction.WEST;
        if (velocityZ > 0.0D)
            return Direction.SOUTH;
        else if (velocityZ < 0.0D)
            return Direction.NORTH;
        return null;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        BlockState newState = super.rotate(state, rotation);
        return switch (rotation) {
            case CLOCKWISE_180 -> newState.with(HORIZONTAL_FACING, newState.get(HORIZONTAL_FACING).getOpposite());
            case COUNTERCLOCKWISE_90 -> newState.with(HORIZONTAL_FACING, newState.get(HORIZONTAL_FACING).rotateYCounterclockwise());
            case CLOCKWISE_90 -> newState.with(HORIZONTAL_FACING, newState.get(HORIZONTAL_FACING).rotateYClockwise());
            default -> newState;
        };
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return super.mirror(state, mirror).with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING).getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HORIZONTAL_FACING);
    }
}
