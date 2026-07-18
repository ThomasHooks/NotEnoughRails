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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class LockingRailBlockEntity extends BlockEntity {
    private AbstractMinecartEntity  minecart;
    private UUID minecartId;
    private double minecartDeltaX = 0.0D;
    private double minecartDeltaZ = 0.0D;
    private final String MINECART_ID_TAG = NotEnoughRails.MOD_ID + "_minecart_id";
    private final String MINECART_DELTA_X_TAG = NotEnoughRails.MOD_ID + "_minecart_delta_x";
    private final String MINECART_DELTA_Z_TAG = NotEnoughRails.MOD_ID + "_minecart_delta_z";

    public LockingRailBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.LOCKING_RAIL, pos, state);
    }

    /**
     * Locks the given minecart to this rail
     * @param minecart The minecart to be locked onto this rail
     */
    public void lockMinecart(@NotNull AbstractMinecartEntity minecart) {
        // This is just here to get the minecart back after loading
        if (!Objects.requireNonNull(getWorld()).isClient() && this.minecartId != null && this.minecart ==  null) {
            Entity entity = getWorld().getEntity(this.minecartId);
            if (entity instanceof AbstractMinecartEntity minecartEntity) {
                this.minecart = minecartEntity;
            }
        }
        if (!this.isMinecartLocked()) {
            this.minecart = minecart;
            this.minecartId = minecart.getUuid();
            this.minecartDeltaX = minecart.getVelocity().getX();
            this.minecartDeltaZ = minecart.getVelocity().getZ();
            markDirty();
            getWorld().playSound(null, getPos(), SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 1.0F, 1.5F);
        }
        minecart.setVelocity(0.0, 0.0, 0.0);
        minecart.setPosition(getPos().getX() + 0.5, getPos().getY(), getPos().getZ() + 0.5);
    }

    /**
     * Unlocks any minecarts that are currently locked on this rail
     */
    public void unlockMinecart() {
        if (this.isMinecartLocked()) {
            this.minecart.setVelocity(this.minecartDeltaX, 0.0D, this.minecartDeltaZ);
            Objects.requireNonNull(getWorld()).playSound(null, getPos(), SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 1.0F, 1.5F);
            this.minecart = null;
            this.minecartId = null;
            this.minecartDeltaX = 0.0D;
            this.minecartDeltaZ = 0.0D;
            markDirty();
        }
    }

    /**
     * @return Returns true if a minecart is currently locked on this rail
     */
    public boolean isMinecartLocked() { return this.minecartId != null; }

    @Override
    protected void readData(ReadView view) {
        if (view.contains(MINECART_ID_TAG)) {
            this.minecartId = view.read(MINECART_ID_TAG, Uuids.INT_STREAM_CODEC).orElse(null);
            this.minecartDeltaX = view.getDouble(MINECART_DELTA_X_TAG, 0.0);
            this.minecartDeltaZ = view.getDouble(MINECART_DELTA_Z_TAG, 0.0);
        }
        super.readData(view);
    }

    @Override
    protected void writeData(WriteView view) {
        if (this.minecartId != null) {
            view.putNullable(MINECART_ID_TAG, Uuids.INT_STREAM_CODEC, this.minecartId);
            view.putDouble(MINECART_DELTA_X_TAG, this.minecartDeltaX);
            view.putDouble(MINECART_DELTA_Z_TAG, this.minecartDeltaZ);
        }
        super.writeData(view);
    }
}
