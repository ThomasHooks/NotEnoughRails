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
import com.github.thomashooks.notenoughrails.block.entity.MillstoneBlockEntity;
import com.github.thomashooks.notenoughrails.block.property.AllProperties;
import com.github.thomashooks.notenoughrails.util.VoxelShapeHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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
import org.jspecify.annotations.Nullable;

public class MillstoneBlock extends Block implements BlockEntityProvider {
    public static final MapCodec<MillstoneBlock> CODEC = createCodec(MillstoneBlock::new);
    public static final BooleanProperty IS_MILLING = AllProperties.IS_MILLING;
    private static final VoxelShape RUNNER_STONE_VOXEL_SHAPE = Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 12.0, 16.0);

    public MillstoneBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(IS_MILLING, false)
        );
    }

    @Override
    protected MapCodec<? extends Block> getCodec() { return CODEC; }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(IS_MILLING)) {
            return;
        }

        if (world.getBlockEntity(pos) instanceof MillstoneBlockEntity blockEntity) {
            ItemStack itemStack = blockEntity.getMillingItemStack();
            if (itemStack.isEmpty()) {
                return;
            }

            double xPos = pos.getX() + 0.5;
            double yPos = pos.getY() + 0.5;
            double zPos = pos.getZ() + 0.5;
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
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            if (world.getBlockEntity(pos) instanceof MillstoneBlockEntity blockEntity) {
                player.openHandledScreen(blockEntity);
            }
        }
        return ActionResult.SUCCESS;
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
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return AllBlockEntities.MILLSTONE.instantiate(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return LazyTickingBlockEntity.getTicker(world);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(IS_MILLING); }
}
