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

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a mechanical connection for a kinematic block
 * @param pos    The location of the kinematic block's connection
 * @param facing The direction that the connection is facing away from this kinematic block
 * @param type   The mechanical connection type at this location
 */
public record MechanicalConnection(BlockPos pos, Direction facing, MechanicalConnection.Type type) {
    /**
     * Represents the different forms of mechanical connections</br>
     * AXLE   - Represents a straight shaft</br>
     * COG    - Represents a cogwheel</br>
     * PINION - Represents a lantern pinion</br>
     */
    public enum Type {
        AXLE,
        COG,
        PINION;

        /**
         * Checks whether the given {@link MechanicalConnection.Type} can connect
         * @param other The second mechanical connection type to check
         * @return True if they can connect to each other, false otherwise
         */
        public boolean canConnect(MechanicalConnection.Type other) {
            return switch (this) {
                case AXLE -> other == Type.AXLE;
                case COG -> other == Type.COG || other == Type.PINION;
                case PINION -> other == Type.COG;
            };
        }
    }

    public boolean isAxle() { return this.type == Type.AXLE; }

    public boolean isCog() { return this.type == Type.COG; }

    public boolean isPinion() { return this.type == Type.PINION; }

    /**
     * Creates a new 2-way axle mechanical connection array
     * <p>
     * The axis must be known ahead of time, and must never change
     * @param pos  The position of the kinematic block
     * @param axis The axis that the kinematic block is aligned with
     * @return The new mechanical connection array
     */
    public static @NotNull ArrayList<MechanicalConnection> makeMonoAxle(BlockPos pos, Direction.Axis axis) {
        return switch (axis) {
            case X -> new ArrayList<>(Arrays.asList(
                    new MechanicalConnection(pos.east(), Direction.EAST, Type.AXLE),
                    new MechanicalConnection(pos.west(), Direction.WEST, Type.AXLE)
            ));
            case Y -> new ArrayList<>(Arrays.asList(
                    new MechanicalConnection(pos.up(), Direction.UP, Type.AXLE),
                    new MechanicalConnection(pos.down(), Direction.DOWN, Type.AXLE)
            ));
            case Z -> new ArrayList<>(Arrays.asList(
                    new MechanicalConnection(pos.north(), Direction.NORTH, Type.AXLE),
                    new MechanicalConnection(pos.south(), Direction.SOUTH, Type.AXLE)
            ));
        };
    }

    /**
     * Creates a new small cogwheel mechanical connection array
     * <p>
     * The axis must be known ahead of time and must never change
     * @param pos  The position of the kinematic block
     * @param axis The axis that the kinematic block is aligned with
     * @return The new mechanical connection array
     */
    public static ArrayList<MechanicalConnection> makeSmallCogwheel(BlockPos pos, Direction.Axis axis) {
        return switch (axis) {
            case X -> new ArrayList<>(Arrays.asList(
                    new MechanicalConnection(pos.east(), Direction.EAST, Type.AXLE),
                    new MechanicalConnection(pos.west(), Direction.WEST, Type.AXLE),
                    new MechanicalConnection(pos.north(), Direction.NORTH, Type.COG),
                    new MechanicalConnection(pos.south(), Direction.SOUTH, Type.COG),
                    new MechanicalConnection(pos.up(), Direction.UP, Type.COG),
                    new MechanicalConnection(pos.down(), Direction.DOWN, Type.COG)
            ));
            case Y -> new ArrayList<>(Arrays.asList(
                    new MechanicalConnection(pos.east(), Direction.EAST, Type.COG),
                    new MechanicalConnection(pos.west(), Direction.WEST, Type.COG),
                    new MechanicalConnection(pos.north(), Direction.NORTH, Type.COG),
                    new MechanicalConnection(pos.south(), Direction.SOUTH, Type.COG),
                    new MechanicalConnection(pos.up(), Direction.UP, Type.AXLE),
                    new MechanicalConnection(pos.down(), Direction.DOWN, Type.AXLE)
            ));
            case Z -> new ArrayList<>(Arrays.asList(
                    new MechanicalConnection(pos.east(), Direction.EAST, Type.COG),
                    new MechanicalConnection(pos.west(), Direction.WEST, Type.COG),
                    new MechanicalConnection(pos.north(), Direction.NORTH, Type.AXLE),
                    new MechanicalConnection(pos.south(), Direction.SOUTH, Type.AXLE),
                    new MechanicalConnection(pos.up(), Direction.UP, Type.COG),
                    new MechanicalConnection(pos.down(), Direction.DOWN, Type.COG)
            ));
        };
    }
}
