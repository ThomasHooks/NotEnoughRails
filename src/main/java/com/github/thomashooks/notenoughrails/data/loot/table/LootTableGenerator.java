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
package com.github.thomashooks.notenoughrails.data.loot.table;

import com.github.thomashooks.notenoughrails.world.block.AllBlocks;
import com.github.thomashooks.notenoughrails.world.block.FlaxCropBlock;
import com.github.thomashooks.notenoughrails.world.item.AllItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class LootTableGenerator extends FabricBlockLootTableProvider {
    public LootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(AllBlocks.COKE_BLOCK);
        addDrop(AllBlocks.CORITE_BLOCK);
        addDrop(AllBlocks.CORITE_CHISELED_BLOCK);
        addDrop(AllBlocks.CORITE_CUT_BLOCK);
        addDrop(AllBlocks.CORITE_CUT_SLAB, slabDrops(AllBlocks.CORITE_CUT_SLAB));
        addDrop(AllBlocks.CORITE_CUT_STAIRS);
        addDrop(AllBlocks.CORITE_DOOR, doorDrops(AllBlocks.CORITE_DOOR));
        addDrop(AllBlocks.CORITE_GRATE);
        addDrop(AllBlocks.CORITE_PLATE_BLOCK);
        BlockStatePropertyLootCondition.Builder flaxCropBuilder = BlockStatePropertyLootCondition.builder(AllBlocks.FLAX_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(FlaxCropBlock.AGE, FlaxCropBlock.MAX_AGE));
        this.addDrop(AllBlocks.FLAX_CROP, this.cropDrops(AllBlocks.FLAX_CROP, AllItems.FLAX, AllItems.FLAXSEEDS, flaxCropBuilder));
        addDrop(AllBlocks.FLUXSTONE);
        addDrop(AllBlocks.FLUXSTONE_POLISHED);
        addDrop(AllBlocks.FLUXSTONE_SMOOTH);
        addDrop(AllBlocks.FLUXSTONE_SMOOTH_SLAB, slabDrops(AllBlocks.FLUXSTONE_SMOOTH_SLAB));
        addDrop(AllBlocks.FLUXSTONE_SMOOTH_STAIRS);
        addDrop(AllBlocks.IRON_PLATE_BLOCK);
        addDrop(AllBlocks.LINEN_BLOCK);
        addDrop(AllBlocks.VERMILION_BLOCK);
        addDrop(AllBlocks.WOODEN_FRAME);
    }
}
