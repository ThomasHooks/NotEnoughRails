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

import com.github.thomashooks.notenoughrails.block.AllBlocks;
import com.github.thomashooks.notenoughrails.util.AllItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.@NotNull WrapperLookup wrapperLookup) {
        valueLookupBuilder(AllItemTags.LINEN_BLOCKS)
                .add(AllBlocks.LINEN_BLOCK.asItem())
                .add(AllBlocks.LINEN_BLOCK_WHITE.asItem())
                .add(AllBlocks.LINEN_BLOCK_LIGHT_GRAY.asItem())
                .add(AllBlocks.LINEN_BLOCK_GRAY.asItem())
                .add(AllBlocks.LINEN_BLOCK_BLACK.asItem())
                .add(AllBlocks.LINEN_BLOCK_BROWN.asItem())
                .add(AllBlocks.LINEN_BLOCK_RED.asItem())
                .add(AllBlocks.LINEN_BLOCK_ORANGE.asItem())
                .add(AllBlocks.LINEN_BLOCK_YELLOW.asItem())
                .add(AllBlocks.LINEN_BLOCK_LIME.asItem())
                .add(AllBlocks.LINEN_BLOCK_GREEN.asItem())
                .add(AllBlocks.LINEN_BLOCK_CYAN.asItem())
                .add(AllBlocks.LINEN_BLOCK_LIGHT_BLUE.asItem())
                .add(AllBlocks.LINEN_BLOCK_BLUE.asItem())
                .add(AllBlocks.LINEN_BLOCK_PURPLE.asItem())
                .add(AllBlocks.LINEN_BLOCK_MAGENTA.asItem())
                .add(AllBlocks.LINEN_BLOCK_PINK.asItem())
        ;
    }
}
