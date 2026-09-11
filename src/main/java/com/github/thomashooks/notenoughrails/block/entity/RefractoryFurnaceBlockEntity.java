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
import com.github.thomashooks.notenoughrails.block.RefractoryFurnaceBlock;
import com.github.thomashooks.notenoughrails.inventory.SidedSimpleInventory;
import com.github.thomashooks.notenoughrails.item.AllItems;
import com.github.thomashooks.notenoughrails.network.BlockPosPayload;
import com.github.thomashooks.notenoughrails.recipe.AllRecipes;
import com.github.thomashooks.notenoughrails.recipe.BlastingRecipe;
import com.github.thomashooks.notenoughrails.recipe.input.SimpleRecipeInput;
import com.github.thomashooks.notenoughrails.screen.RefractoryFurnaceScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

import java.util.Objects;
import java.util.Optional;

public class RefractoryFurnaceBlockEntity extends LazyTickingBlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload> {
    //region Sided Inventory Anonymous Class
    public static final int INPUT_SLOTS = 3;
    public static final int OUTPUT_SLOTS = 1;
    public static final int NUMBER_OF_SLOTS = INPUT_SLOTS + OUTPUT_SLOTS;
    public static final int INPUT_SLOT_INDEX = 0;
    public static final int FLUX_SLOT_INDEX = 1;
    public static final int FUEL_SLOT_INDEX = 2;
    public static final int OUTPUT_SLOT_INDEX = 3;
    private final SidedSimpleInventory inventory = new SidedSimpleInventory(NUMBER_OF_SLOTS) {
        @Override
        public void markDirty() {
            super.markDirty();
            updateAndNotifyAll();
        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) { return super.canPlayerUse(player); }

        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return switch (slot) {
                case INPUT_SLOT_INDEX -> !stack.isOf(AllItems.FLUX);
                case FLUX_SLOT_INDEX -> stack.isOf(AllItems.FLUX);
                case FUEL_SLOT_INDEX -> {
                    ItemStack fuelItem = inventory.getStack(FUEL_SLOT_INDEX);
                    yield Objects.requireNonNull(world).getFuelRegistry().isFuel(stack) || stack.isOf(Items.BUCKET) && !fuelItem.isOf(Items.BUCKET);
                }
                case OUTPUT_SLOT_INDEX -> false;
                default -> true;
            };
        }

        @Override
        public int[] getAvailableSlots(Direction side) {
            return switch (side) {
                case DOWN -> new int[]{ OUTPUT_SLOT_INDEX, FUEL_SLOT_INDEX };
                case UP -> new int[]{ INPUT_SLOT_INDEX, FLUX_SLOT_INDEX };
                case NORTH, SOUTH, WEST, EAST -> new int[]{ FUEL_SLOT_INDEX };
            };
        }

        @Override
        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) { return isValid(slot, stack); }

        @Override
        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            if (dir == Direction.DOWN && slot == FUEL_SLOT_INDEX) {
                return stack.isOf(Items.BUCKET) || stack.isOf(Items.WATER_BUCKET);
            }
            return true;
        }
    };
    //endregion

    //region Property Delegate Anonymous Class
    public static final int PROPERTY_DELEGATE_SIZE = 4;
    public static final int PROPERTY_DELEGATE_LIT_TIME_INDEX = 0;
    public static final int PROPERTY_DELEGATE_MAX_LIT_TIME_INDEX = 1;
    public static final int PROPERTY_DELEGATE_PROGRESS_INDEX = 2;
    public static final int PROPERTY_DELEGATE_MAX_PROGRESS_INDEX = 3;
    protected final PropertyDelegate delegate = new PropertyDelegate() {

        @Override
        public int get(int index) {
            return switch (index) {
                case PROPERTY_DELEGATE_LIT_TIME_INDEX -> RefractoryFurnaceBlockEntity.this.litTime;
                case PROPERTY_DELEGATE_MAX_LIT_TIME_INDEX -> RefractoryFurnaceBlockEntity.this.maxLitTime;
                case PROPERTY_DELEGATE_PROGRESS_INDEX -> RefractoryFurnaceBlockEntity.this.progress;
                case PROPERTY_DELEGATE_MAX_PROGRESS_INDEX -> RefractoryFurnaceBlockEntity.this.maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case PROPERTY_DELEGATE_LIT_TIME_INDEX -> RefractoryFurnaceBlockEntity.this.litTime = value;
                case PROPERTY_DELEGATE_MAX_LIT_TIME_INDEX -> RefractoryFurnaceBlockEntity.this.maxLitTime = value;
                case PROPERTY_DELEGATE_PROGRESS_INDEX -> RefractoryFurnaceBlockEntity.this.progress = value;
                case PROPERTY_DELEGATE_MAX_PROGRESS_INDEX -> RefractoryFurnaceBlockEntity.this.maxProgress = value;
                default -> {}
            }
        }

        @Override
        public int size() { return PROPERTY_DELEGATE_SIZE; }
    };
    //endregion

    private final ServerRecipeManager.MatchGetter<SimpleRecipeInput, BlastingRecipe>  matchGetter;
    private int progress;
    public static final int DEFAULT_COOK_TIME = 400;
    private int maxProgress;
    private int litTime;
    private int maxLitTime;
    private static final String PROGRESS_TAG = NotEnoughRails.MOD_ID + ":progress";
    private static final String MAX_PROGRESS_TAG = NotEnoughRails.MOD_ID + ":max_progress";
    private static final String LIT_TIME_TAG = NotEnoughRails.MOD_ID + ":lit_time";
    private static final String MAX_LIT_TIME_TAG = NotEnoughRails.MOD_ID + ":max_lit_time";
    public static final Text SCREEN_TITLE = Text.translatable("container." + NotEnoughRails.MOD_ID + ".refractory_furnace");

    protected RefractoryFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.REFRACTORY_FURNACE, pos, state);
        this.matchGetter = ServerRecipeManager.createCachedMatchGetter(AllRecipes.Types.BLASTING);
        this.progress = 0;
        this.maxProgress = DEFAULT_COOK_TIME;
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

        boolean wasBurning = isBurning();
        if (wasBurning) {
            this.litTime--;
        }

        ItemStack fuelItemStack = this.inventory.getStack(FUEL_SLOT_INDEX).copy();
        boolean hasFuel = !fuelItemStack.isEmpty();

        ItemStack inputItemStack = this.inventory.getStack(INPUT_SLOT_INDEX).copy();
        BlastingRecipe recipe = getRecipe(inputItemStack).map(RecipeEntry::value).orElse(null);
        ItemStack outputItemStack = this.inventory.getStack(OUTPUT_SLOT_INDEX).copy();
        boolean canCraft = canCraftRecipe(inputItemStack, outputItemStack, recipe);

        boolean hasChanged = false;
        if (!wasBurning && hasFuel && canCraft) {
            this.maxLitTime = getFuelTime(fuelItemStack);
            this.litTime = this.maxLitTime;
            Item fuelItem = fuelItemStack.getItem();
            fuelItemStack.decrement(1);
            if (fuelItemStack.isEmpty()) {
                inventory.setStack(FUEL_SLOT_INDEX, fuelItem.getRecipeRemainder());
            } else {
                inventory.setStack(FUEL_SLOT_INDEX, fuelItemStack);
            }
            hasChanged = true;
        }

        if (canCraft && isBurning()) {
            this.progress++;
            this.maxProgress = recipe.cookingTime();
        } else if (isCrafting()) {
            this.progress = 0;
            this.maxProgress = DEFAULT_COOK_TIME;
        }

        if (canCraft && this.progress >= this.maxProgress) {
            if (craftRecipe(recipe)) {
                this.progress = 0;
                this.maxProgress = DEFAULT_COOK_TIME;
                hasChanged = true;
            }
        }

        if (wasBurning != isBurning()) {
            if (getWorld() != null) {
                getWorld().setBlockState(getPos(), getCachedState().with(RefractoryFurnaceBlock.LIT, isBurning()), Block.NOTIFY_ALL);
                hasChanged = true;
            }
        }

        if (hasChanged) {
            updateAndNotifyAll();
        }
    }

    //region Crafting Methods
    private boolean isBurning() { return this.litTime > 0; }

    private int getFuelTime(ItemStack stack) {
        return Objects.requireNonNull(getWorld()).getFuelRegistry().getFuelTicks(stack);
    }

    private boolean hasFlux() {
        ItemStack fluxItemStack = this.inventory.getStack(FLUX_SLOT_INDEX).copy();
        return !fluxItemStack.isEmpty() && fluxItemStack.isOf(AllItems.FLUX);
    }

    private boolean isCrafting() { return this.progress > 0; }

    private Optional<RecipeEntry<BlastingRecipe>> getRecipe(ItemStack input) {
        if (input.isEmpty()) {
            return Optional.empty();
        }

        SimpleRecipeInput recipeInput = new SimpleRecipeInput(input);
        if (getWorld() instanceof ServerWorld serverWorld) {
            return this.matchGetter.getFirstMatch(recipeInput, serverWorld);
        }
        return Optional.empty();
    }

    private boolean craftRecipe(BlastingRecipe recipe) {
        ItemStack input = this.inventory.getStack(INPUT_SLOT_INDEX);
        ItemStack output = this.inventory.getStack(OUTPUT_SLOT_INDEX);
        if (canCraftRecipe(input, output, recipe)) {
            ItemStack result = recipe.result().copy();
            if (output.isEmpty()) {
                this.inventory.setStack(OUTPUT_SLOT_INDEX, result.copy());
            } else if (ItemStack.areItemsEqual(output, result)) {
                output.increment(result.getCount());
            } else {
                throw new IllegalStateException("Fix the Refractory Furnace crafting!");
            }
            input.decrement(1); // All recipes only use one input
            this.inventory.getStack(FLUX_SLOT_INDEX).decrement(1);
            return true;
        }
        return false;
    }

    private boolean canCraftRecipe(ItemStack input, ItemStack output, BlastingRecipe recipe) {
        if (recipe == null || input.isEmpty() || !hasFlux()) {
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
        return new RefractoryFurnaceScreenHandler(syncId, playerInventory, this, this.delegate);
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
            this.maxProgress = view.getInt(MAX_PROGRESS_TAG, DEFAULT_COOK_TIME);
        }
        if (view.contains(LIT_TIME_TAG)) {
            this.litTime = view.getInt(LIT_TIME_TAG, 0);
        }
        if (view.contains(MAX_LIT_TIME_TAG)) {
            this.maxLitTime = view.getInt(MAX_LIT_TIME_TAG, 0);
        }
        Inventories.readData(view, this.inventory.getHeldStacks());
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt(PROGRESS_TAG, this.progress);
        view.putInt(MAX_PROGRESS_TAG, this.maxProgress);
        view.putInt(LIT_TIME_TAG, this.litTime);
        view.putInt(MAX_LIT_TIME_TAG, this.maxLitTime);
        Inventories.writeData(view, this.inventory.getHeldStacks());
    }
    //endregion
}
