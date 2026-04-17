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

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class OxidizableRailBlock extends AbstractRailBlock implements Oxidizable {
    public static final MapCodec<OxidizableRailBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Oxidizable.OxidationLevel.CODEC.fieldOf("weathering_state").forGetter(Degradable::getDegradationLevel),
                    Codec.FLOAT.fieldOf("max_speed").forGetter(block -> block.maxSpeed),
                    createSettingsCodec()
            ).apply(instance, OxidizableRailBlock::new)
    );
    public static final EnumProperty<RailShape> SHAPE = Properties.RAIL_SHAPE;
    private final Oxidizable.OxidationLevel oxidationLevel;
    protected final float maxSpeed;

    public OxidizableRailBlock(Oxidizable.OxidationLevel oxidationLevelIn, float maxSpeedIn, Settings settings) {
        super(false, settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(SHAPE, RailShape.NORTH_SOUTH)
                .with(WATERLOGGED, false)
        );
        this.oxidationLevel = oxidationLevelIn;
        this.maxSpeed = maxSpeedIn;
    }

    @Override
    protected MapCodec<? extends AbstractRailBlock> getCodec() { return CODEC; }

    @Override
    protected void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor) {
        if (neighbor.getDefaultState().emitsRedstonePower() && this.getNeighborCount(world, pos) == 3) {
            this.updateBlockState(world, pos, state, false);
        }
    }

    private int getNeighborCount(World world, BlockPos pos) {
        int i = 0;
        for (Direction direction : Direction.Type.HORIZONTAL) {
            if (this.isVerticallyNearRail(world, pos.offset(direction))) {
                i++;
            }
        }
        return i;
    }

    private boolean isVerticallyNearRail(World world, BlockPos pos) {
        return AbstractRailBlock.isRail(world, pos) || AbstractRailBlock.isRail(world, pos.up()) || AbstractRailBlock.isRail(world, pos.down());
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.tickDegradation(state, world, pos, random);
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return Oxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }

    @Override
    public Property<RailShape> getShapeProperty() { return SHAPE; }

    @Override
    public OxidationLevel getDegradationLevel() { return this.oxidationLevel; }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        RailShape railShape = state.get(SHAPE);
        RailShape railShape2 = this.rotateShape(railShape, rotation);
        return state.with(SHAPE, railShape2);
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        RailShape railShape = state.get(SHAPE);
        RailShape railShape2 = this.mirrorShape(railShape, mirror);
        return state.with(SHAPE, railShape2);
    }

    @Override
    public float notEnoughRails$getMaxSpeed(BlockState state, BlockPos pos, AbstractMinecartEntity minecart) {
        return this.maxSpeed * (minecart.isTouchingWater() ? WATERLOGGED_MAX_SPEED_RATIO : 1.0F) / 20.0F;
    }

    @Override
    public double notEnoughRails$getSpeedRetention(BlockState state, BlockPos pos, AbstractMinecartEntity minecart) {
        return switch (this.getDegradationLevel()) {
            case UNAFFECTED -> super.notEnoughRails$getSpeedRetention(state, pos, minecart);
            case EXPOSED -> minecart.hasPassengers() ? 0.978 : 0.942;
            case WEATHERED -> minecart.hasPassengers() ? 0.96 : 0.923;
            case OXIDIZED -> minecart.hasPassengers() ? 0.941 : 0.905;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, WATERLOGGED);
    }
}
