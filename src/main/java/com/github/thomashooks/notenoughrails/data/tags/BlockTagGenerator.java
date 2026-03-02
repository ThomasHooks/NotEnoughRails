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
package com.github.thomashooks.notenoughrails.data.tags;

import com.github.thomashooks.notenoughrails.world.block.AllBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.@NotNull WrapperLookup wrapperLookup) {
        valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(AllBlocks.CORITE_BLOCK)
                .add(AllBlocks.CORITE_CHISELED_BLOCK)
                .add(AllBlocks.CORITE_CUT_BLOCK)
                .add(AllBlocks.CORITE_CUT_SLAB)
                .add(AllBlocks.CORITE_CUT_STAIRS)
                .add(AllBlocks.CORITE_DOOR)
                .add(AllBlocks.CORITE_GRATE)
                .add(AllBlocks.CORITE_PLATE_BLOCK)
                .add(AllBlocks.FLUXSTONE)
                .add(AllBlocks.FLUXSTONE_POLISHED)
                .add(AllBlocks.IRON_PLATE_BLOCK)
                .add(AllBlocks.VERMILION_BLOCK)
        ;

        valueLookupBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(AllBlocks.CORITE_BLOCK)
                .add(AllBlocks.CORITE_CHISELED_BLOCK)
                .add(AllBlocks.CORITE_CUT_BLOCK)
                .add(AllBlocks.CORITE_CUT_SLAB)
                .add(AllBlocks.CORITE_CUT_STAIRS)
                .add(AllBlocks.CORITE_DOOR)
                .add(AllBlocks.CORITE_GRATE)
                .add(AllBlocks.CORITE_PLATE_BLOCK)
                .add(AllBlocks.FLUXSTONE)
                .add(AllBlocks.FLUXSTONE_POLISHED)
                .add(AllBlocks.IRON_PLATE_BLOCK)
        ;

        valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(AllBlocks.VERMILION_BLOCK)
        ;
    }
}
