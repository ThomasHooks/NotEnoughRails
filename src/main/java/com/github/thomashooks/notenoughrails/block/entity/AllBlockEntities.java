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
import com.github.thomashooks.notenoughrails.block.AllBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AllBlockEntities {
    public static final BlockEntityType<CokeOvenBlockEntity> COKE_OVEN = Registry.register(Registries.BLOCK_ENTITY_TYPE, NotEnoughRails.identifier("coke_oven"),
            FabricBlockEntityTypeBuilder.create(CokeOvenBlockEntity::new, AllBlocks.COKE_OVEN).build());
    public static final BlockEntityType<LockingRailBlockEntity> LOCKING_RAIL = Registry.register(Registries.BLOCK_ENTITY_TYPE, NotEnoughRails.identifier("locking_rail"),
            FabricBlockEntityTypeBuilder.create(LockingRailBlockEntity::new, AllBlocks.LOCKING_RAIL).build());

    public static void registerAll() {
        // We have to do this otherwise the block entities won't get created
        NotEnoughRails.LOGGER.info("Registering all Block Entities");
    }

    public static void registerAllStorageBlockEntities() {
        NotEnoughRails.LOGGER.info("Registering storage block entities");
        ItemStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> InventoryStorage.of(blockEntity.getInventory(), direction),
                AllBlockEntities.COKE_OVEN
        );
    }
}
