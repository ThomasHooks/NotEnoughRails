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
package com.github.thomashooks.notenoughrails.mixin;

import com.github.thomashooks.notenoughrails.block.AllBlocks;
import com.github.thomashooks.notenoughrails.util.AllBlockTags;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PoweredRailBlock.class)
public abstract class MixinPoweredRailBlock {
    @ModifyExpressionValue(method = "isPoweredByOtherRails(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ZILnet/minecraft/block/enums/RailShape;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private boolean isPoweredRail(boolean original, @Local(ordinal = 0)BlockState state) {
        Block block = ((PoweredRailBlock)(Object)this);
        return original || isCopperPoweredRail(block, state) || isCopperActivatorRail(block, state);
    }
    
    @Unique
    private boolean isCopperPoweredRail(Block current, BlockState other) {
        return other.isIn(AllBlockTags.COPPER_POWERED_RAILS) && (
                current == AllBlocks.COPPER_POWERED_RAIL_WAXED || current == AllBlocks.COPPER_POWERED_RAIL_EXPOSED_WAXED || current == AllBlocks.COPPER_POWERED_RAIL_WEATHERED_WAXED || current == AllBlocks.COPPER_POWERED_RAIL_OXIDIZED_WAXED ||
                        current == AllBlocks.COPPER_POWERED_RAIL || current == AllBlocks.COPPER_POWERED_RAIL_EXPOSED || current == AllBlocks.COPPER_POWERED_RAIL_WEATHERED || current == AllBlocks.COPPER_POWERED_RAIL_OXIDIZED
        );
    }

    @Unique
    private boolean isCopperActivatorRail(Block current, BlockState other) {
        return other.isIn(AllBlockTags.COPPER_ACTIVATOR_RAILS) && (
                current == AllBlocks.COPPER_ACTIVATOR_RAIL_WAXED || current == AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED_WAXED || current == AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED_WAXED || current == AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED_WAXED ||
                        current == AllBlocks.COPPER_ACTIVATOR_RAIL || current == AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED || current == AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED || current == AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED
        );
    }
}
