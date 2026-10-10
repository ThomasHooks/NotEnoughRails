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

import com.github.thomashooks.notenoughrails.block.entity.AllBlockEntities;
import com.github.thomashooks.notenoughrails.block.entity.LazyTickingBlockEntity;
import com.github.thomashooks.notenoughrails.block.entity.QuernBlockEntity;
import com.github.thomashooks.notenoughrails.block.property.AllProperties;
import com.github.thomashooks.notenoughrails.energy.KinematicBlockProvider;
import com.github.thomashooks.notenoughrails.energy.MechanicalConnection;
import com.github.thomashooks.notenoughrails.util.VoxelShapeHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

public class QuernBlock extends Block implements BlockEntityProvider, KinematicBlockProvider {
    public static final MapCodec<QuernBlock> CODEC = createCodec(QuernBlock::new);
    public static final BooleanProperty IS_MILLING = AllProperties.IS_MILLING;
    public static final BooleanProperty IS_OVERLOADED = AllProperties.IS_OVERLOADED;
    public static final BooleanProperty SHAFT_IS_SHIFTED = AllProperties.SHAFT_IS_SHIFTED;
    private static final VoxelShape RUNNER_STONE_VOXEL_SHAPE = Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 12.0, 16.0);

    public QuernBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(IS_MILLING, false)
                .with(IS_OVERLOADED, false)
                .with(SHAFT_IS_SHIFTED, false)
        );
    }

    @Override
    protected MapCodec<? extends Block> getCodec() { return CODEC; }

    //region Block Methods
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double xPos = pos.getX() + 0.5;
        double yPos = pos.getY() + 0.5;
        double zPos = pos.getZ() + 0.5;

        if (state.get(IS_OVERLOADED)) {
            double randomShift = random.nextDouble() * 0.6 - 0.3;
            double yAxisOffset = random.nextDouble() * 0.375;
            world.addParticleClient(ParticleTypes.LARGE_SMOKE, xPos + randomShift, yPos + yAxisOffset, zPos + randomShift, 0.0, 0.0, 0.0);
        } else if (state.get(IS_MILLING) && world.getBlockEntity(pos) instanceof QuernBlockEntity blockEntity) {
            ItemStack itemStack = blockEntity.getMillingItemStack();
            if (itemStack.isEmpty()) {
                return;
            }

            for (Direction direction : Direction.Type.HORIZONTAL) {
                Direction.Axis axis = direction.getAxis();
                double randomShift = random.nextDouble() * 0.6 - 0.3;
                double xAxisOffset = axis == Direction.Axis.X ? direction.getOffsetX() * 0.52 : randomShift;
                double zAxisOffset = axis == Direction.Axis.Z ? direction.getOffsetZ() * 0.52 : randomShift;
                double yAxisOffset = random.nextDouble() * 0.125;
                double xVelocity = 0.0;
                double zVelocity = 0.0;
                switch (direction) {
                    case EAST ->  xVelocity = Math.abs(randomShift * 0.52);
                    case WEST -> xVelocity = -Math.abs(randomShift * 0.52);
                    case NORTH ->  zVelocity = -Math.abs(randomShift * 0.52);
                    case SOUTH ->  zVelocity = Math.abs(randomShift * 0.52);
                }
                world.addParticleClient(new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack), xPos + xAxisOffset, yPos + yAxisOffset, zPos + zAxisOffset, xVelocity, 0.0, zVelocity);
            }
        }
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
            world.setBlockState(pos, state.with(SHAFT_IS_SHIFTED, shifted));
            break;
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            if (world.getBlockEntity(pos) instanceof QuernBlockEntity blockEntity) {
                player.openHandledScreen(blockEntity);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) { return true; }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos, Direction direction) {
        int signalStrength = 0;
        if (world.getBlockEntity(pos) instanceof QuernBlockEntity blockEntity) {
            signalStrength = blockEntity.calculateComparatorOutput();
        }
        return signalStrength;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(
                VoxelShapeHelper.HALF_BLOCK,
                RUNNER_STONE_VOXEL_SHAPE,
                VoxelShapeHelper.AXLE[VoxelShapeHelper.getAxisIndex(Direction.Axis.Y)]
        ).simplify();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_MILLING, IS_OVERLOADED, SHAFT_IS_SHIFTED);
    }
    //endregion

    //region Kinematic Block Provider Methods
    @Override
    public float getSpeed(@NotNull World world, @NotNull BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof QuernBlockEntity blockEntity) {
            return blockEntity.getSpeed();
        }
        return 0.0F;
    }

    @Override
    public void changeSpeed(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockPos driverPos, float speedIn) {
        if (world.getBlockEntity(pos) instanceof QuernBlockEntity blockEntity) {
            blockEntity.setSpeed(driverPos, speedIn);
        }
    }

    @Override
    public @NotNull ArrayList<MechanicalConnection> getMechanicalConnections(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state) {
        if (world.getBlockEntity(pos) instanceof QuernBlockEntity blockEntity) {
            return blockEntity.getMechanicalConnections();
        }
        return new ArrayList<>();
    }
    //endregion

    //region Block Entity Provider Methods
    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return AllBlockEntities.QUERN.instantiate(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return LazyTickingBlockEntity.getTicker(world);
    }
    //endregion
}
