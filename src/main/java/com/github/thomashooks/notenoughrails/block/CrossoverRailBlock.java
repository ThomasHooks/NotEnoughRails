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
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class CrossoverRailBlock extends AbstractRailBlock {
    public static final MapCodec<CrossoverRailBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Oxidizable.OxidationLevel.CODEC.fieldOf("weathering_state").forGetter(CrossoverRailBlock::getDegradationLevel),
                    Codec.FLOAT.fieldOf("maxSpeed").forGetter(block -> block.maxSpeed),
                    createSettingsCodec()
            ).apply(instance, CrossoverRailBlock::new)
    );
    public static final EnumProperty<RailShape> SHAPE = AllProperties.FLAT_RAIL_SHAPE;
    private final Oxidizable.OxidationLevel oxidationLevel;
    protected final float maxSpeed;

    /**
     * @param maxSpeedIn - The maximum speed a minecart can move on this rail in blocks/second
     * @param settings   - The settings for this block
     */
    public CrossoverRailBlock(Oxidizable.OxidationLevel oxidationLevelIn, float maxSpeedIn, Settings settings) {
        super(true, settings);
        this.setDefaultState(this.getDefaultState()
                .with(SHAPE, RailShape.NORTH_SOUTH)
                .with(WATERLOGGED, false)
        );
        this.oxidationLevel = oxidationLevelIn;
        this.maxSpeed = maxSpeedIn;
    }

    @Override
    protected MapCodec<? extends AbstractRailBlock> getCodec() { return CODEC; }

    @Override
    public Property<RailShape> getShapeProperty() { return SHAPE; }

    public Oxidizable.OxidationLevel getDegradationLevel() { return this.oxidationLevel; }

    @Override
    public boolean notEnoughRails$isFlatRail(BlockState state, World world, BlockPos pos) { return true; }

    @Override
    public RailShape notEnoughRails$getRailDirection(BlockState state, World world, BlockPos pos, @Nullable AbstractMinecartEntity minecart) {
        if (minecart != null) {
            Direction direction = minecart.getMovementDirection();
            if (direction != null) {
                return switch (direction) {
                    case EAST, WEST -> RailShape.EAST_WEST;
                    default -> RailShape.NORTH_SOUTH;
                };
            }
        }
        return super.notEnoughRails$getRailDirection(state, world, pos, minecart);
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
