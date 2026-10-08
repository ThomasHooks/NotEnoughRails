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
import com.github.thomashooks.notenoughrails.energy.KinematicBlockEntity;
import com.github.thomashooks.notenoughrails.block.entity.behaviors.RotatingShaftAnimator;
import com.github.thomashooks.notenoughrails.block.entity.behaviors.RotatingShaftProvider;
import com.github.thomashooks.notenoughrails.energy.KinematicBlockProvider;
import com.github.thomashooks.notenoughrails.energy.MechanicalConnection;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

public class LanternPinionBlockEntity extends LazyTickingBlockEntity implements KinematicBlockEntity, RotatingShaftProvider {
    //region Kinematics Anonymous Class
    private final Kinematics kinematics = new Kinematics() {
        @Override
        public void markDirty() { updateAndNotifyAll(); }
    };
    private final ArrayList<MechanicalConnection> connections = MechanicalConnection.makeSmallCogwheel(getPos(), Direction.Axis.Y); // This is for testing
    //endregion

    private final RotatingShaftAnimator shaftAnimator = new RotatingShaftAnimator();

    protected LanternPinionBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.LANTERN_PINION, pos, state);
        setLazyTickRate(30);
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

        shaftAnimator.step(getSpeed());
    }
    //endregion

    //region Kinematics Block Entity Methods
    @Override
    public @NotNull ArrayList<MechanicalConnection> getMechanicalConnections() { return connections; }

    @Override
    public float getSpeed() { return this.kinematics.getSpeed(); }

    @Override
    public void setSpeed(BlockPos driverPosIn, float speedIn) { this.kinematics.setSpeed(driverPosIn, speedIn); }

    protected void propagateSpeed() {
        this.kinematics.propagateSpeed((KinematicBlockProvider) getCachedState().getBlock(), getWorld(), getPos());
    }
    //endregion

    //region Block Entity Renderer Methods
    @Override
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
