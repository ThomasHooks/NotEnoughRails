package com.github.thomashooks.notenoughrails.mixin;

import com.github.thomashooks.notenoughrails.block.ExtendedRailBehavior;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractRailBlock.class)
abstract class MixinAbstractRailBlock implements ExtendedRailBehavior {
    @Override
    public RailShape notEnoughRails$getRailDirection(BlockState state, World world, BlockPos pos, @Nullable AbstractMinecartEntity minecart) {
        return state.get(((AbstractRailBlock) (Object) this).getShapeProperty());
    }
}
