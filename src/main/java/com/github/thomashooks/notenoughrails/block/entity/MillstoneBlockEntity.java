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
import com.github.thomashooks.notenoughrails.block.MillstoneBlock;
import com.github.thomashooks.notenoughrails.inventory.SidedSimpleInventory;
import com.github.thomashooks.notenoughrails.network.BlockPosPayload;
import com.github.thomashooks.notenoughrails.recipe.AllRecipes;
import com.github.thomashooks.notenoughrails.recipe.MillingRecipe;
import com.github.thomashooks.notenoughrails.recipe.input.SimpleRecipeInput;
import com.github.thomashooks.notenoughrails.screen.QuernScreenHandler;
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
import net.minecraft.util.math.MathHelper;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

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
                case PROPERTY_DELEGATE_SPEED_INDEX -> Math.round(MillstoneBlockEntity.this.speed); // TODO: change this to a % int ie. 0 to 100
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

    private final ServerRecipeManager.MatchGetter<SimpleRecipeInput, MillingRecipe>  matchGetter;
    private int progress;
    public static final int DEFAULT_MILLING_TIME = 200;
    private int maxProgress;
    private static final String PROGRESS_TAG = NotEnoughRails.MOD_ID + ":progress";
    private static final String MAX_PROGRESS_TAG = NotEnoughRails.MOD_ID + ":max_progress";
    public static final Text SCREEN_TITLE = Text.translatable("container." + NotEnoughRails.MOD_ID + ".quern");

    private final RotatingShaftAnimator shaftAnimator = new RotatingShaftAnimator();

    // These data fields are temporary
    private float speed;
    public static final int MIN_SPEED = 16;
    public static final int MAX_SPEED = 32;
    private static final String SPEED_TAG = NotEnoughRails.MOD_ID + ":speed";

    protected MillstoneBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.MILLSTONE, pos, state);
        this.matchGetter = ServerRecipeManager.createCachedMatchGetter(AllRecipes.Types.MILLING);
        this.progress = 0;
        this.maxProgress = DEFAULT_MILLING_TIME;
        setLazyTickRate(20);
    }

    public SimpleInventory getInventory() { return inventory; }

    public float getSpeed() { return speed; }

    protected void setSpeed(float speed) {
        if (!MathHelper.approximatelyEquals(speed, this.speed)) {
            this.speed = speed;
            updateAndNotifyAll();
        }
    }

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

        boolean wasMilling = isMilling();

        ItemStack inputItemStack = this.inventory.getStack(INPUT_SLOT_INDEX).copy();
        MillingRecipe recipe = getRecipe(inputItemStack).map(RecipeEntry::value).orElse(null);
        ItemStack outputItemStack = this.inventory.getStack(OUTPUT_SLOT_INDEX).copy();
        boolean canCraft = canCraftRecipe(inputItemStack, outputItemStack, recipe);
        // TODO: Add a requirement to be powered
        if (canCraft) {
            this.progress++;
            this.maxProgress = recipe.millingTime();
            setSpeed(32.0F); // REMOVE - this is for testing only
        } else if (isMilling()) {
            this.progress = 0;
            this.maxProgress = DEFAULT_MILLING_TIME;
            setSpeed(0.0F); // REMOVE - this is for testing only
        }

        boolean hasChanged = false;
        if (canCraft && this.progress >= this.maxProgress) {
            if (craftRecipe(recipe)) {
                this.progress = 0;
                this.maxProgress = DEFAULT_MILLING_TIME;
                hasChanged = true;
                setSpeed(0.0F); // REMOVE - this is for testing only
            }
        }

        if (wasMilling != isMilling()) {
            if (getWorld() != null) {
                getWorld().setBlockState(getPos(), getCachedState().with(MillstoneBlock.IS_MILLING, isMilling()), Block.NOTIFY_ALL);
                hasChanged = true;
            }
        }

        if (hasChanged) {
            updateAndNotifyAll();
        }
    }

    @Override
    protected void clientTick() {
        super.clientTick();

        shaftAnimator.step(getSpeed());
    }
    //endregion

    //region Crafting Methods
    private boolean isMilling() { return this.progress > 0; }

    public ItemStack getMillingItemStack() { return this.inventory.getStack(INPUT_SLOT_INDEX).copy(); }

    private Optional<RecipeEntry<MillingRecipe>> getRecipe(ItemStack input) {
        if (input.isEmpty()) {
            return Optional.empty();
        }

        SimpleRecipeInput recipeInput = new SimpleRecipeInput(input);
        if (getWorld() instanceof ServerWorld serverWorld) {
            return this.matchGetter.getFirstMatch(recipeInput, serverWorld);
        }
        return Optional.empty();
    }

    private boolean craftRecipe(MillingRecipe recipe) {
        // Handle bonus items
        ItemStack result = recipe.result().copy();
        float bonusChance = recipe.bonus();
        int bonusAmount = 0;
        if  (bonusChance > 0.001F) {
            float roll = MathHelper.nextBetween(Objects.requireNonNull(getWorld()).getRandom(), 0.001F, 1.0F);
            if (roll < bonusChance) {
                bonusAmount = MathHelper.ceil(result.getCount() / 2.0);
            }
        }

        ItemStack input = this.inventory.getStack(INPUT_SLOT_INDEX);
        ItemStack output = this.inventory.getStack(OUTPUT_SLOT_INDEX);
        if (canCraftRecipe(input, output, recipe)) {
            if (output.isEmpty()) {
                if (bonusAmount > 0) {
                    // Since the output slot is empty we can just add the bonus items
                    result.increment(bonusAmount);
                }
                this.inventory.setStack(OUTPUT_SLOT_INDEX, result.copy());
            } else if (ItemStack.areItemsEqual(output, result)) {
                if (bonusAmount > 0) {
                    // Ignore the bonus items if they do not fit
                    if (output.getCount() + result.getCount() + bonusAmount <= output.getMaxCount()) {
                        result.increment(bonusAmount);
                    }
                }
                output.increment(result.getCount());
            } else {
                throw new IllegalStateException("Fix the Quern crafting!");
            }
            input.decrement(1); // All recipes only use one input
            return true;
        }
        return false;
    }

    private boolean canCraftRecipe(ItemStack input, ItemStack output, MillingRecipe recipe) {
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
        return new QuernScreenHandler(syncId, playerInventory, this, this.delegate);
    }

    public int calculateComparatorOutput() {
        if (inventory == null) {
            return 0;
        }

        float signalStrength = 0.0F;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack itemStack = inventory.getStack(i).copy();
            if (!itemStack.isEmpty()) {
                signalStrength += (float) itemStack.getCount() / (float) inventory.getMaxCount(itemStack);
            }
        }

        signalStrength /= (float) inventory.size();
        return MathHelper.lerpPositive(signalStrength, 0, 15);
    }
    //endregion

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
        if (view.contains(PROGRESS_TAG)) {
            this.progress = view.getInt(PROGRESS_TAG, 0);
        }
        if (view.contains(MAX_PROGRESS_TAG)) {
            this.maxProgress = view.getInt(MAX_PROGRESS_TAG, DEFAULT_MILLING_TIME);
        }
        if (view.contains(SPEED_TAG)) {
            this.speed = view.getFloat(SPEED_TAG, 0.0F);
        }
        Inventories.readData(view, this.inventory.getHeldStacks());
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt(PROGRESS_TAG, this.progress);
        view.putInt(MAX_PROGRESS_TAG, this.maxProgress);
        view.putFloat(SPEED_TAG, this.speed);
        Inventories.writeData(view, this.inventory.getHeldStacks());
    }
    //endregion
}
