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
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CoritePoweredRail extends PoweredRailBlock {
    public static final double BOOSTING_FACTOR = 0.06D;
    public static final double LAUNCHING_RATE = 0.02D;
    public static final double BRAKING_FACTOR = 0.05D;

    public CoritePoweredRail(Settings settings) {
        super(settings);
    }

    @Override
    public float notEnoughRails$getMaxSpeed(BlockState state, BlockPos pos, AbstractMinecartEntity minecart) {
        return minecart.isInFluid() ? WATERLOGGED_MAX_SPEED : DEFAULT_MAX_SPEED * 4.0F;
    }

    @Override
    public void notEnoughRails$onMinecartPass(BlockState state, ServerWorld world, BlockPos pos, AbstractMinecartEntity minecart) {
        if (!state.get(POWERED)) {
            if (minecart.getFirstPassenger() instanceof ServerPlayerEntity player) {
                Vec3d playerInputVelocity = player.getInputVelocityForMinecart();
                if (playerInputVelocity.lengthSquared() > 0.0) {
                    double lengthSquared = minecart.getVelocity().horizontalLengthSquared();
                    if (playerInputVelocity.normalize().lengthSquared() > 0.0 && lengthSquared < 0.01) {
                        //When the player is trying to move the minecart the powered rail doesn't brake
                        return;
                    }
                }
            }
            //Braking Minecart
            double horizontalLength = minecart.getVelocity().horizontalLength();
            if (horizontalLength < 0.03) {
                minecart.setVelocity(Vec3d.ZERO);
            } else {
                minecart.setVelocity(minecart.getVelocity().multiply(BRAKING_FACTOR, 0.0D, BRAKING_FACTOR));
            }
            return;
        }

        Vec3d velocity = minecart.getVelocity();
        double horizontalLength = velocity.horizontalLength();
        if (horizontalLength > 0.01D) {
            //Boosting minecart
            minecart.setVelocity(velocity.add(velocity.x / horizontalLength * BOOSTING_FACTOR, 0.0D, velocity.z / horizontalLength * BOOSTING_FACTOR));
        } else {
            //Launching minecart
            double velocityX = velocity.x;
            double velocityZ = velocity.z;
            RailShape railShape = ((AbstractRailBlock)state.getBlock()).notEnoughRails$getRailDirection(state, world ,pos, minecart);
            switch (railShape) {
                case EAST_WEST -> {
                    if (minecart.willHitBlockAt(pos.west())) {
                        velocityX = LAUNCHING_RATE;
                    } else if (minecart.willHitBlockAt(pos.east())) {
                        velocityX = -LAUNCHING_RATE;
                    }
                }
                case NORTH_SOUTH -> {
                    if (minecart.willHitBlockAt(pos.north())) {
                        velocityZ = LAUNCHING_RATE;
                    } else if (minecart.willHitBlockAt(pos.south())) {
                        velocityZ = -LAUNCHING_RATE;
                    }
                }
                default -> { return; } //We shouldn't get here as powered rails can't make turns
            }
            minecart.setVelocity(velocityX, velocity.y, velocityZ);
        }
    }
}
