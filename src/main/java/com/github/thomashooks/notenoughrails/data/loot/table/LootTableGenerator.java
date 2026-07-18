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

import com.github.thomashooks.notenoughrails.block.AllBlocks;
import com.github.thomashooks.notenoughrails.block.FlaxCropBlock;
import com.github.thomashooks.notenoughrails.item.AllItems;
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
        addDrop(AllBlocks.BRAKING_RAIL);
        addDrop(AllBlocks.BUFFER_STOP_RAIL);
        addDrop(AllBlocks.CHECK_RAIL);
        addDrop(AllBlocks.CHIME_RAIL);
        addDrop(AllBlocks.COKE_BLOCK);
        addDrop(AllBlocks.STEEL_ACTIVATOR_RAIL);
        addDrop(AllBlocks.STEEL_BLOCK);
        addDrop(AllBlocks.STEEL_CHISELED_BLOCK);
        addDrop(AllBlocks.STEEL_CUT_BLOCK);
        addDrop(AllBlocks.STEEL_CUT_SLAB, slabDrops(AllBlocks.STEEL_CUT_SLAB));
        addDrop(AllBlocks.STEEL_CUT_STAIRS);
        addDrop(AllBlocks.STEEL_BUFFER_STOP_RAIL);
        addDrop(AllBlocks.STEEL_CROSSOVER_RAIL);
        addDrop(AllBlocks.STEEL_DETECTOR_RAIL);
        addDrop(AllBlocks.STEEL_DOOR, doorDrops(AllBlocks.STEEL_DOOR));
        addDrop(AllBlocks.STEEL_GRATE);
        addDrop(AllBlocks.STEEL_PLATE_BLOCK);
        addDrop(AllBlocks.STEEL_POWERED_RAIL);
        addDrop(AllBlocks.STEEL_RAIL);
        addDrop(AllBlocks.STEEL_TRAPDOOR);
        addDrop(AllBlocks.COPPER_RAIL_WAXED);
        addDrop(AllBlocks.COPPER_ACTIVATOR_RAIL);
        addDrop(AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED);
        addDrop(AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED);
        addDrop(AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED);
        addDrop(AllBlocks.COPPER_ACTIVATOR_RAIL_WAXED);
        addDrop(AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED_WAXED);
        addDrop(AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED_WAXED);
        addDrop(AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED_WAXED);
        addDrop(AllBlocks.COPPER_BUFFER_STOP_RAIL);
        addDrop(AllBlocks.COPPER_BUFFER_STOP_RAIL_EXPOSED);
        addDrop(AllBlocks.COPPER_BUFFER_STOP_RAIL_WEATHERED);
        addDrop(AllBlocks.COPPER_BUFFER_STOP_RAIL_OXIDIZED);
        addDrop(AllBlocks.COPPER_BUFFER_STOP_RAIL_WAXED);
        addDrop(AllBlocks.COPPER_BUFFER_STOP_RAIL_EXPOSED_WAXED);
        addDrop(AllBlocks.COPPER_BUFFER_STOP_RAIL_WEATHERED_WAXED);
        addDrop(AllBlocks.COPPER_BUFFER_STOP_RAIL_OXIDIZED_WAXED);
        addDrop(AllBlocks.COPPER_CROSSOVER_RAIL);
        addDrop(AllBlocks.COPPER_CROSSOVER_RAIL_EXPOSED);
        addDrop(AllBlocks.COPPER_CROSSOVER_RAIL_WEATHERED);
        addDrop(AllBlocks.COPPER_CROSSOVER_RAIL_OXIDIZED);
        addDrop(AllBlocks.COPPER_CROSSOVER_RAIL_WAXED);
        addDrop(AllBlocks.COPPER_CROSSOVER_RAIL_EXPOSED_WAXED);
        addDrop(AllBlocks.COPPER_CROSSOVER_RAIL_WEATHERED_WAXED);
        addDrop(AllBlocks.COPPER_CROSSOVER_RAIL_OXIDIZED_WAXED);
        addDrop(AllBlocks.COPPER_DETECTOR_RAIL);
        addDrop(AllBlocks.COPPER_DETECTOR_RAIL_EXPOSED);
        addDrop(AllBlocks.COPPER_DETECTOR_RAIL_WEATHERED);
        addDrop(AllBlocks.COPPER_DETECTOR_RAIL_OXIDIZED);
        addDrop(AllBlocks.COPPER_DETECTOR_RAIL_WAXED);
        addDrop(AllBlocks.COPPER_DETECTOR_RAIL_EXPOSED_WAXED);
        addDrop(AllBlocks.COPPER_DETECTOR_RAIL_WEATHERED_WAXED);
        addDrop(AllBlocks.COPPER_DETECTOR_RAIL_OXIDIZED_WAXED);
        addDrop(AllBlocks.COPPER_POWERED_RAIL);
        addDrop(AllBlocks.COPPER_POWERED_RAIL_EXPOSED);
        addDrop(AllBlocks.COPPER_POWERED_RAIL_WEATHERED);
        addDrop(AllBlocks.COPPER_POWERED_RAIL_OXIDIZED);
        addDrop(AllBlocks.COPPER_POWERED_RAIL_WAXED);
        addDrop(AllBlocks.COPPER_POWERED_RAIL_EXPOSED_WAXED);
        addDrop(AllBlocks.COPPER_POWERED_RAIL_WEATHERED_WAXED);
        addDrop(AllBlocks.COPPER_POWERED_RAIL_OXIDIZED_WAXED);
        addDrop(AllBlocks.COPPER_RAIL);
        addDrop(AllBlocks.COPPER_RAIL_EXPOSED);
        addDrop(AllBlocks.COPPER_RAIL_WEATHERED);
        addDrop(AllBlocks.COPPER_RAIL_OXIDIZED);
        addDrop(AllBlocks.COPPER_RAIL_WAXED);
        addDrop(AllBlocks.COPPER_RAIL_EXPOSED_WAXED);
        addDrop(AllBlocks.COPPER_RAIL_WEATHERED_WAXED);
        addDrop(AllBlocks.COPPER_RAIL_OXIDIZED_WAXED);
        addDrop(AllBlocks.CROSSOVER_RAIL);
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
