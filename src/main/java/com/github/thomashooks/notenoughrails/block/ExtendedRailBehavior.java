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

import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public interface ExtendedRailBehavior {
    /**
     * @param state    - The Rail's current block state
     * @param world    - The current world
     * @param pos      - The Rails position in the world
     * @return Gets if the rail is forbidding from making slopes
     */
    default boolean notEnoughRails$isFlatRail(BlockState state, World world, BlockPos pos) { return false; }

    /**
     * @param state    - The Rail's current block state
     * @param world    - The current world
     * @param pos      - The Rails position in the world
     * @return Gets if the rail is a powered rail and should be powered by other rails
     */
    default boolean notEnoughRails$isPoweredRail(BlockState state, World world, BlockPos pos) { return false; }

    /**
     * This can be used to trick minecarts into thinking the rail has a different shape
     * @param state    - The Rail's current block state
     * @param world    - The current world
     * @param pos      - The Rails position in the world
     * @param minecart - The minecart that is passing over the rail
     * @return Gets the rail's current direction
     */
    default RailShape notEnoughRails$getRailDirection(BlockState state, World world, BlockPos pos, @Nullable AbstractMinecartEntity minecart) {
        if (state.getBlock() instanceof AbstractRailBlock railBlock) {
            return state.get(railBlock.getShapeProperty());
        } else {
            throw new IllegalStateException("Block '" + state.getBlock().getTranslationKey() + "' is in an unknown state while getting rail direction!");
        }
    }

    /**
     * @param state    - The Rail's current block state
     * @param pos      - The Rails position in the world
     * @param minecart - The minecart that is passing over the rail
     * @return Gets the maximum speed a minecart can have when passing over this rail
     */
    default float notEnoughRails$getMaxSpeed(BlockState state, BlockPos pos, AbstractMinecartEntity minecart) {
        return minecart.isInFluid() ? 0.2F : 0.4F;
    }

    /**
     * Called by minecarts once per tick when they pass over this rail block
     * @param state    - The Rail's current block state
     * @param world    - The current world
     * @param pos      - The Rails position in the world
     * @param minecart - The minecart that is passing over the rail
     */
    default void notEnoughRails$onMinecartPass(BlockState state, ServerWorld world, BlockPos pos, AbstractMinecartEntity minecart) {
        boolean boostMinecart = false;
        boolean brakeMinecart = false;
        if (state.isOf(Blocks.POWERED_RAIL)) {
            boostMinecart = state.get(PoweredRailBlock.POWERED);
            brakeMinecart = !boostMinecart;
        }

        if (minecart.getFirstPassenger() instanceof ServerPlayerEntity player) {
            Vec3d playerInputVelocity = player.getInputVelocityForMinecart();
            if (playerInputVelocity.lengthSquared() > 0.0) {
                double lengthSquared = minecart.getVelocity().horizontalLengthSquared();
                if (playerInputVelocity.normalize().lengthSquared() > 0.0 && lengthSquared < 0.01) {
                    //When the player is trying to move the minecart the powered rail doesn't brake
                    brakeMinecart = false;
                }
            }
        }

        //Default powered rail braking
        if (brakeMinecart) {
            double horizontalLength = minecart.getVelocity().horizontalLength();
            if (horizontalLength < 0.03) {
                minecart.setVelocity(Vec3d.ZERO);
            } else {
                minecart.setVelocity(minecart.getVelocity().multiply(0.5, 0.0, 0.5));
            }
        }

        //Default powered rail boosting
        if (boostMinecart) {
            Vec3d velocity = minecart.getVelocity();
            double horizontalLength = velocity.horizontalLength();
            if (horizontalLength > 0.01) {
                double y = 0.06;
                minecart.setVelocity(velocity.add(velocity.x / horizontalLength * y, 0.0, velocity.z / horizontalLength * y));
            } else {
                double velocityX = velocity.x;
                double velocityZ = velocity.z;
                RailShape railShape = ((AbstractRailBlock)state.getBlock()).notEnoughRails$getRailDirection(state, world ,pos, minecart);
                switch (railShape) {
                    case EAST_WEST -> {
                        if (minecart.willHitBlockAt(pos.west())) {
                            velocityX = 0.02;
                        } else if (minecart.willHitBlockAt(pos.east())) {
                            velocityX = -0.02;
                        }
                    }
                    case NORTH_SOUTH -> {
                        if (minecart.willHitBlockAt(pos.north())) {
                            velocityZ = 0.02;
                        } else if (minecart.willHitBlockAt(pos.south())) {
                            velocityZ = -0.02;
                        }
                    }
                    default -> { return; } //We shouldn't get here as powered rails can't make turns
                }
                minecart.setVelocity(velocityX, velocity.y, velocityZ);
            }
        }
    }
}
