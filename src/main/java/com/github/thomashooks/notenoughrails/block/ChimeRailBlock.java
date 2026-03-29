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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.ScheduledTickView;
import org.jspecify.annotations.Nullable;

public class ChimeRailBlock extends DetectorRailBlock {
    public static final EnumProperty<NoteBlockInstrument> INSTRUMENT = Properties.INSTRUMENT;
    public static final IntProperty NOTE = Properties.NOTE;
    public static final float VOLUME = 4.0F;

    public ChimeRailBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(POWERED, false)
                .with(SHAPE, RailShape.NORTH_SOUTH)
                .with(WATERLOGGED, false)
                .with(INSTRUMENT, NoteBlockInstrument.HARP)
                .with(NOTE, 0)
        );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.setInstrument(ctx.getWorld(), ctx.getBlockPos(), super.getPlacementState(ctx));
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (state.get(POWERED)) {
            return (int)((double)state.get(NOTE) * (7.0D / 12.0D) + 1.0D);
        }
        return 0;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (state.get(POWERED) && direction == Direction.UP) {
            return (int)((double)state.get(NOTE) * (7.0D / 12.0D) + 1.0D);
        }
        return 0;
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return false;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        boolean isYAxis = direction.getAxis() == Direction.Axis.Y;
        return isYAxis ? this.setInstrument(world, pos, state) : state;
    }

    private BlockState setInstrument(WorldView world, BlockPos pos, BlockState state) {
        NoteBlockInstrument blockInstrument = world.getBlockState(pos.up()).getInstrument();
        if (blockInstrument.isNotBaseBlock()) {
            return state.with(INSTRUMENT, blockInstrument);
        } else {
            NoteBlockInstrument blockInstrumentBelow = world.getBlockState(pos.down()).getInstrument();
            return state.with(INSTRUMENT, blockInstrumentBelow.isNotBaseBlock() ? NoteBlockInstrument.HARP : blockInstrumentBelow);
        }
    }

    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        super.onStateReplaced(state, world, pos, moved);
        if (!moved && state.get(POWERED)) {
            //This causes it to play a note after the minecart moves off
            //This is because the event system is very temperamental
            this.playNote(null, state, world, pos);
        }
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return stack.isIn(ItemTags.NOTEBLOCK_TOP_INSTRUMENTS) && hit.getSide() == Direction.UP ?
                ActionResult.PASS :
                super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            state = state.cycle(NOTE);
            world.setBlockState(pos, state, 3);
            this.playNote(player, state, world, pos);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient()) {
            this.playNote(player, state, world, pos);
        }
    }

    private void playNote(@Nullable Entity entity, BlockState state, World world, BlockPos pos) {
        world.addSyncedBlockEvent(pos, world.getBlockState(pos).getBlock(), 0, 0);
        world.emitGameEvent(entity, GameEvent.NOTE_BLOCK_PLAY, pos);
    }

    @Override
    protected boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        NoteBlockInstrument blockInstrument = state.get(INSTRUMENT);
        float pitch;
        if (blockInstrument.canBePitched()) {
            int noteID = state.get(NOTE);
            pitch = NoteBlock.getNotePitch(noteID);
            world.addParticleClient(ParticleTypes.NOTE, (double)pos.getX() + 0.5, (double)pos.getY() + 1.2, (double)pos.getZ() + 0.5, (double)noteID / 24.0, 0.0, 0.0);
        } else {
            pitch = 1.0F;
        }

        RegistryEntry<SoundEvent> registryEntry;
        if (blockInstrument.hasCustomSound()) {
            Identifier identifier = this.getCustomSound(world, pos);
            if (identifier == null) {
                return false;
            }

            registryEntry = RegistryEntry.of(SoundEvent.of(identifier));
        } else {
            registryEntry = blockInstrument.getSound();
        }

        world.playSound(null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, registryEntry, SoundCategory.RECORDS, VOLUME, pitch, world.random.nextLong());
        return true;
    }

    private @Nullable Identifier getCustomSound(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos.up());
        if (blockEntity instanceof SkullBlockEntity skullBlockEntity) {
            return skullBlockEntity.getNoteBlockSound();
        } else {
            return null;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(INSTRUMENT, NOTE);
    }
}
