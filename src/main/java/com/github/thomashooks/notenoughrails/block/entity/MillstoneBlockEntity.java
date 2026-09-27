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
import com.github.thomashooks.notenoughrails.inventory.SidedSimpleInventory;
import com.github.thomashooks.notenoughrails.network.BlockPosPayload;
import com.github.thomashooks.notenoughrails.screen.QuernScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class MillstoneBlockEntity extends LazyTickingBlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload> {
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

    //region Property Delegate Anonymous Class
    public static final int PROPERTY_DELEGATE_SIZE = 3;
    public static final int PROPERTY_DELEGATE_PROGRESS_INDEX = 0;
    public static final int PROPERTY_DELEGATE_MAX_PROGRESS_INDEX = 1;
    public static final int PROPERTY_DELEGATE_SPEED_INDEX = 2;
    protected final PropertyDelegate delegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case PROPERTY_DELEGATE_PROGRESS_INDEX -> MillstoneBlockEntity.this.progress;
                case PROPERTY_DELEGATE_MAX_PROGRESS_INDEX -> MillstoneBlockEntity.this.maxProgress;
                case PROPERTY_DELEGATE_SPEED_INDEX -> MillstoneBlockEntity.this.speed;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case PROPERTY_DELEGATE_PROGRESS_INDEX -> MillstoneBlockEntity.this.progress = value;
                case PROPERTY_DELEGATE_MAX_PROGRESS_INDEX -> MillstoneBlockEntity.this.maxProgress = value;
                default -> {}
            }
        }

        @Override
        public int size() { return PROPERTY_DELEGATE_SIZE; }
    };
    //endregion

    private int progress;
    public static final int MAX_PROGRESS_TIME = 200;
    private int maxProgress = MAX_PROGRESS_TIME;
    private static final String PROGRESS_TAG = NotEnoughRails.MOD_ID + ":progress";
    private static final String MAX_PROGRESS_TAG = NotEnoughRails.MOD_ID + ":max_progress";
    private int speed;
    public static final int MIN_SPEED = 8;
    public static final int MAX_SPEED = 16; // Max speed possible is 256
    private static final String SPEED_TAG = NotEnoughRails.MOD_ID + ":speed";
    public static final Text SCREEN_TITLE = Text.translatable("container." + NotEnoughRails.MOD_ID + ".quern");

    protected MillstoneBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.MILLSTONE, pos, state);
        this.progress = 0;
        setLazyTickRate(20);
    }

    public SimpleInventory getInventory() { return inventory; }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState) {
        super.onBlockReplaced(pos, oldState);
        if (getWorld() instanceof ServerWorld serverWorld) {
            ItemScatterer.spawn(serverWorld, pos, inventory);
        }
    }

    @Override
    protected void tick() {
        super.tick();

        // REMOVE - this is for testing only
        this.speed = 8;
        this.progress = 100;
    }

    //region Crafting Methods
    // TODO: Add crafting and recipe
    //endregion

    //region Screen Methods
    @Override
    public @NonNull BlockPosPayload getScreenOpeningData(@NonNull ServerPlayerEntity player) {
        return new BlockPosPayload(getPos());
    }

    @Override
    public Text getDisplayName() { return SCREEN_TITLE; }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new QuernScreenHandler(syncId, playerInventory, this, this.delegate);
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
            this.maxProgress = view.getInt(MAX_PROGRESS_TAG, MAX_PROGRESS_TIME);
        }
        if (view.contains(SPEED_TAG)) {
            this.speed = view.getInt(SPEED_TAG, 0);
        }
        Inventories.readData(view, this.inventory.getHeldStacks());
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt(PROGRESS_TAG, this.progress);
        view.putInt(MAX_PROGRESS_TAG, this.maxProgress);
        view.putInt(SPEED_TAG, this.speed);
        Inventories.writeData(view, this.inventory.getHeldStacks());
    }
    //endregion
}
