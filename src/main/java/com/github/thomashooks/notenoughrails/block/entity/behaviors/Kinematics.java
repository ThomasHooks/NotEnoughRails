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
package com.github.thomashooks.notenoughrails.block.entity.behaviors;

import com.github.thomashooks.notenoughrails.NotEnoughRails;
import com.github.thomashooks.notenoughrails.energy.KinematicBlockProvider;
import com.github.thomashooks.notenoughrails.energy.MechanicalConnection;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public abstract class Kinematics {
    protected float speed;
    protected BlockPos driverPos;
    protected static final String SPEED_TAG = NotEnoughRails.MOD_ID + ":speed";
    protected static final String DRIVER_POS_TAG = NotEnoughRails.MOD_ID + ":driver_pos";

    /**
     * @return Gets the block position of the kinematic block that is powering this block
     */
    public BlockPos getDriverPos() { return this.driverPos; }

    protected void setDriverPos(BlockPos driverPosIn) { this.driverPos = driverPosIn; }

    /**
     * @return Gets the current speed of this kinematic block
     */
    public float getSpeed() { return this.speed; }

    /**
     * Sets the speed of this kinematic block
     * @param driverPosIn The block position of the driving kinematic block
     * @param speedIn     The new speed for this kinematic block
     */
    public void setSpeed(BlockPos driverPosIn, float speedIn) {
        if (MathHelper.approximatelyEquals(getSpeed(), speedIn) || (getDriverPos() != null && !getDriverPos().equals(driverPosIn))) {
            return;
        }

        this.speed = speedIn;
        if (MathHelper.approximatelyEquals(0.0F, speedIn)) {
            setDriverPos(null);
        } else {
            setDriverPos(driverPosIn);
        }
        markDirty();
    }

    /**
     * @return Gets if this kinematic block is moving
     */
    public boolean isMoving() { return !MathHelper.approximatelyEquals(getSpeed(), 0.0F); }

    /**
     * Stops this kinematic block's movement
     */
    public void stop() {
        this.speed = 0.0f;
        setDriverPos(null);
        markDirty();
    }

    /**
     * Attempts to drive the next kinematic block that is attached to this kinematic block
     * @param blockProvider This kinematic block
     * @param world         The world that this kinematic block is in
     * @param pos           The block position of this kinematic block
     */
    public void propagateSpeed(KinematicBlockProvider blockProvider, World world, BlockPos pos) {
        if (!isDriverPresent(world)) {
            stop();
        }

        for (MechanicalConnection connection : blockProvider.getMechanicalConnections(world, pos, world.getBlockState(pos))) {
            if (connection.pos().equals(getDriverPos())) {
                continue;
            }
            KinematicBlockProvider neighborBlockProvider = KinematicBlockProvider.getKinematicBlock(world, connection.pos());
            if (neighborBlockProvider == null || !neighborBlockProvider.isAlignedWith(world, connection.pos(), world.getBlockState(connection.pos()), connection)) {
                continue;
            }

            if (connection.isAxle()) {
                neighborBlockProvider.changeSpeed(world, connection.pos(), pos, getSpeed());
            }
            else if (connection.isCog()) {
                float gearRatio = blockProvider.getNumberOfTeeth() / neighborBlockProvider.getNumberOfTeeth();
                neighborBlockProvider.changeSpeed(world, connection.pos(), pos, getSpeed() * -gearRatio);
            }
        }
    }

    /**
     * Checks if the driving kinematic block is still present
     * @param world The world that the kinematic block is in
     * @return True if the driving kinematic block is still present
     */
    protected boolean isDriverPresent(World world) {
        return getDriverPos() != null && world.getBlockState(getDriverPos()).getBlock() instanceof  KinematicBlockProvider;
    }

    /**
     * Marks this Kinematics as dirty and that it needs to be saved
     */
    abstract public void markDirty();

    /**
     * Reads data from NBT
     * @param view The NBT storage to read from
     */
    public void readData(ReadView view) {
        if (view.contains(SPEED_TAG)) {
            this.speed = view.getFloat(SPEED_TAG, 0.0F);
        }
        if (view.contains(DRIVER_POS_TAG)) {
            this.driverPos = new BlockPos(view.read(DRIVER_POS_TAG, Vec3i.CODEC).orElse(Vec3i.ZERO));
        }
    }

    /**
     * Writes data to NBT
     * @param view The NBT storage to write to
     */
    public void writeData(WriteView view) {
        view.putFloat(SPEED_TAG, this.speed);
        if (this.driverPos != null) {
            view.put(DRIVER_POS_TAG, Vec3i.CODEC, new Vec3i(this.driverPos.getX(), this.driverPos.getY(), this.driverPos.getZ()));
        }
    }
}
