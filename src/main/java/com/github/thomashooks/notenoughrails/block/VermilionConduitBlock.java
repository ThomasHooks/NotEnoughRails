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
import com.github.thomashooks.notenoughrails.block.property.PowerDirection;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.OrientationHelper;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.TickPriority;
import org.jspecify.annotations.Nullable;

public class VermilionConduitBlock extends Block {
    public static final MapCodec<VermilionConduitBlock> CODEC = createCodec(VermilionConduitBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;
    public static final IntProperty POWER = Properties.POWER;
    public static final EnumProperty<PowerDirection> POWER_DIRECTION = AllProperties.POWER_DIRECTION;
    private static final int[] COLORS = Util.make(new int[16], colors -> {
        for (int i = 0; i <= 15; i++) {
            float power = i / 15.0F;
            float red = (power * 0.6F) + 0.4F;
            float green = MathHelper.clamp(power * power * 0.8176F - 0.5F, 0.0F, 1.0F);
            float blue = MathHelper.clamp(power * power * 0.9157F - 0.7F, 0.0F, 1.0F);
            colors[i] = ColorHelper.fromFloats(1.0F, red, green, blue);
        }
    });
    public static final int UPDATE_DELAY = 1;

    public VermilionConduitBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(AXIS, Direction.Axis.Y)
                .with(POWER, 0)
                .with(POWER_DIRECTION, PowerDirection.NONE)
        );
    }

    @Override
    protected MapCodec<? extends Block> getCodec() { return CODEC; }

    public static int getConduitColor(int powerLevel) { return COLORS[powerLevel]; }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(AXIS, ctx.getSide().getAxis());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (getReceivedPower(world, pos, state) > 0) {
            world.scheduleBlockTick(pos, this, UPDATE_DELAY);
        }
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        updateTarget(world, pos, state);
        if (oldState.isOf(Blocks.MOVING_PISTON)) {
            // Only happens if the conduit has been pushed by a piston
            // Doing this keeps connected conduits and redstone wires from oscillating
            world.scheduleBlockTick(pos, this, UPDATE_DELAY, TickPriority.EXTREMELY_HIGH);
        }
    }

    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        if (!moved) {
            updateTarget(world, pos, state);
        }
    }

    private void updateTarget(World world, BlockPos pos, BlockState state) {
        Direction[] directions = state.get(AXIS).getDirections();
        for (Direction direction : directions) {
            BlockPos offsetPos = pos.offset(direction.getOpposite());
            WireOrientation wireOrientation = OrientationHelper.getEmissionOrientation(world, direction.getOpposite(), Direction.UP);
            world.updateNeighborsExcept(offsetPos, this, direction, wireOrientation);
            world.updateNeighbor(offsetPos, this, wireOrientation);
        }
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (hasReceivedPowerChanged(world, pos, state)) {
            world.scheduleBlockTick(pos, this, UPDATE_DELAY, TickPriority.HIGH);
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int receivedPower = getReceivedPower(world, pos, state);
        if (receivedPower == 0) {
            world.setBlockState(
                    pos,
                    state.with(POWER, 0).with(POWER_DIRECTION, PowerDirection.NONE),
                    Block.NOTIFY_LISTENERS
            );
        } else {
            PowerDirection powerDir = state.get(POWER_DIRECTION);
            switch (powerDir) {
                case DOWN, UP, NORTH, SOUTH, WEST, EAST -> {
                    Direction direction = powerDir.asDirection();
                    updateBlockState(state, world, pos, direction, receivedPower);
                }
                case NONE -> {
                    // This should only happen if the conduit isn't currently powered
                    Direction[] directions = state.get(AXIS).getDirections();
                    for (Direction direction : directions) {
                        updateBlockState(state, world, pos, direction, receivedPower);
                    }
                }
            }
        }
    }

    private void updateBlockState(BlockState state, ServerWorld world, BlockPos pos, Direction direction, int receivedPower) {
        BlockPos otherPos = pos.offset(direction);
        int power = world.getEmittedRedstonePower(otherPos, direction);
        if (power != receivedPower) {
            return;
        }

        power = reducedEmittedPower(world, otherPos, power);
        if (power > 0) {
            world.setBlockState(
                    pos,
                    state.with(POWER, power).with(POWER_DIRECTION, PowerDirection.byDirection(direction)),
                    Block.NOTIFY_LISTENERS
            );
        } else {
            world.setBlockState(
                    pos,
                    state.with(POWER, 0).with(POWER_DIRECTION, PowerDirection.NONE),
                    Block.NOTIFY_LISTENERS
            );
        }
    }

    private boolean hasReceivedPowerChanged(World world, BlockPos pos, BlockState state) {
        PowerDirection powerDir = state.get(POWER_DIRECTION);
        if (powerDir == PowerDirection.NONE) {
            return getReceivedPower(world, pos, state) > 0;
        }

        Direction direction = powerDir.asDirection();
        BlockPos otherPos = pos.offset(direction);
        int power = world.getEmittedRedstonePower(otherPos, direction);
        power = reducedEmittedPower(world, otherPos, power);
        return power != state.get(POWER);
    }

    private int getReceivedPower(World world, BlockPos pos, BlockState state) {
        PowerDirection powerDir = state.get(POWER_DIRECTION);
        switch (powerDir) {
            case DOWN, UP, NORTH, SOUTH, WEST, EAST -> {
                Direction direction = powerDir.asDirection();
                BlockPos otherPos = pos.offset(direction);
                int power = world.getEmittedRedstonePower(otherPos, direction);
                if (power > 0) {
                    return power;
                }
            }
            case NONE -> {
                // This condition should only happen if the conduit isn't currently powered
                Direction[] directions = state.get(AXIS).getDirections();
                for (Direction direction : directions) {
                    BlockPos otherPos = pos.offset(direction);
                    int power = world.getEmittedRedstonePower(otherPos, direction);
                    if (power > 0) {
                        return power;
                    }
                }
            }
        }
        return 0;
    }

    private int reducedEmittedPower(World world, BlockPos otherPos, int power) {
        boolean isRedstonePowerSource = !world.getBlockState(otherPos).isOf(Blocks.REDSTONE_WIRE) &&
                !world.getBlockState(otherPos).isOf(this);
        return !isRedstonePowerSource ? Math.max(0, power - 1) : power;
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) { return true; }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        // This keeps the conduit from powering itself through redstone dust or another conduit
        PowerDirection powerDir = state.get(POWER_DIRECTION);
        BlockState otherState = world.getBlockState(pos.offset(direction.getOpposite()));
        boolean isRedstoneWireSelfPowering = otherState.isOf(Blocks.REDSTONE_WIRE) && powerDir == PowerDirection.byDirection(direction.getOpposite());
        boolean isConduitSelfPowering = otherState.isOf(this) && otherState.get(POWER_DIRECTION).getOpposite() == powerDir;
        if (isRedstoneWireSelfPowering || isConduitSelfPowering) {
            return 0;
        }

        int power = state.get(POWER);
        if (world.getBlockState(pos.offset(direction.getOpposite())).isOf(Blocks.REDSTONE_WIRE)) {
            // This keeps redstone dust from have the same signal strength as the conduit
            power = Math.max(0, power - 1);
        }

        return direction.getAxis() == state.get(AXIS) ? power : 0;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) { return changeRotation(state, rotation); }

    public static BlockState changeRotation(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.get(AXIS)) {
                case X -> state.with(AXIS, Direction.Axis.Z);
                case Z -> state.with(AXIS, Direction.Axis.X);
                default -> state;
            };
            default -> state;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS, POWER, POWER_DIRECTION);
    }
}
