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

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;
import org.jspecify.annotations.Nullable;

/**
 * A property that is used to determine which direction redstone blocks are receiving redstone power from.
 */
public enum PowerDirection implements StringIdentifiable {
    DOWN("down"),
    UP("up"),
    NORTH("north"),
    SOUTH("south"),
    WEST("west"),
    EAST("east"),
    NONE("none");

    private final String name;

    PowerDirection(String name) { this.name = name; }

    public String toString() { return this.asString(); }

    @Override
    public String asString() { return name; }

    public PowerDirection getOpposite() {
        return switch (this) {
            case DOWN -> PowerDirection.UP;
            case UP -> PowerDirection.DOWN;
            case NORTH -> PowerDirection.SOUTH;
            case SOUTH -> PowerDirection.NORTH;
            case WEST -> PowerDirection.EAST;
            case EAST -> PowerDirection.WEST;
            default -> PowerDirection.NONE;
        };
    }

    public @Nullable Direction asDirection() {
        return switch (this) {
            case DOWN -> Direction.DOWN;
            case UP -> Direction.UP;
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
            case EAST -> Direction.EAST;
            default -> null;
        };
    }

    public static PowerDirection byDirection(@Nullable Direction direction) {
        if (direction == null)
            return PowerDirection.NONE;

        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            case DOWN -> DOWN;
            case UP -> UP;
            default -> NONE;
        };
    }
}
