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

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailPlacementHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RailPlacementHelper.class)
public abstract class MixinRailPlacementHelper {
    @Shadow
    @Final
    private AbstractRailBlock block;
    @Unique
    private boolean forbidSlopes;

    @Inject(method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/RailPlacementHelper;computeNeighbors(Lnet/minecraft/block/enums/RailShape;)V"))
    private void initializeForbidSlopes(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        this.forbidSlopes = this.block.notEnoughRails$isFlatRail(state, world, pos);
    }

    @ModifyExpressionValue(method = "computeRailShape",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean onlyMakeSlopesIfAllowedInComputeRailShape(boolean original) {
        return original && !this.forbidSlopes;
    }

    @ModifyExpressionValue(method = "updateBlockState",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean onlyMakeSlopesIfAllowedInUpdateBlockState(boolean original) {
        return original && !this.forbidSlopes;
    }
}
