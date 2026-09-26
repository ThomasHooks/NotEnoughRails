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
package com.github.thomashooks.notenoughrails.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class VoxelShapeHelper {
    protected static final Map<Direction.Axis, Integer> AXIS_LOOKUP;
    static {
        AXIS_LOOKUP = new HashMap<>();
        AXIS_LOOKUP.put(Direction.Axis.X, 0);
        AXIS_LOOKUP.put(Direction.Axis.Y, 1);
        AXIS_LOOKUP.put(Direction.Axis.Z, 2);
    }

    public static final VoxelShape QUARTER_BLOCK = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
    public static final VoxelShape HALF_BLOCK = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    public static final VoxelShape FULL_BLOCK = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    public static final VoxelShape[] AXLE = new VoxelShape[]{
            Block.createCuboidShape(0.0, 6.0, 6.0, 16.0, 10.0, 10.0), // X-axis
            Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0), // Y-axis
            Block.createCuboidShape(6.0, 6.0, 0.0, 16.0, 10.0, 16.0)  // Z-axis
    };

    /**
     * Gets the voxel shape helper axis index
     * @param axis The axis to get the index for
     * @return The voxel shape helper axis index for the given axis
     */
    public static int getAxisIndex(@NotNull Direction.Axis axis) { return AXIS_LOOKUP.get(axis); }
}
