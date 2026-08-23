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

import java.util.HashMap;
import java.util.Map;

public class VoxelShapeHelper {
    public static final Map<Direction.Axis, Integer> AXIS_LOOKUP;
    static {
        AXIS_LOOKUP = new HashMap<>();
        AXIS_LOOKUP.put(Direction.Axis.X, 0);
        AXIS_LOOKUP.put(Direction.Axis.Y, 1);
        AXIS_LOOKUP.put(Direction.Axis.Z, 2);
    }

    public static final VoxelShape BASE_16X4X16 = Block.createCuboidShape(0, 0, 0, 16, 4, 16);
    public static final VoxelShape FULL_BLOCK = Block.createCuboidShape(0, 0, 0, 16, 16, 16);
}
