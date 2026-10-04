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
 * @param pos    The location of the machine's connection
 * @param facing The direction that the connection is facing away from this machine
 * @param type   The machine connection type
 */
public record MechanicalConnection(BlockPos pos, Direction facing, MechanicalConnection.Type type) {
    public enum Type {
        AXLE,
        COG
    }

    public boolean isAxle() { return this.type == Type.AXLE; }

    public boolean isCog() { return this.type == Type.COG; }

    /**
     * Creates a new 2-way mechanical axle connection array
     * <p>
     * The axis must be known ahead of time, and must never change
     * @param pos The position of the machine
     * @param axis The axis that the machine is aligned with
     * @return New 2-way mechanical axle connection array
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
}
