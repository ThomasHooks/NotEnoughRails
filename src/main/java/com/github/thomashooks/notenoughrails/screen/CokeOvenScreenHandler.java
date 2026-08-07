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

public class CokeOvenScreenHandler extends ScreenHandler {
    private static final int NUMBER_OF_PLAYERS_INVENTORY_SLOTS = 36;
    private static final int PLAYER_INVENTORY_START_INDEX = CokeOvenBlockEntity.NUMBER_OF_SLOTS;
    private static final int PLAYER_INVENTORY_END_INDEX = PLAYER_INVENTORY_START_INDEX + NUMBER_OF_PLAYERS_INVENTORY_SLOTS;
    private static final int PLAYER_HOTBAR_START_INDEX = PLAYER_INVENTORY_END_INDEX - 9;

    private final CokeOvenBlockEntity blockEntity;
    private final ScreenHandlerContext context;
    private final PropertyDelegate delegate;

    //Server Side
    public CokeOvenScreenHandler(int syncId, PlayerInventory playerInventory, CokeOvenBlockEntity blockEntity, PropertyDelegate delegate) {
        super(AllScreenHandlers.COKE_OVEN, syncId);
        this.blockEntity = blockEntity;
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(), this.blockEntity.getPos());

        SimpleInventory inventory = blockEntity.getInventory();
        checkSize(inventory, CokeOvenBlockEntity.NUMBER_OF_SLOTS);
        inventory.onOpen(playerInventory.player);
        checkDataCount(delegate, CokeOvenBlockEntity.PROPERTY_DELEGATE_SIZE);
        this.delegate = delegate;

        addSlot(new Slot(inventory, CokeOvenBlockEntity.INPUT_SLOT_INDEX, 55, 21));
        addSlot(new CraftingOutputSlot(inventory, CokeOvenBlockEntity.OUTPUT_SLOT_INDEX, 114, 21));
        addPlayerInventorySlots(playerInventory, 8, 52);
        addProperties(delegate);
    }

    //Client Side
    public CokeOvenScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (CokeOvenBlockEntity) playerInventory.player.getEntityWorld().getBlockEntity(payload.pos()), new ArrayPropertyDelegate(2));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (slot == null || !slot.hasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack stackInSlot = slot.getStack();
        ItemStack stackCopy = stackInSlot.copy();
        if (slotIndex == CokeOvenBlockEntity.OUTPUT_SLOT_INDEX) {
            if (!this.insertItem(stackInSlot, PLAYER_INVENTORY_START_INDEX, PLAYER_INVENTORY_END_INDEX, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickTransfer(stackInSlot, stackCopy);
        } else if (slotIndex != CokeOvenBlockEntity.INPUT_SLOT_INDEX) {
            //TODO: add recipe check, if it's not a valid recipe cycle in player inventory
            if (!this.insertItem(stackInSlot, CokeOvenBlockEntity.INPUT_SLOT_INDEX, CokeOvenBlockEntity.OUTPUT_SLOT_INDEX, false)) {
                return ItemStack.EMPTY;
            } else if (slotIndex >= CokeOvenBlockEntity.NUMBER_OF_SLOTS && slotIndex < PLAYER_HOTBAR_START_INDEX) {
                if (!this.insertItem(stackInSlot, PLAYER_HOTBAR_START_INDEX, PLAYER_INVENTORY_END_INDEX, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= PLAYER_HOTBAR_START_INDEX && slotIndex < PLAYER_INVENTORY_END_INDEX) {
                if (!this.insertItem(stackInSlot, CokeOvenBlockEntity.NUMBER_OF_SLOTS, PLAYER_HOTBAR_START_INDEX, false)) {
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

    /**
     * Adds the player's inventory and quick bar slots to this container
     * @param slotX The X coordinate of the top-left inventory slot inside corner in the player's inventory
     * @param slotY The Y coordinate of the top-left inventory slot inside corner in the player's inventory
     */
    protected void addPlayerInventorySlots(PlayerInventory playerInv, int slotX, int slotY) {
        final int SLOT_WIDTH = 18;
        final int SLOT_HEIGHT = 18;
        final int HOTBAR_HEIGHT_OFFSET = 58;
        //Player's hotbar
        int index = 0;
        for (int colum = 0; colum < 9; colum++) {
            addSlot(new Slot(playerInv, index, slotX + (colum * SLOT_WIDTH), slotY + HOTBAR_HEIGHT_OFFSET));
            index++;
        }

        //Player's inventory
        for (int row = 0; row < 3; row++) {
            for (int colum = 0; colum < 9; colum++) {
                addSlot(new Slot(playerInv, index, slotX + (colum * SLOT_WIDTH), slotY + (row * SLOT_HEIGHT)));
                index++;
            }
        }
    }
}
