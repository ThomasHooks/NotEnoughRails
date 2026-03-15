package com.github.thomashooks.notenoughrails.block;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public interface ExtendedRailBehavior {
    /**
     * @param state    - The Rail's current block state
     * @param world    - The current world
     * @param pos      - The Rails position in the world
     * @return True if the rail can make slopes
     */
    default boolean notEnoughRails$canMakeSlopes(BlockState state, World world, BlockPos pos) { return true; }

    /**
     * This can be used to trick minecarts into thinking the rail has a different shape
     * @param state    - The Rail's current block state
     * @param world    - The current world
     * @param pos      - The Rails position in the world
     * @param minecart - The minecart that is passing over the rail
     * @return Gets the rail's current direction
     */
    default RailShape notEnoughRails$getRailDirection(BlockState state, World world, BlockPos pos, @Nullable AbstractMinecartEntity minecart) {
        if (state.getBlock() instanceof AbstractRailBlock railBlock) {
            return state.get(railBlock.getShapeProperty());
        } else {
            throw new IllegalStateException("Block '" + state.getBlock().getTranslationKey() + "' at position (" + pos.toShortString() + ") is in an unknown state while getting rail direction!");
        }
    }

    /**
     * @param state    - The Rail's current block state
     * @param world    - The current world
     * @param pos      - The Rails position in the world
     * @param minecart - The minecart that is passing over the rail
     * @return Gets the maximum speed a minecart can have when passing over this rail
     */
    default float notEnoughRails$getMaxSpeed(BlockState state, World world, BlockPos pos, AbstractMinecartEntity minecart) {
        if (minecart instanceof FurnaceMinecartEntity) {
            return minecart.isInFluid() ? 0.15F : 0.2F;
        } else {
            return minecart.isInFluid() ? 0.2F : 0.4F;
        }
    }

    /**
     * Called by minecarts once per tick when they pass over this rail block
     * @param state    - The Rail's current block state
     * @param world    - The current world
     * @param pos      - The Rails position in the world
     * @param minecart - The minecart that is passing over the rail
     */
    default void notEnoughRails$onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity minecart) { }
}
