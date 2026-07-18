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
package com.github.thomashooks.notenoughrails;

import com.github.thomashooks.notenoughrails.block.AllBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.render.BlockRenderLayer;

@Environment(EnvType.CLIENT)
public class NotEnoughRailsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerBlockRenderLayer();
        registerColorProviderBlock();
    }

    private void registerBlockRenderLayer() {
        NotEnoughRails.LOGGER.info("Registering BlockRenderLayer");

        //To make some parts of the block transparent (like glass, saplings and doors):
        BlockRenderLayerMap.putBlock(AllBlocks.BRAKING_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.BUFFER_STOP_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.CHECK_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.CHIME_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.STEEL_ACTIVATOR_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.STEEL_BUFFER_STOP_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.STEEL_GRATE, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.STEEL_CROSSOVER_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.STEEL_DETECTOR_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.STEEL_DOOR, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.STEEL_POWERED_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.STEEL_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.STEEL_TRAPDOOR, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_ACTIVATOR_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_ACTIVATOR_RAIL_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_BUFFER_STOP_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_BUFFER_STOP_RAIL_EXPOSED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_BUFFER_STOP_RAIL_WEATHERED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_BUFFER_STOP_RAIL_OXIDIZED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_BUFFER_STOP_RAIL_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_BUFFER_STOP_RAIL_EXPOSED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_BUFFER_STOP_RAIL_WEATHERED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_BUFFER_STOP_RAIL_OXIDIZED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_CROSSOVER_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_CROSSOVER_RAIL_EXPOSED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_CROSSOVER_RAIL_WEATHERED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_CROSSOVER_RAIL_OXIDIZED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_CROSSOVER_RAIL_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_CROSSOVER_RAIL_EXPOSED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_CROSSOVER_RAIL_WEATHERED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_CROSSOVER_RAIL_OXIDIZED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_DETECTOR_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_DETECTOR_RAIL_EXPOSED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_DETECTOR_RAIL_WEATHERED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_DETECTOR_RAIL_OXIDIZED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_DETECTOR_RAIL_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_DETECTOR_RAIL_EXPOSED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_DETECTOR_RAIL_WEATHERED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_DETECTOR_RAIL_OXIDIZED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_POWERED_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_POWERED_RAIL_EXPOSED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_POWERED_RAIL_WEATHERED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_POWERED_RAIL_OXIDIZED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_POWERED_RAIL_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_POWERED_RAIL_EXPOSED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_POWERED_RAIL_WEATHERED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_POWERED_RAIL_OXIDIZED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_RAIL_EXPOSED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_RAIL_WEATHERED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_RAIL_OXIDIZED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_RAIL_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_RAIL_EXPOSED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_RAIL_WEATHERED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.COPPER_RAIL_OXIDIZED_WAXED, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.CROSSOVER_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.FLAX_CROP, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(AllBlocks.LOCKING_RAIL, BlockRenderLayer.CUTOUT);
    }

    private void registerColorProviderBlock() {
        NotEnoughRails.LOGGER.info("Registering BlockColorProvider");

        ColorProviderRegistry.BLOCK.register(
                (state, view, pos, tintIndex) -> RedstoneWireBlock.getWireColor(state.get(RedstoneWireBlock.POWER)),
                AllBlocks.BRAKING_RAIL
        );
    }
}
