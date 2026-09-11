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
package com.github.thomashooks.notenoughrails.screen;

import com.github.thomashooks.notenoughrails.block.AllBlocks;
import com.github.thomashooks.notenoughrails.block.entity.RefractoryFurnaceBlockEntity;
import com.github.thomashooks.notenoughrails.item.AllItems;
import com.github.thomashooks.notenoughrails.network.BlockPosPayload;
import com.github.thomashooks.notenoughrails.screen.slot.CraftingOutputSlot;
import com.github.thomashooks.notenoughrails.screen.slot.FluxSlot;
import com.github.thomashooks.notenoughrails.screen.slot.FuelSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class RefractoryFurnaceScreenHandler extends ScreenHandler {
    private static final int NUMBER_OF_PLAYERS_INVENTORY_SLOTS = 36;
    private static final int INPUT_SLOT_INDEX = RefractoryFurnaceBlockEntity.INPUT_SLOT_INDEX;
    private static final int FLUX_SLOT_INDEX = RefractoryFurnaceBlockEntity.FLUX_SLOT_INDEX;
    private static final int FUEL_SLOT_INDEX = RefractoryFurnaceBlockEntity.FUEL_SLOT_INDEX;
    private static final int OUTPUT_SLOT_INDEX = RefractoryFurnaceBlockEntity.OUTPUT_SLOT_INDEX;
    private static final int NUMBER_OF_SLOTS = RefractoryFurnaceBlockEntity.NUMBER_OF_SLOTS;
    private static final int PLAYER_INVENTORY_START_INDEX = NUMBER_OF_SLOTS;
    private static final int PLAYER_INVENTORY_END_INDEX = PLAYER_INVENTORY_START_INDEX + NUMBER_OF_PLAYERS_INVENTORY_SLOTS;
    private static final int PLAYER_HOTBAR_START_INDEX = PLAYER_INVENTORY_END_INDEX - 9;
    private final RefractoryFurnaceBlockEntity blockEntity;
    private final World world;
    private final ScreenHandlerContext context;
    private final PropertyDelegate delegate;
    private static final int PROPERTY_DELEGATE_SIZE = RefractoryFurnaceBlockEntity.PROPERTY_DELEGATE_SIZE;

    //Server Side
    public RefractoryFurnaceScreenHandler(
            int syncId,
            PlayerInventory playerInventory,
            RefractoryFurnaceBlockEntity blockEntity,
            PropertyDelegate delegate
    ) {
        super(AllScreenHandlers.REFRACTORY_FURNACE, syncId);
        this.blockEntity = blockEntity;
        this.world = blockEntity.getWorld();
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(), this.blockEntity.getPos());

        SimpleInventory inventory = blockEntity.getInventory();
        checkSize(inventory, NUMBER_OF_SLOTS);
        inventory.onOpen(playerInventory.player);
        checkDataCount(delegate, PROPERTY_DELEGATE_SIZE);
        this.delegate = delegate;

        addSlot(new Slot(inventory, INPUT_SLOT_INDEX, 66, 17));
        addSlot(new FluxSlot(inventory, FLUX_SLOT_INDEX, 45, 17));
        addSlot(new FuelSlot(Objects.requireNonNull(blockEntity.getWorld()).getFuelRegistry(), inventory, FUEL_SLOT_INDEX, 56, 53));
        addSlot(new CraftingOutputSlot(inventory, OUTPUT_SLOT_INDEX, 116, 35));
        addPlayerSlots(playerInventory, 8, 84);
        addProperties(delegate);
    }

    //Client Side
    public RefractoryFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(
                syncId,
                playerInventory,
                (RefractoryFurnaceBlockEntity) playerInventory.player.getEntityWorld().getBlockEntity(payload.pos()),
                new ArrayPropertyDelegate(PROPERTY_DELEGATE_SIZE)
        );
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (slot == null || !slot.hasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack stackInSlot = slot.getStack();
        ItemStack stackCopy = stackInSlot.copy();
        if (slotIndex == OUTPUT_SLOT_INDEX) {
            if (!insertItem(stackInSlot, PLAYER_INVENTORY_START_INDEX, PLAYER_INVENTORY_END_INDEX, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickTransfer(stackInSlot, stackCopy);
        } else if (slotIndex != INPUT_SLOT_INDEX && slotIndex != FLUX_SLOT_INDEX && slotIndex != FUEL_SLOT_INDEX) {
            if (isFlux(stackInSlot)) {
                if (!insertItem(stackInSlot, FLUX_SLOT_INDEX, FUEL_SLOT_INDEX, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (isFuel(stackInSlot)) {
                if (!insertItem(stackInSlot, FUEL_SLOT_INDEX, OUTPUT_SLOT_INDEX, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (isSmeltable(this.slots.get(INPUT_SLOT_INDEX), stackInSlot)) {
                if (!insertItem(stackInSlot, INPUT_SLOT_INDEX, FLUX_SLOT_INDEX, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= PLAYER_INVENTORY_START_INDEX && slotIndex < PLAYER_HOTBAR_START_INDEX) {
                if (!insertItem(stackInSlot, PLAYER_HOTBAR_START_INDEX, PLAYER_INVENTORY_END_INDEX, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= PLAYER_HOTBAR_START_INDEX &&
                    slotIndex < PLAYER_INVENTORY_END_INDEX &&
                    !insertItem(stackInSlot, PLAYER_INVENTORY_START_INDEX, PLAYER_HOTBAR_START_INDEX, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!insertItem(stackInSlot, PLAYER_INVENTORY_START_INDEX, PLAYER_INVENTORY_END_INDEX, false)) {
            return ItemStack.EMPTY;
        }

        if (stackInSlot.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }

        if (stackInSlot.getCount() == stackCopy.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTakeItem(player, stackInSlot);
        return stackCopy;
    }

    @Override
    public boolean canUse(PlayerEntity player) { return canUse(this.context, player, AllBlocks.REFRACTORY_FURNACE); }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.blockEntity.getInventory().onClose(player);
    }

    //region Screen Progress Bars
    public boolean isBurning() {
        return this.delegate.get(RefractoryFurnaceBlockEntity.PROPERTY_DELEGATE_LIT_TIME_INDEX) > 0;
    }

    public float getCookProgress() {
        int progress = this.delegate.get(RefractoryFurnaceBlockEntity.PROPERTY_DELEGATE_PROGRESS_INDEX);
        int maxProgress = this.delegate.get(RefractoryFurnaceBlockEntity.PROPERTY_DELEGATE_MAX_PROGRESS_INDEX);
        if (progress == 0 || maxProgress == 0) {
            return 0.0F;
        }
        return MathHelper.clamp((float) progress / (float) maxProgress, 0.0F, 1.0F);
    }

    public float getFuelProgress() {
        int litTime = this.delegate.get(RefractoryFurnaceBlockEntity.PROPERTY_DELEGATE_LIT_TIME_INDEX);
        int maxLitTime = this.delegate.get(RefractoryFurnaceBlockEntity.PROPERTY_DELEGATE_MAX_LIT_TIME_INDEX);
        if (maxLitTime == 0) {
            maxLitTime = 200;
        }
        return MathHelper.clamp((float) litTime / (float) maxLitTime, 0.0F, 1.0F);
    }
    //endregion

    protected boolean isFuel(ItemStack item) { return this.world.getFuelRegistry().isFuel(item); }

    protected boolean isFlux(ItemStack item) { return item.isOf(AllItems.FLUX); }

    protected boolean isSmeltable(@Nullable Slot slot, ItemStack stack) {
        //TODO: add recipe check, if it's not a valid recipe cycle in player inventory
        return canInsertItemIntoSlot(slot, stack, false);
    }
}
