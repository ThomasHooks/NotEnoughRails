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
package com.github.thomashooks.notenoughrails.energy;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public interface KinematicBlockProvider {
    /**
     * @param world The kinematic block's world
     * @param pos   The kinematic block's position
     * @return Gets the kinematic block's current speed
     */
    float getSpeed(@NotNull World world, @NotNull BlockPos pos);

    /**
     * Changes this kinematic block's speed
     * @param world     The kinematic block's world
     * @param pos       The kinematic block's position
     * @param driverPos The driving kinematic block's position
     * @param speedIn   The new speed
     */
    void changeSpeed(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockPos driverPos, float speedIn);

    /**
     * Sets the number of teeth for this kinematic block
     * <p>
     * Note: This method is only used by kinematic block's with cog mechanical connections
     * @return The number of teeth of this kinematic block, by default it is 1
     */
    default float getNumberOfTeeth() { return 1.0F; }

    /**
     * Gets an array that contains this kinematic block's mechanical connection points
     * @param world The kinematic block's world
     * @param pos   The position of the block
     * @param state This kinematic block's current state
     * @return The mechanical connections that this kinematic block has
     */
    @NotNull ArrayList<MechanicalConnection> getMechanicalConnections(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state);

    /**
     * Checks if the given Mechanical connection is aligned with this kinematic block
     * @param world              This kinematic block's World
     * @param pos                This kinematic block's position
     * @param state              This kinematic block's current Block State
     * @param neighborConnection The mechanical connection that is being checked for alignment
     * @return True if the given mechanical connection is aligned with this kinematic block
     */
    default boolean isAlignedWith(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull MechanicalConnection neighborConnection) {
        for (MechanicalConnection connection : getMechanicalConnections(world, pos, state)) {
            boolean sameFacing = connection.facing() == neighborConnection.facing().getOpposite();
            boolean areBothAxles = connection.isAxle() && neighborConnection.isAxle();
            boolean areBothCogs = connection.isCog() && neighborConnection.isCog();
            if (!sameFacing || (!areBothAxles && !areBothCogs)) {
                continue;
            }

            boolean sameXCoordinate = connection.pos().getX() == neighborConnection.pos().getX();
            boolean sameYCoordinate = connection.pos().getY() == neighborConnection.pos().getY();
            boolean sameZCoordinate = connection.pos().getZ() == neighborConnection.pos().getZ();
            switch (connection.facing()) {
                case NORTH, SOUTH:
                    if (sameXCoordinate && sameYCoordinate)
                        return true;
                    break;
                case EAST, WEST:
                    if (sameYCoordinate && sameZCoordinate)
                        return true;
                    break;
                case UP, DOWN:
                     if (sameXCoordinate && sameZCoordinate)
                         return true;
                     break;
                default:
                    throw  new IllegalArgumentException(String.format("Invalid facing state: %s, %s", connection.facing(), world.getBlockState(pos).getBlock().getName()));
            }
        }
        return false;
    }

    /**
     * Gets the kinematic block at the given location
     * @param blockView The block accessor
     * @param pos       The position of the kinematic block
     * @return The kinematic block at the given location, or null if it's not a kinematic block
     */
    static @Nullable KinematicBlockProvider getKinematicBlock(@NotNull BlockView blockView, @NotNull BlockPos pos) {
        if (blockView.getBlockState(pos).getBlock() instanceof KinematicBlockProvider blockProvider) {
            return blockProvider;
        } else {
            return null;
        }
    }
}
