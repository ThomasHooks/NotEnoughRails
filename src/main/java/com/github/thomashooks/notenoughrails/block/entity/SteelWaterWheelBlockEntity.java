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

import com.github.thomashooks.notenoughrails.block.entity.behaviors.Kinematics;
import com.github.thomashooks.notenoughrails.block.entity.behaviors.RotatingShaftProvider;
import com.github.thomashooks.notenoughrails.block.entity.behaviors.RotatingShaftAnimator;
import com.github.thomashooks.notenoughrails.energy.KinematicBlockProvider;
import com.github.thomashooks.notenoughrails.energy.MechanicalConnection;
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
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class SteelWaterWheelBlockEntity extends LazyTickingBlockEntity implements RotatingShaftProvider {
    //region Kinematics Anonymous Class
    private final Kinematics kinematics = new Kinematics() {
        @Override
        public void markDirty() { updateAndNotifyAll(); }

        @Override
        public BlockPos getDriverPos() { return getPos(); }

        @Override
        protected void setDriverPos(BlockPos driverPosIn) {
            // The steel water wheel is its own driver
        }

        @Override
        public void setSpeed(BlockPos driverPosIn, float speedIn) {
            if (MathHelper.approximatelyEquals(getSpeed(), speedIn) || !getDriverPos().equals(driverPosIn)) {
                return;
            }

            this.speed = speedIn;
            markDirty();
        }

        @Override
        public void stop() {
            // Do nothing
        }
    };
    private final ArrayList<MechanicalConnection> connections = MechanicalConnection.makeMonoAxle(getPos(), Direction.Axis.Y);
    public static final float MAX_NUMBER_OF_SIDES_WITH_WATER = 3.0F;
    public static final double MAX_SPEED = 12.0;
    //endregion

    private final RotatingShaftAnimator shaftAnimator = new RotatingShaftAnimator();

    public SteelWaterWheelBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.WATERWHEEL_STEEL, pos, state);
        setLazyTickRate(30);
    }

    //region Lazy Ticking Methods
    @Override
    protected void tick() {
        super.tick();

        propagateSpeed();
    }

    @Override
    protected void lazyTick() {
        updateFlows();
    }

    @Override
    protected void clientTick() {
        super.clientTick();

        this.shaftAnimator.step(getSpeed());
    }
    //endregion

    //region Kinematics Methods
    public @NotNull ArrayList<MechanicalConnection> getMechanicalConnections() { return connections; }

    public float getSpeed() { return this.kinematics.getSpeed(); }

    protected void propagateSpeed() {
        this.kinematics.propagateSpeed((KinematicBlockProvider) getCachedState().getBlock(), getWorld(), getPos());
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
        this.kinematics.setSpeed(getPos(),flowSpeed / MAX_NUMBER_OF_SIDES_WITH_WATER);
    }
    //endregion

    //region Block Entity Renderer Methods
    public float getRotatingShaftProgress(float tickProgress) { return shaftAnimator.getShaftAngle(tickProgress); }
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
        this.kinematics.readData(view);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        this.kinematics.writeData(view);
    }
    //endregion
}
