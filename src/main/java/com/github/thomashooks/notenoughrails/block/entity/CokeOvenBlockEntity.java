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
import com.github.thomashooks.notenoughrails.block.CokeOvenBlock;
import com.github.thomashooks.notenoughrails.inventory.SidedSimpleInventory;
import com.github.thomashooks.notenoughrails.network.BlockPosPayload;
import com.github.thomashooks.notenoughrails.recipe.AllRecipes;
import com.github.thomashooks.notenoughrails.recipe.CokingRecipe;
import com.github.thomashooks.notenoughrails.recipe.input.SimpleRecipeInput;
import com.github.thomashooks.notenoughrails.screen.CokeOvenScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
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
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
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

import java.util.Optional;

public class CokeOvenBlockEntity extends LazyTickingBlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload> {
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
        public boolean canPlayerUse(PlayerEntity player) {
            return super.canPlayerUse(player);
        }

        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return slot != OUTPUT_SLOT_INDEX;
        }

        @Override
        public int[] getAvailableSlots(Direction side) {
            if (side == Direction.DOWN) {
                return new int[]{ OUTPUT_SLOT_INDEX };
            } else {
                return new int[]{ INPUT_SLOT_INDEX };
            }
        }

        @Override
        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            return isValid(slot, stack);
        }

        @Override
        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return dir == Direction.DOWN && slot == OUTPUT_SLOT_INDEX;
        }
    };
    //endregion
    //region Property Delegate Anonymous Class
    public static final int PROPERTY_DELEGATE_SIZE = 2;
    public static final int PROPERTY_DELEGATE_PROGRESS_INDEX = 0;
    public static final int PROPERTY_DELEGATE_MAX_PROGRESS_INDEX = 1;
    protected final PropertyDelegate delegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case PROPERTY_DELEGATE_PROGRESS_INDEX -> CokeOvenBlockEntity.this.progress;
                case PROPERTY_DELEGATE_MAX_PROGRESS_INDEX -> CokeOvenBlockEntity.this.maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case PROPERTY_DELEGATE_PROGRESS_INDEX -> CokeOvenBlockEntity.this.progress = value;
                case PROPERTY_DELEGATE_MAX_PROGRESS_INDEX -> CokeOvenBlockEntity.this.maxProgress = value;
                default -> {}
            }
        }

        @Override
        public int size() { return 2; }
    };
    //endregion

    private final ServerRecipeManager.MatchGetter<SimpleRecipeInput, CokingRecipe>  matchGetter;
    private int progress;
    public static final int MAX_PROGRESS_TIME = 2400;
    private int maxProgress = MAX_PROGRESS_TIME;
    private static final String PROGRESS_TAG = NotEnoughRails.MOD_ID + ":progress";
    private static final String MAX_PROGRESS_TAG = NotEnoughRails.MOD_ID + ":max_progress";
    public static final Text SCREEN_TITLE = Text.translatable("container." + NotEnoughRails.MOD_ID + ".coke_oven");

    public CokeOvenBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.COKE_OVEN, pos, state);
        this.matchGetter = ServerRecipeManager.createCachedMatchGetter(AllRecipes.Types.COKING);
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

        boolean wasCrafting = isCrafting();

        ItemStack input = this.inventory.getStack(INPUT_SLOT_INDEX).copy();
        Optional<RecipeEntry<CokingRecipe>> recipeEntry = getRecipe(input);
        CokingRecipe recipe = recipeEntry.map(RecipeEntry::value).orElse(null);
        ItemStack output = this.inventory.getStack(OUTPUT_SLOT_INDEX).copy();
        boolean canCraft = canCraftRecipe(input, output, recipe);
        if (canCraft) {
            this.progress++;
            this.maxProgress = recipe.cookingTime();
        } else if (this.progress > 0) {
            this.progress = 0;
            this.maxProgress = 0;
        }

        boolean hasChanged = false;
        if (canCraft && this.progress > this.maxProgress) {
            hasChanged = craftRecipe(recipe);
        }

        if (wasCrafting != isCrafting()) {
            if (getWorld() != null) {
                getWorld().setBlockState(getPos(), getCachedState().with(CokeOvenBlock.LIT, isCrafting()), Block.NOTIFY_ALL);
                hasChanged = true;
            }
        }

        if (hasChanged) {
            updateAndNotifyAll();
        }
    }

    //region Crafting Methods
    private boolean isCrafting() { return this.progress > 0; }

    private Optional<RecipeEntry<CokingRecipe>> getRecipe(ItemStack input) {
        if (input.isEmpty()) {
            return Optional.empty();
        }

        SimpleRecipeInput recipeInput = new SimpleRecipeInput(input);
        if (getWorld() instanceof ServerWorld serverWorld) {
            return this.matchGetter.getFirstMatch(recipeInput, serverWorld);
        }
        return Optional.empty();
    }

    private boolean craftRecipe(CokingRecipe recipe) {
        ItemStack input = this.inventory.getStack(INPUT_SLOT_INDEX);
        ItemStack output = this.inventory.getStack(OUTPUT_SLOT_INDEX);
        ItemStack result = recipe.result().copy();
        if (canCraftRecipe(input, output, recipe)) {
            if (output.isEmpty()) {
                this.inventory.setStack(OUTPUT_SLOT_INDEX, result.copy());
            } else if (ItemStack.areItemsEqual(output, result)) {
                output.increment(result.getCount());
            } else {
                throw new IllegalStateException("Fix the Coke Oven crafting!");
            }
            input.decrement(1); // All recipes only use one input
            this.progress = 0;
            this.maxProgress = 0;
            return true;
        }
        return false;
    }

    private boolean canCraftRecipe(ItemStack input, ItemStack output, CokingRecipe recipe) {
        if (recipe == null || input.isEmpty()) {
            return false;
        }

        ItemStack result = recipe.result().copy();
        if (output.isEmpty()) {
            return true;
        } else if (!ItemStack.areItemsEqual(output, result)) {
            return false;
        } else {
            return output.getCount() + result.getCount() <= output.getMaxCount();
        }
    }
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
        return new CokeOvenScreenHandler(syncId, playerInventory, this, this.delegate);
    }
    //endregion

    //region Serialize and Deserialize Methods
    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        if (view.contains(PROGRESS_TAG)) {
            this.progress = view.getInt(PROGRESS_TAG, 0);
        }
        if (view.contains(MAX_PROGRESS_TAG)) {
            this.maxProgress = view.getInt(MAX_PROGRESS_TAG, MAX_PROGRESS_TIME);
        }
        Inventories.readData(view, this.inventory.getHeldStacks());
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt(PROGRESS_TAG, this.progress);
        view.putInt(MAX_PROGRESS_TAG, this.maxProgress);
        Inventories.writeData(view, this.inventory.getHeldStacks());
    }
    //endregion
}
