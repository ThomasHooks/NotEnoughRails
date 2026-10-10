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
package com.github.thomashooks.notenoughrails.block;

import com.github.thomashooks.notenoughrails.block.property.AllProperties;
import com.github.thomashooks.notenoughrails.energy.KinematicBlockProvider;
import com.github.thomashooks.notenoughrails.energy.MechanicalConnection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public abstract class RotatingShaftBlock extends Block implements KinematicBlockProvider {
    public static final BooleanProperty SHAFT_IS_SHIFTED = AllProperties.SHAFT_IS_SHIFTED;
    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;

    protected RotatingShaftBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(SHAFT_IS_SHIFTED, false)
                .with(AXIS, Direction.Axis.Y)
        );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return Objects.requireNonNull(super.getPlacementState(ctx)).with(AXIS, ctx.getSide().getAxis());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.isClient()) {
            return;
        }

        for (MechanicalConnection connection : getMechanicalConnections(world, pos, state)) {
            KinematicBlockProvider neighborBlockProvider = KinematicBlockProvider.getKinematicBlock(world, connection.pos());
            BlockState neighborState = world.getBlockState(connection.pos());
            if (neighborBlockProvider == null || !neighborBlockProvider.isAlignedWith(world, connection.pos(), neighborState, connection)) {
                continue;
            }

            boolean shifted = neighborState.get(SHAFT_IS_SHIFTED);
            if (connection.isCog() && neighborBlockProvider.isCogwheel(world, connection.pos())) {
                world.setBlockState(pos, state.with(SHAFT_IS_SHIFTED, !shifted));
            }  else {
                world.setBlockState(pos, state.with(SHAFT_IS_SHIFTED, shifted));
            }
            break;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAFT_IS_SHIFTED, AXIS);
    }
}
