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
import com.github.thomashooks.notenoughrails.block.entity.CokeOvenBlockEntity;
import com.github.thomashooks.notenoughrails.network.BlockPosPayload;
import com.github.thomashooks.notenoughrails.screen.slot.CraftingOutputSlot;
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
import org.jspecify.annotations.Nullable;

public class CokeOvenScreenHandler extends ScreenHandler {
    private static final int NUMBER_OF_PLAYERS_INVENTORY_SLOTS = 36;
    private static final int INPUT_SLOT_INDEX = CokeOvenBlockEntity.INPUT_SLOT_INDEX;
    private static final int OUTPUT_SLOT_INDEX = CokeOvenBlockEntity.OUTPUT_SLOT_INDEX;
    private static final int NUMBER_OF_SLOTS = CokeOvenBlockEntity.NUMBER_OF_SLOTS;
    private static final int PLAYER_INVENTORY_START_INDEX = NUMBER_OF_SLOTS;
    private static final int PLAYER_INVENTORY_END_INDEX = PLAYER_INVENTORY_START_INDEX + NUMBER_OF_PLAYERS_INVENTORY_SLOTS;
    private static final int PLAYER_HOTBAR_START_INDEX = PLAYER_INVENTORY_END_INDEX - 9;
    private final CokeOvenBlockEntity blockEntity;
    private final ScreenHandlerContext context;
    private final PropertyDelegate delegate;
    private static final int PROPERTY_DELEGATE_SIZE = CokeOvenBlockEntity.PROPERTY_DELEGATE_SIZE;

    //Server Side
    public CokeOvenScreenHandler(int syncId, PlayerInventory playerInventory, CokeOvenBlockEntity blockEntity, PropertyDelegate delegate) {
        super(AllScreenHandlers.COKE_OVEN, syncId);
        this.blockEntity = blockEntity;
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(), this.blockEntity.getPos());

        SimpleInventory inventory = blockEntity.getInventory();
        checkSize(inventory, NUMBER_OF_SLOTS);
        inventory.onOpen(playerInventory.player);
        checkDataCount(delegate, PROPERTY_DELEGATE_SIZE);
        this.delegate = delegate;

        addSlot(new Slot(inventory, INPUT_SLOT_INDEX, 55, 21));
        addSlot(new CraftingOutputSlot(inventory, OUTPUT_SLOT_INDEX, 114, 21));
        addPlayerSlots(playerInventory, 8, 52);
        addProperties(delegate);
    }

    //Client Side
    public CokeOvenScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(
                syncId,
                playerInventory,
                (CokeOvenBlockEntity) playerInventory.player.getEntityWorld().getBlockEntity(payload.pos()),
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
            if (!this.insertItem(stackInSlot, PLAYER_INVENTORY_START_INDEX, PLAYER_INVENTORY_END_INDEX, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickTransfer(stackInSlot, stackCopy);
        } else if (slotIndex != INPUT_SLOT_INDEX) {
            if (isSmeltable(this.getSlot(INPUT_SLOT_INDEX), stackInSlot)) {
                if (!this.insertItem(stackInSlot, INPUT_SLOT_INDEX, OUTPUT_SLOT_INDEX, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= NUMBER_OF_SLOTS && slotIndex < PLAYER_HOTBAR_START_INDEX) {
                if (!this.insertItem(stackInSlot, PLAYER_HOTBAR_START_INDEX, PLAYER_INVENTORY_END_INDEX, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= PLAYER_HOTBAR_START_INDEX && slotIndex < PLAYER_INVENTORY_END_INDEX) {
                if (!this.insertItem(stackInSlot, NUMBER_OF_SLOTS, PLAYER_HOTBAR_START_INDEX, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else if (!this.insertItem(stackInSlot, PLAYER_INVENTORY_START_INDEX, PLAYER_INVENTORY_END_INDEX, false)) {
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
    public boolean canUse(PlayerEntity player) { return canUse(this.context, player, AllBlocks.COKE_OVEN); }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.blockEntity.getInventory().onClose(player);
    }

    public float getProgress() {
        int progress = this.delegate.get(CokeOvenBlockEntity.PROPERTY_DELEGATE_PROGRESS_INDEX);
        int maxProgress = this.delegate.get(CokeOvenBlockEntity.PROPERTY_DELEGATE_MAX_PROGRESS_INDEX);
        if (progress == 0 || maxProgress == 0) {
            return 0.0F;
        }

        return MathHelper.clamp((float) progress / (float) maxProgress, 0.0F, 1.0F);
    }

    protected boolean isSmeltable(@Nullable Slot slot, ItemStack stack) {
        //TODO: add recipe check, if it's not a valid recipe cycle in player inventory
        return canInsertItemIntoSlot(slot, stack, false);
    }
}
