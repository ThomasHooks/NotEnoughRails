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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class LazyTickingBlockEntity extends BlockEntity {
    public final int TICKS_PER_SECOND = 20;
    private int lazyTickRate = 0;
    private int ticks = 0;

    protected LazyTickingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(TICKS_PER_SECOND * 2);
    }

    /**
     * Marks this block entity as dirty and that it needs to be saved.
     * It also notifies neighbors, listeners, and forces a redraw on clients.
     * <p>
     * This should be called when something changed in a way that affects the saved NBT;
     * otherwise, the game might not save the block entity
     */
    protected void updateAndNotifyAll() {
        markDirty();
        if (world != null && !world.isClient()) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    /**
     * This gets called by the server side approximately 20 times every second.
     * <p>
     * If overridden the super must be called.
     */
    protected void tick() {
        if (world == null)
            return;
        if (pollLazyTimer())
            lazyTick();
    }

    /**
     * This gets called by the client side approximately 20 times every second.
     * <p>
     * If overridden the super must be called.
     */
    protected void clientTick() {
        if (world == null)
            return;
        if (pollLazyTimer())
            clientLazyTick();
    }

    /**
     * This gets called by the server side at a rate controlled by {@link #setLazyTickRate(int)}
     */
    protected void lazyTick() {
        //Do nothing
    }

    /**
     * This gets called by the client side at a rate controlled by {@link #setLazyTickRate(int)}
     */
    protected void clientLazyTick() {
        //Do nothing
    }

    /**
     * Checks if a lazy tick has happened
     * @return True if there is a lazy tick
     */
    protected boolean pollLazyTimer() {
        ticks++;
        if (ticks >= getLazyTickRate()) {
            ticks = 0;
            return true;
        }
        return false;
    }

    /**
     * Sets this Block Entity's lazy tick rate
     * <p>
     * By default, a lazy tick happens every 40 standard ticks
     * @param tickRateNew The new lazy tick rate given in standard ticks
     */
    protected void setLazyTickRate(int tickRateNew) { this.lazyTickRate = Math.abs(tickRateNew); }

    /**
     * @return Gets the lazy tick rate in standard ticks
     */
    protected int getLazyTickRate() { return this.lazyTickRate; }

    /**
     * Helper method used to hook {@link #tick()} and {@link #clientTick()} into blocks
     */
    public static <T extends BlockEntity> BlockEntityTicker<T> getTicker(World worldIn) {
        return worldIn.isClient() ? (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof LazyTickingBlockEntity tickingBlockEntity) {
                tickingBlockEntity.clientTick();
            }
        } : (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof LazyTickingBlockEntity tickingBlockEntity) {
                tickingBlockEntity.tick();
            }
        };
    }
}
