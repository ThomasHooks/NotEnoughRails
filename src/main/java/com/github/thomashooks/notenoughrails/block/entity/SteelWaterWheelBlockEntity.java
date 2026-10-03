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
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class SteelWaterWheelBlockEntity extends LazyTickingBlockEntity {
    public static final float MAX_NUMBER_OF_SIDES_WITH_WATER = 3.0F;
    private final RotatingShaftAnimator shaftAnimator = new RotatingShaftAnimator();

    private float speed;
    public static final double MAX_SPEED = 12.0;
    private static final String SPEED_TAG = NotEnoughRails.MOD_ID + ":speed";

    public SteelWaterWheelBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.WATERWHEEL_STEEL, pos, state);
        setLazyTickRate(30);
    }

    //region Lazy Ticking Methods
    @Override
    protected void lazyTick() {
        updateFlows();
    }

    @Override
    protected void clientTick() {
        super.clientTick();

        shaftAnimator.step(getSpeed());
    }
    //endregion

    public float getSpeed() { return speed; }

    private void setSpeed(float speedIn) {
        if (!MathHelper.approximatelyEquals(speedIn, this.speed)) {
            this.speed = speedIn;
            updateAndNotifyAll();
        }
    }

    private void updateFlows() {
        // TODO: Add support for other axes
        float flowSpeed = 0.0F;
        for (Direction direction : Direction.Type.HORIZONTAL) {
            BlockPos fluidPos = getPos().offset(direction);
            FluidState fluidState = Objects.requireNonNull(getWorld()).getFluidState(fluidPos);
            if (fluidState.isEmpty()) {
                continue;
            }
            Vec3d flow = fluidState.getVelocity(getWorld(), fluidPos).multiply(MAX_SPEED);
            switch (direction) {
                /*
                 * 				North (-Z)
                 *
                 * West (-X)					East (+X)
                 *
                 * 				South (+Z)
                 *
                 * Because counter-clockwise is positive rotation, the flow for both north and east must be negated
                 */
                case EAST -> flowSpeed += (float) -flow.getZ();
                case NORTH -> flowSpeed += (float) -flow.getX();
                case WEST -> flowSpeed += (float) flow.getZ();
                case SOUTH ->  flowSpeed += (float) flow.getX();
                default -> throw new IllegalStateException(String.format("SteelWaterWheelBlockEntity: Invalid direction: %s", direction));
            }
        }
        setSpeed(flowSpeed / MAX_NUMBER_OF_SIDES_WITH_WATER);
    }

    //region Block Entity Renderer Methods
    public float getAnimationProgress(float tickProgress) { return shaftAnimator.getShaftAngle(tickProgress); }
    //endregion

    //region Serialize and Deserialize Methods
    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) { return createNbt(registries); }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        if (view.contains(SPEED_TAG)) {
            this.speed = view.getFloat(SPEED_TAG, 0.0F);
        }
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putFloat(SPEED_TAG, this.speed);
    }
    //endregion
}
