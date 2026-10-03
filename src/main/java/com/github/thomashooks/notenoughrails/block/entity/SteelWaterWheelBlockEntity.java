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
package com.github.thomashooks.notenoughrails.block.entity;

import com.github.thomashooks.notenoughrails.NotEnoughRails;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class SteelWaterWheelBlockEntity extends LazyTickingBlockEntity {

    public SteelWaterWheelBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.WATERWHEEL_STEEL, pos, state);
        setLazyTickRate(20);
    }

    @Override
    protected void lazyTick() {
        // REMOVE: this is only for testing
        NotEnoughRails.LOGGER.info("SteelWaterWheelBlockEntity.lazyTick at {}", getLazyTickRate());
    }
}
