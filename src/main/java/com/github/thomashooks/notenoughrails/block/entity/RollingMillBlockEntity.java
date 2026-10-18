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
import com.github.thomashooks.notenoughrails.energy.KinematicBlockEntity;
import com.github.thomashooks.notenoughrails.energy.KinematicBlockProvider;
import com.github.thomashooks.notenoughrails.energy.KinematicsController;
import com.github.thomashooks.notenoughrails.energy.MechanicalConnection;
import com.github.thomashooks.notenoughrails.inventory.SidedSimpleInventory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class RollingMillBlockEntity extends LazyTickingBlockEntity implements KinematicBlockEntity {
    //region Sided Inventory Anonymous Class
    public static final int INPUT_SLOTS = 1;
    public static final int OUTPUT_SLOTS = 1;
    public static final int NUMBER_OF_SLOTS = INPUT_SLOTS + OUTPUT_SLOTS;
    public static final int INPUT_SLOT_INDEX = 0;
    public static final int OUTPUT_SLOT_INDEX = 1;
    private final SidedSimpleInventory inventory = new SidedSimpleInventory(NUMBER_OF_SLOTS) {
        @Override
        public void markDirty() {
            super.markDirty();
            updateAndNotifyAll();
        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) { return super.canPlayerUse(player); }

        @Override
        public boolean isValid(int slot, ItemStack stack) { return slot != OUTPUT_SLOT_INDEX; }

        @Override
        public int[] getAvailableSlots(Direction side) {
            if (side == Direction.DOWN) {
                return new int[]{ OUTPUT_SLOT_INDEX };
            } else {
                return new int[]{ INPUT_SLOT_INDEX };
            }
        }

        @Override
        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) { return isValid(slot, stack); }

        @Override
        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return dir == Direction.DOWN && slot == OUTPUT_SLOT_INDEX;
        }
    };
    //endregion

    //region Kinematics Anonymous Class
    public static final float MIN_SPEED = 40.0F;
    public static final float MAX_SPEED = 80.0F;
    private ArrayList<MechanicalConnection> connections;
    private final KinematicsController kinematicsController = new KinematicsController() {
        @Override
        public void markDirty() { updateAndNotifyAll(); }
    };
    //endregion

    private int progress;
    public static final int DEFAULT_MILLING_TIME = 200;
    private int maxProgress;
    private static final String PROGRESS_TAG = NotEnoughRails.MOD_ID + ":progress";
    private static final String MAX_PROGRESS_TAG = NotEnoughRails.MOD_ID + ":max_progress";

    protected RollingMillBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.ROLLING_MILL, pos, state);
    }

    public SimpleInventory getInventory() { return inventory; }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState) {
        super.onBlockReplaced(pos, oldState);
        if (getWorld() instanceof ServerWorld serverWorld) {
            ItemScatterer.spawn(serverWorld, pos, inventory);
        }
    }

    //region Lazy Ticking Methods
    @Override
    protected void tick() {
        super.tick();

        propagateSpeed();
    }

    @Override
    protected void clientTick() {
        super.clientTick();

        this.kinematicsController.stepRotatingShaft(Objects.requireNonNull(getWorld()), getPos());
    }
    //endregion

    //region Kinematics Methods
    @Override
    public @NotNull ArrayList<MechanicalConnection> getMechanicalConnections() {
        if (this.connections == null) {
            this.connections = MechanicalConnection.makeMonoAxle(getPos(), getAxis());
        }
        return this.connections;
    }

    @Override
    public float getSpeed() { return this.kinematicsController.getSpeed(); }

    @Override
    public void setSpeed(BlockPos driverPosIn, float speedIn) { this.kinematicsController.setSpeed(driverPosIn, speedIn); }

    protected boolean hasEnoughPower() {
        int amount = getPowerGauge();
        return amount > 0 && amount < 101;
    }

    /**
     * @return Gets the power gauge as a whole number percentage
     */
    protected int getPowerGauge() {
        float speed = Math.abs(getSpeed());
        if (speed < MIN_SPEED) {
            return 0;
        } else {
            return Math.round(((speed - MIN_SPEED) / (MAX_SPEED - MIN_SPEED)) * 100.0F);
        }
    }

    protected void propagateSpeed() {
        this.kinematicsController.propagateSpeed((KinematicBlockProvider) getCachedState().getBlock(), Objects.requireNonNull(getWorld()), getPos());
    }

    public float getRotatingShaftProgress(float tickProgress) {
        return this.kinematicsController.getRotatingShaftProgress(tickProgress);
    }

    public Direction.Axis getAxis() {
        Direction facing = getCachedState().get(Properties.HORIZONTAL_FACING);
        return facing.rotateYClockwise().getAxis();
    }
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
        if (view.contains(PROGRESS_TAG)) {
            this.progress = view.getInt(PROGRESS_TAG, 0);
        }
        if (view.contains(MAX_PROGRESS_TAG)) {
            this.maxProgress = view.getInt(MAX_PROGRESS_TAG, DEFAULT_MILLING_TIME);
        }
        this.kinematicsController.readData(view);
        Inventories.readData(view, this.inventory.getHeldStacks());
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt(PROGRESS_TAG, this.progress);
        view.putInt(MAX_PROGRESS_TAG, this.maxProgress);
        this.kinematicsController.writeData(view);
        Inventories.writeData(view, this.inventory.getHeldStacks());
    }
    //endregion
}
