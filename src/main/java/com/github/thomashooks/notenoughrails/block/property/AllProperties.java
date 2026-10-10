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
package com.github.thomashooks.notenoughrails.block.property;

import net.minecraft.block.enums.RailShape;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;

public class AllProperties {
    /**
     * A property that specifies the two directions a rail connects to.
     * This property does not allow for a rail to turn or to make slopes.
     */
    public static final EnumProperty<RailShape> FLAT_RAIL_SHAPE = EnumProperty.of(
            "shape",
            RailShape.class,
            shape -> shape != RailShape.NORTH_EAST && shape != RailShape.NORTH_WEST && shape != RailShape.SOUTH_EAST && shape != RailShape.SOUTH_WEST
                    && shape != RailShape.ASCENDING_NORTH && shape != RailShape.ASCENDING_EAST && shape != RailShape.ASCENDING_SOUTH && shape != RailShape.ASCENDING_WEST
    );
    public static final EnumProperty<PowerDirection> POWER_DIRECTION =  EnumProperty.of("power_direction", PowerDirection.class);
    /**
     * A property that specifies if a block is milling an item.
     */
    public static final BooleanProperty IS_MILLING = BooleanProperty.of("is_milling");
    /**
     * A property that specifies if a kinematic block is spinning to fast
     */
    public static final BooleanProperty IS_OVERLOADED = BooleanProperty.of("is_overloaded");
    /**
     * A property that specifies if a kinematic block shaft is shifted by 22.5 degrees
     */
    public static final BooleanProperty SHAFT_IS_SHIFTED = BooleanProperty.of("shaft_is_shifted");
}
