package com.github.thomashooks.notenoughrails;

import com.github.thomashooks.notenoughrails.world.block.AllBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

@Environment(EnvType.CLIENT)
public class NotEnoughRailsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //To make some parts of the block transparent (like glass, saplings and doors):
        BlockRenderLayerMap.putBlock(AllBlocks.CORITE_GRATE, BlockRenderLayer.CUTOUT);
    }
}
