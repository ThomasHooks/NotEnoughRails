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

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BrakingRailBlock extends AbstractRailBlock {
    public static final MapCodec<BrakingRailBlock> CODEC = createCodec(BrakingRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = Properties.STRAIGHT_RAIL_SHAPE;
    public static final IntProperty POWER = Properties.POWER;
    public static final int MIN_REDSTONE_POWER = 0;
    public static final int MAX_REDSTONE_POWER = 15;
    public static final double BRAKING_FACTOR = 0.02D;

    protected BrakingRailBlock(Settings settings) {
        super(true, settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(SHAPE, RailShape.NORTH_SOUTH)
                .with(POWER, 0)
                .with(WATERLOGGED, false)
        );
    }

    protected int getPowerFromOtherRails(World world, BlockPos pos, BlockState state, boolean bl, int distance) {
        if (distance >= 8) {
            return MIN_REDSTONE_POWER;
        }

        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        boolean bl2 = true;
        RailShape railShape = state.get(SHAPE);
        switch (railShape) {
            case NORTH_SOUTH:
                if (bl) {
                    posZ++;
                } else {
                    posZ--;
                }
                break;
            case EAST_WEST:
                if (bl) {
                    posX--;
                } else {
                    posX++;
                }
                break;
            case ASCENDING_EAST:
                if (bl) {
                    posX--;
                } else {
                    posX++;
                    posY++;
                    bl2 = false;
                }
                railShape = RailShape.EAST_WEST;
                break;
            case ASCENDING_WEST:
                if (bl) {
                    posX--;
                    posY++;
                    bl2 = false;
                } else {
                    posX++;
                }
                railShape = RailShape.EAST_WEST;
                break;
            case ASCENDING_NORTH:
                if (bl) {
                    posZ++;
                } else {
                    posZ--;
                    posY++;
                    bl2 = false;
                }
                railShape = RailShape.NORTH_SOUTH;
                break;
            case ASCENDING_SOUTH:
                if (bl) {
                    posZ++;
                    posY++;
                    bl2 = false;
                } else {
                    posZ--;
                }
                railShape = RailShape.NORTH_SOUTH;
                break;
        }
        int powerFromOtherRail = this.getPowerFromOtherRails(world, new BlockPos(posX, posY, posZ), bl, distance, railShape);
        if (powerFromOtherRail > MIN_REDSTONE_POWER) {
            return powerFromOtherRail;
        }
        powerFromOtherRail = this.getPowerFromOtherRails(world, new BlockPos(posX, posY - 1, posZ), bl, distance, railShape);
        if (bl2 && powerFromOtherRail > MIN_REDSTONE_POWER) {
            return powerFromOtherRail;
        }
        return MIN_REDSTONE_POWER;
    }

    protected int getPowerFromOtherRails(World world, BlockPos pos, boolean bl, int distance, RailShape shape) {
        BlockState blockState = world.getBlockState(pos);
        if (!blockState.isOf(this)) {
            return MIN_REDSTONE_POWER;
        }

        RailShape railShape = blockState.get(SHAPE);
        if (isOrientationIncompatible(shape, railShape)) {
            return MIN_REDSTONE_POWER;
        }

        if (blockState.get(POWER) == MIN_REDSTONE_POWER) {
            return MIN_REDSTONE_POWER;
        }

        int receivedRedstonePower = world.getReceivedRedstonePower(pos);
        if (receivedRedstonePower == MIN_REDSTONE_POWER) {
            receivedRedstonePower = this.getPowerFromOtherRails(world, pos, blockState, bl, distance + 1);
        }
        return receivedRedstonePower;
    }

    private boolean isOrientationIncompatible(RailShape searchDirection, RailShape neighborShape) {
        if (searchDirection == RailShape.EAST_WEST) {
            return neighborShape == RailShape.NORTH_SOUTH
                    || neighborShape == RailShape.ASCENDING_NORTH
                    || neighborShape == RailShape.ASCENDING_SOUTH;
        }
        if (searchDirection == RailShape.NORTH_SOUTH) {
            return neighborShape == RailShape.EAST_WEST
                    || neighborShape == RailShape.ASCENDING_EAST
                    || neighborShape == RailShape.ASCENDING_WEST;
        }
        return false;
    }

    @Override
    protected void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor) {
        int currentPower = state.get(POWER);
        int receivedPower = world.getReceivedRedstonePower(pos);
        if (receivedPower == MIN_REDSTONE_POWER) {
            receivedPower = this.getPowerFromOtherRails(world, pos, state, true, 0);
            if (receivedPower == MIN_REDSTONE_POWER) {
                receivedPower = this.getPowerFromOtherRails(world, pos, state, false, 0);
            }
        }

        if (receivedPower != currentPower) {
            world.setBlockState(pos, state.with(POWER, receivedPower), Block.NOTIFY_ALL);
            world.updateNeighbors(pos.down(), this);
            if (state.get(SHAPE).isAscending()) {
                world.updateNeighbors(pos.up(), this);
            }
        }
    }

    @Override
    protected MapCodec<? extends AbstractRailBlock> getCodec() { return CODEC; }

    @Override
    public Property<RailShape> getShapeProperty() { return SHAPE; }

    @Override
    public void notEnoughRails$onMinecartPass(BlockState state, ServerWorld world, BlockPos pos, AbstractMinecartEntity minecart) {
        int  currentRedstonePower = state.get(POWER);
        if (currentRedstonePower == MIN_REDSTONE_POWER) {
            return;
        }

        //When the player is trying to move the minecart the braking rail doesn't brake
        if (minecart.getFirstPassenger() instanceof ServerPlayerEntity player) {
            Vec3d playerInputVelocity = player.getInputVelocityForMinecart();
            if (playerInputVelocity.lengthSquared() > 0.0) {
                double lengthSquared = minecart.getVelocity().horizontalLengthSquared();
                if (playerInputVelocity.normalize().lengthSquared() > 0.0 && lengthSquared < 0.01) {
                    return;
                }
            }
        }

        //Braking Minecart
        double horizontalLength = minecart.getVelocity().horizontalLength();
        if (horizontalLength < 0.03) {
            minecart.setVelocity(Vec3d.ZERO);
        } else {
            double braking = 1.0D - (BRAKING_FACTOR * currentRedstonePower);
            minecart.setVelocity(minecart.getVelocity().multiply(braking, 0.0D, braking));
        }
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        RailShape railShape = state.get(SHAPE);
        RailShape rotatedShape = this.rotateShape(railShape, rotation);
        return state.with(SHAPE, rotatedShape);
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        RailShape railShape = state.get(SHAPE);
        RailShape mirroredShape = this.mirrorShape(railShape, mirror);
        return state.with(SHAPE, mirroredShape);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWER, WATERLOGGED);
    }
}
