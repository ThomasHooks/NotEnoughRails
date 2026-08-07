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

import com.github.thomashooks.notenoughrails.NotEnoughRails;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Function;

public class AllBlocks {
    public static final Block COKE_BLOCK = registerBlock("coke_block",
            settings -> new Block(settings
                    .strength(5.0F, 6.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .mapColor(MapColor.GRAY)
                    .requiresTool()
            ));
    public static final Block COKE_OVEN = registerBlock("coke_oven",
            settings -> new CokeOvenBlock(settings
                    .strength(3.5F, 3.5F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .mapColor(MapColor.GRAY)
                    .requiresTool()
                    .luminance(Blocks.createLightLevelFromLitBlockState(13))
            ));

    //------------------------------------------------------------------------------------------------------------------

    //region Fire Bricks Blocks
    public static final Block FIRE_BRICKS = registerBlock("fire_bricks",
            settings -> new Block(getDefaultFireBricksSettings(settings)
            ));
    public static final Block FIRE_BRICKS_SLAB = registerBlock("fire_bricks_slab",
            settings -> new SlabBlock(getDefaultFireBricksSettings(settings)
            ));
    public static final Block FIRE_BRICKS_STAIRS = registerBlock("fire_bricks_stairs",
            settings -> new StairsBlock(AllBlocks.FIRE_BRICKS.getDefaultState(), getDefaultFireBricksSettings(settings)
            ));
    public static final Block FIRE_BRICKS_WALL = registerBlock("fire_bricks_wall",
            settings -> new WallBlock(getDefaultFireBricksSettings(settings)
            ));
    //endregion

    //------------------------------------------------------------------------------------------------------------------

    public static final Block FLAX_CROP = registerBlockWithoutItem("flax_crop",
            settings -> new FlaxCropBlock(settings
                    .mapColor(state -> state.get(FlaxCropBlock.AGE) >= 6 ? MapColor.CYAN : MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)
            ));

    //------------------------------------------------------------------------------------------------------------------

    //region Fluxstone Blocks
    public static final Block FLUXSTONE = registerBlock("fluxstone",
            settings -> new PillarBlock(getDefaultFluxstoneSettings(settings)
            ));
    public static final Block FLUXSTONE_POLISHED = registerBlock("fluxstone_polished",
            settings -> new PillarBlock(getDefaultFluxstoneSettings(settings)
            ));
    public static final Block FLUXSTONE_SMOOTH = registerBlock("fluxstone_smooth",
            settings -> new Block(getDefaultFluxstoneSettings(settings)
            ));
    public static final Block FLUXSTONE_SMOOTH_SLAB = registerBlock("fluxstone_smooth_slab",
            settings -> new SlabBlock(getDefaultFluxstoneSettings(settings)
            ));
    public static final Block FLUXSTONE_SMOOTH_STAIRS = registerBlock("fluxstone_smooth_stairs",
            settings -> new StairsBlock(AllBlocks.FLUXSTONE_SMOOTH.getDefaultState(), getDefaultFluxstoneSettings(settings)
            ));
    //endregion

    //------------------------------------------------------------------------------------------------------------------

    public static final Block IRON_PLATE_BLOCK = registerBlock("iron_plate_block",
            settings -> new PillarBlock(settings
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.IRON)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.IRON_GRAY)
                    .requiresTool()
            ));

    //------------------------------------------------------------------------------------------------------------------

    //region Linen Blocks
    public static final Block LINEN_BLOCK = registerBlock("linen_block",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.OFF_WHITE)
            ));
    public static final Block LINEN_BLOCK_BLACK = registerBlock("linen_block_black",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.BLACK)
            ));
    public static final Block LINEN_BLOCK_BLUE = registerBlock("linen_block_blue",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.BLUE)
            ));
    public static final Block LINEN_BLOCK_BROWN = registerBlock("linen_block_brown",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.BROWN)
            ));
    public static final Block LINEN_BLOCK_CYAN = registerBlock("linen_block_cyan",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.CYAN)
            ));
    public static final Block LINEN_BLOCK_GRAY = registerBlock("linen_block_gray",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.GRAY)
            ));
    public static final Block LINEN_BLOCK_GREEN = registerBlock("linen_block_green",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.GREEN)
            ));
    public static final Block LINEN_BLOCK_LIGHT_BLUE = registerBlock("linen_block_light_blue",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.LIGHT_BLUE)
            ));
    public static final Block LINEN_BLOCK_LIGHT_GRAY = registerBlock("linen_block_light_gray",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.LIGHT_GRAY)
            ));
    public static final Block LINEN_BLOCK_LIME = registerBlock("linen_block_lime",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.LIME)
            ));
    public static final Block LINEN_BLOCK_MAGENTA = registerBlock("linen_block_magenta",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.MAGENTA)
            ));
    public static final Block LINEN_BLOCK_ORANGE = registerBlock("linen_block_orange",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.ORANGE)
            ));
    public static final Block LINEN_BLOCK_PINK = registerBlock("linen_block_pink",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.PINK)
            ));
    public static final Block LINEN_BLOCK_PURPLE = registerBlock("linen_block_purple",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.PURPLE)
            ));
    public static final Block LINEN_BLOCK_RED = registerBlock("linen_block_red",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.RED)
            ));
    public static final Block LINEN_BLOCK_WHITE = registerBlock("linen_block_white",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.WHITE)
            ));
    public static final Block LINEN_BLOCK_YELLOW = registerBlock("linen_block_yellow",
            settings -> new LinenBlock(getDefaultLinenBlockSettings(settings)
                    .mapColor(MapColor.YELLOW)
            ));
    //endregion

    //------------------------------------------------------------------------------------------------------------------

    //region Steel Blocks
    public static final Block STEEL_BLOCK = registerBlock("steel_block",
            settings -> new Block(settings
                    .strength(10.0F, 12.0F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.BLACK)
                    .requiresTool()
            ));
    public static final Block STEEL_CHISELED_BLOCK = registerBlock("steel_chiseled_block",
            settings -> new Block(settings
                    .strength(10.0F, 12.0F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.BLACK)
                    .requiresTool()
            ));
    public static final Block STEEL_CUT_BLOCK = registerBlock("steel_cut_block",
            settings -> new Block(settings
                    .strength(10.0F, 12.0F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.BLACK)
                    .requiresTool()
            ));
    public static final Block STEEL_CUT_SLAB = registerBlock("steel_cut_slab",
            settings -> new SlabBlock(settings
                    .strength(10.0F, 12.0F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.BLACK)
                    .requiresTool()
            ));
    public static final Block STEEL_CUT_STAIRS = registerBlock("steel_cut_stairs",
            settings -> new StairsBlock(AllBlocks.STEEL_BLOCK.getDefaultState(), settings
                    .strength(10.0F, 12.0F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.BLACK)
                    .requiresTool()
            ));
    public static final Block STEEL_DOOR = registerBlock("steel_door",
            settings -> new DoorBlock(BlockSetType.IRON, settings
                    .strength(10.0F, 12.0F)
                    .mapColor(STEEL_BLOCK.getDefaultMapColor())
                    .requiresTool()
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
            ));
    public static final Block STEEL_GRATE = registerBlock("steel_grate",
            settings -> new GrateBlock(settings
                    .strength(6.0F, 12.0F)
                    .sounds(BlockSoundGroup.COPPER_GRATE)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.BLACK)
                    .nonOpaque()
                    .requiresTool()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
            ));
    public static final Block STEEL_PLATE_BLOCK = registerBlock("steel_plate_block",
            settings -> new PillarBlock(settings
                    .strength(10.0F, 12.0F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.BLACK)
                    .requiresTool()
            ));
    public static final Block STEEL_TRAPDOOR = registerBlock("steel_trapdoor",
            settings -> new TrapdoorBlock(BlockSetType.IRON, settings
                    .strength(6.0F, 12.0F)
                    .mapColor(STEEL_BLOCK.getDefaultMapColor())
                    .requiresTool()
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
            ));
    //endregion

    //------------------------------------------------------------------------------------------------------------------

    public static final Block VERMILION_BLOCK = registerBlock("vermilion_block",
            settings -> new RedstoneBlock(settings
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .mapColor(MapColor.BRIGHT_RED)
                    .requiresTool()
                    .luminance(state -> 7)
                    .solidBlock(Blocks::never)
            ));
    public static final Block WOODEN_FRAME = registerBlock("wooden_frame",
            settings -> new Block(settings
                    .strength(2.0F, 3.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .mapColor(MapColor.OAK_TAN)
                    .burnable()
            ));

    //------------------------------------------------------------------------------------------------------------------

    //region Iron Rails
    public static final Block BRAKING_RAIL = registerBlock("braking_rail",
            settings -> new BrakingRailBlock(settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.METAL)
                    .noCollision()
            ));
    public static final Block BUFFER_STOP_RAIL = registerBlock("buffer_stop_rail",
            settings -> new BufferStopRailBlock(settings
                    .strength(1.05F)
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
                    .solidBlock(Blocks::always)
            ));
    public static final Block CHECK_RAIL = registerBlock("check_rail",
            settings -> new CheckRailBlock(settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.METAL)
                    .noCollision()
            ));
    public static final Block CHIME_RAIL = registerBlock("chime_rail",
            settings -> new ChimeRailBlock(settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.METAL)
                    .noCollision()
            ));
    public static final Block CROSSOVER_RAIL = registerBlock("crossover_rail",
            settings -> new CrossoverRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.DEFAULT_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.METAL)
                    .noCollision()
            ));
    public static final Block LOCKING_RAIL = registerBlock("locking_rail",
            settings -> new LockingRailBlock(settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.METAL)
                    .noCollision()
            ));
    //endregion

    //------------------------------------------------------------------------------------------------------------------

    //region Copper Rails
    public static final Block COPPER_ACTIVATOR_RAIL = registerBlock("copper_activator_rail",
            settings -> new OxidizableActivatorRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.UNAFFECTED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_ACTIVATOR_RAIL_EXPOSED = registerBlock("copper_activator_rail_exposed",
            settings -> new OxidizableActivatorRailBlock(Oxidizable.OxidationLevel.EXPOSED, ExtendedRailBehavior.EXPOSED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_ACTIVATOR_RAIL_WEATHERED = registerBlock("copper_activator_rail_weathered",
            settings -> new OxidizableActivatorRailBlock(Oxidizable.OxidationLevel.WEATHERED, ExtendedRailBehavior.WEATHERED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_ACTIVATOR_RAIL_OXIDIZED = registerBlock("copper_activator_rail_oxidized",
            settings -> new OxidizableActivatorRailBlock(Oxidizable.OxidationLevel.OXIDIZED, ExtendedRailBehavior.OXIDIZED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_ACTIVATOR_RAIL_WAXED = registerBlock("waxed_copper_activator_rail",
            settings -> new AdjustableActivatorRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.UNAFFECTED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_ACTIVATOR_RAIL_EXPOSED_WAXED = registerBlock("waxed_copper_activator_rail_exposed",
            settings -> new AdjustableActivatorRailBlock(Oxidizable.OxidationLevel.EXPOSED, ExtendedRailBehavior.EXPOSED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_ACTIVATOR_RAIL_WEATHERED_WAXED = registerBlock("waxed_copper_activator_rail_weathered",
            settings -> new AdjustableActivatorRailBlock(Oxidizable.OxidationLevel.WEATHERED, ExtendedRailBehavior.WEATHERED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_ACTIVATOR_RAIL_OXIDIZED_WAXED = registerBlock("waxed_copper_activator_rail_oxidized",
            settings -> new AdjustableActivatorRailBlock(Oxidizable.OxidationLevel.OXIDIZED, ExtendedRailBehavior.OXIDIZED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));

    public static final Block COPPER_BUFFER_STOP_RAIL = registerBlock("copper_buffer_stop_rail",
            settings -> new OxidizableBufferStopRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .nonOpaque()
                    .solidBlock(Blocks::always)
            ));
    public static final Block COPPER_BUFFER_STOP_RAIL_EXPOSED = registerBlock("copper_buffer_stop_rail_exposed",
            settings -> new OxidizableBufferStopRailBlock(Oxidizable.OxidationLevel.EXPOSED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .nonOpaque()
                    .solidBlock(Blocks::always)
            ));
    public static final Block COPPER_BUFFER_STOP_RAIL_WEATHERED = registerBlock("copper_buffer_stop_rail_weathered",
            settings -> new OxidizableBufferStopRailBlock(Oxidizable.OxidationLevel.WEATHERED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .nonOpaque()
                    .solidBlock(Blocks::always)
            ));
    public static final Block COPPER_BUFFER_STOP_RAIL_OXIDIZED = registerBlock("copper_buffer_stop_rail_oxidized",
            settings -> new OxidizableBufferStopRailBlock(Oxidizable.OxidationLevel.OXIDIZED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .nonOpaque()
                    .solidBlock(Blocks::always)
            ));
    public static final Block COPPER_BUFFER_STOP_RAIL_WAXED = registerBlock("waxed_copper_buffer_stop_rail",
            settings -> new BufferStopRailBlock(settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .nonOpaque()
                    .solidBlock(Blocks::always)
            ));
    public static final Block COPPER_BUFFER_STOP_RAIL_EXPOSED_WAXED = registerBlock("waxed_copper_buffer_stop_rail_exposed",
            settings -> new BufferStopRailBlock(settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .nonOpaque()
                    .solidBlock(Blocks::always)
            ));
    public static final Block COPPER_BUFFER_STOP_RAIL_WEATHERED_WAXED = registerBlock("waxed_copper_buffer_stop_rail_weathered",
            settings -> new BufferStopRailBlock(settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .nonOpaque()
                    .solidBlock(Blocks::always)
            ));
    public static final Block COPPER_BUFFER_STOP_RAIL_OXIDIZED_WAXED = registerBlock("waxed_copper_buffer_stop_rail_oxidized",
            settings -> new BufferStopRailBlock(settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .nonOpaque()
                    .solidBlock(Blocks::always)
            ));

    public static final Block COPPER_CROSSOVER_RAIL = registerBlock("copper_crossover_rail",
            settings -> new OxidizableCrossoverRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.UNAFFECTED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_CROSSOVER_RAIL_EXPOSED = registerBlock("copper_crossover_rail_exposed",
            settings -> new OxidizableCrossoverRailBlock(Oxidizable.OxidationLevel.EXPOSED, ExtendedRailBehavior.EXPOSED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_CROSSOVER_RAIL_WEATHERED = registerBlock("copper_crossover_rail_weathered",
            settings -> new OxidizableCrossoverRailBlock(Oxidizable.OxidationLevel.WEATHERED, ExtendedRailBehavior.WEATHERED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_CROSSOVER_RAIL_OXIDIZED = registerBlock("copper_crossover_rail_oxidized",
            settings -> new OxidizableCrossoverRailBlock(Oxidizable.OxidationLevel.OXIDIZED, ExtendedRailBehavior.OXIDIZED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_CROSSOVER_RAIL_WAXED = registerBlock("waxed_copper_crossover_rail",
            settings -> new CrossoverRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.UNAFFECTED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_CROSSOVER_RAIL_EXPOSED_WAXED = registerBlock("waxed_copper_crossover_rail_exposed",
            settings -> new CrossoverRailBlock(Oxidizable.OxidationLevel.EXPOSED, ExtendedRailBehavior.EXPOSED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_CROSSOVER_RAIL_WEATHERED_WAXED = registerBlock("waxed_copper_crossover_rail_weathered",
            settings -> new CrossoverRailBlock(Oxidizable.OxidationLevel.WEATHERED, ExtendedRailBehavior.WEATHERED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_CROSSOVER_RAIL_OXIDIZED_WAXED = registerBlock("waxed_copper_crossover_rail_oxidized",
            settings -> new CrossoverRailBlock(Oxidizable.OxidationLevel.OXIDIZED, ExtendedRailBehavior.OXIDIZED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));

    public static final Block COPPER_DETECTOR_RAIL = registerBlock("copper_detector_rail",
            settings -> new OxidizableDetectorRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 4.0F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_DETECTOR_RAIL_EXPOSED = registerBlock("copper_detector_rail_exposed",
            settings -> new OxidizableDetectorRailBlock(Oxidizable.OxidationLevel.EXPOSED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 2.0F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_DETECTOR_RAIL_WEATHERED = registerBlock("copper_detector_rail_weathered",
            settings -> new OxidizableDetectorRailBlock(Oxidizable.OxidationLevel.WEATHERED, ExtendedRailBehavior.DEFAULT_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_DETECTOR_RAIL_OXIDIZED = registerBlock("copper_detector_rail_oxidized",
            settings -> new OxidizableDetectorRailBlock(Oxidizable.OxidationLevel.OXIDIZED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 0.5F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_DETECTOR_RAIL_WAXED = registerBlock("waxed_copper_detector_rail",
            settings -> new AdjustableDetectorRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 4.0F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_DETECTOR_RAIL_EXPOSED_WAXED = registerBlock("waxed_copper_detector_rail_exposed",
            settings -> new AdjustableDetectorRailBlock(Oxidizable.OxidationLevel.EXPOSED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 2.0F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_DETECTOR_RAIL_WEATHERED_WAXED = registerBlock("waxed_copper_detector_rail_weathered",
            settings -> new AdjustableDetectorRailBlock(Oxidizable.OxidationLevel.WEATHERED, ExtendedRailBehavior.DEFAULT_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_DETECTOR_RAIL_OXIDIZED_WAXED = registerBlock("waxed_copper_detector_rail_oxidized",
            settings -> new AdjustableDetectorRailBlock(Oxidizable.OxidationLevel.OXIDIZED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 0.5F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));

    public static final Block COPPER_POWERED_RAIL = registerBlock("copper_powered_rail",
            settings -> new OxidizablePoweredRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 4.0F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_POWERED_RAIL_EXPOSED = registerBlock("copper_powered_rail_exposed",
            settings -> new OxidizablePoweredRailBlock(Oxidizable.OxidationLevel.EXPOSED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 2.0F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_POWERED_RAIL_WEATHERED = registerBlock("copper_powered_rail_weathered",
            settings -> new OxidizablePoweredRailBlock(Oxidizable.OxidationLevel.WEATHERED, ExtendedRailBehavior.DEFAULT_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_POWERED_RAIL_OXIDIZED = registerBlock("copper_powered_rail_oxidized",
            settings -> new OxidizablePoweredRailBlock(Oxidizable.OxidationLevel.OXIDIZED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 0.5F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_POWERED_RAIL_WAXED = registerBlock("waxed_copper_powered_rail",
            settings -> new AdjustablePoweredRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 4.0F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_POWERED_RAIL_EXPOSED_WAXED = registerBlock("waxed_copper_powered_rail_exposed",
            settings -> new AdjustablePoweredRailBlock(Oxidizable.OxidationLevel.EXPOSED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 2.0F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_POWERED_RAIL_WEATHERED_WAXED = registerBlock("waxed_copper_powered_rail_weathered",
            settings -> new AdjustablePoweredRailBlock(Oxidizable.OxidationLevel.WEATHERED, ExtendedRailBehavior.DEFAULT_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_POWERED_RAIL_OXIDIZED_WAXED = registerBlock("waxed_copper_powered_rail_oxidized",
            settings -> new AdjustablePoweredRailBlock(Oxidizable.OxidationLevel.OXIDIZED, ExtendedRailBehavior.DEFAULT_MAX_SPEED * 0.5F, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    
    public static final Block COPPER_RAIL = registerBlock("copper_rail",
            settings -> new OxidizableRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.UNAFFECTED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_RAIL_EXPOSED = registerBlock("copper_rail_exposed",
            settings -> new OxidizableRailBlock(Oxidizable.OxidationLevel.EXPOSED, ExtendedRailBehavior.EXPOSED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_RAIL_WEATHERED = registerBlock("copper_rail_weathered",
            settings -> new OxidizableRailBlock(Oxidizable.OxidationLevel.WEATHERED, ExtendedRailBehavior.WEATHERED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_RAIL_OXIDIZED = registerBlock("copper_rail_oxidized",
            settings -> new OxidizableRailBlock(Oxidizable.OxidationLevel.OXIDIZED, ExtendedRailBehavior.OXIDIZED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_RAIL_WAXED = registerBlock("waxed_copper_rail",
            settings -> new AdjustableRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.UNAFFECTED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_RAIL_EXPOSED_WAXED = registerBlock("waxed_copper_rail_exposed",
            settings -> new AdjustableRailBlock(Oxidizable.OxidationLevel.EXPOSED, ExtendedRailBehavior.EXPOSED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_RAIL_WEATHERED_WAXED = registerBlock("waxed_copper_rail_weathered",
            settings -> new AdjustableRailBlock(Oxidizable.OxidationLevel.WEATHERED, ExtendedRailBehavior.WEATHERED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    public static final Block COPPER_RAIL_OXIDIZED_WAXED = registerBlock("waxed_copper_rail_oxidized",
            settings -> new AdjustableRailBlock(Oxidizable.OxidationLevel.OXIDIZED, ExtendedRailBehavior.OXIDIZED_COPPER_MAX_SPEED, settings
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.COPPER)
                    .noCollision()
            ));
    //endregion

    //------------------------------------------------------------------------------------------------------------------

    //region Steel Rails
    public static final Block STEEL_ACTIVATOR_RAIL = registerBlock("steel_activator_rail",
            settings -> new AdjustableActivatorRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.STEEL_MAX_SPEED, settings
                    .strength(1.05F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .noCollision()
            ));
    public static final Block STEEL_BUFFER_STOP_RAIL = registerBlock("steel_buffer_stop_rail",
            settings -> new BufferStopRailBlock(settings
                    .strength(1.4F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .nonOpaque()
                    .solidBlock(Blocks::always)
            ));
    public static final Block STEEL_CROSSOVER_RAIL = registerBlock("steel_crossover_rail",
            settings -> new CrossoverRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.STEEL_MAX_SPEED, settings
                    .strength(1.05F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .noCollision()
            ));
    public static final Block STEEL_DETECTOR_RAIL = registerBlock("steel_detector_rail",
            settings -> new AdjustableDetectorRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.STEEL_MAX_SPEED, settings
                    .strength(1.05F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .noCollision()
            ));
    public static final Block STEEL_POWERED_RAIL = registerBlock("steel_powered_rail",
            settings -> new AdjustablePoweredRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.STEEL_MAX_SPEED, settings
                    .strength(1.05F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .noCollision()
            ));
    public static final Block STEEL_RAIL = registerBlock("steel_rail",
            settings -> new AdjustableRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, ExtendedRailBehavior.STEEL_MAX_SPEED, settings
                    .strength(1.05F)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .noCollision()
            ));
    //endregion

    //------------------------------------------------------------------------------------------------------------------

    //region Register Methods
    public static void registerAll() {
        NotEnoughRails.LOGGER.info("Registering all Blocks");

        //Building Blocks
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            //Wood Blocks
            //Order: full block -> stairs -> slab -> wall -> fence -> fence gate -> door -> trapdoor -> pressure plate -> button
            entries.add(AllBlocks.WOODEN_FRAME);

            //Stone Blocks
            //Order: full block -> stairs -> slab -> wall -> fence -> fence gate -> door -> trapdoor -> pressure plate -> button
            entries.add(AllBlocks.FLUXSTONE_POLISHED);
            entries.add(AllBlocks.FLUXSTONE_SMOOTH);
            entries.add(AllBlocks.FLUXSTONE_SMOOTH_STAIRS);
            entries.add(AllBlocks.FLUXSTONE_SMOOTH_SLAB);
            entries.add(AllBlocks.FIRE_BRICKS);
            entries.add(AllBlocks.FIRE_BRICKS_STAIRS);
            entries.add(AllBlocks.FIRE_BRICKS_SLAB);
            entries.add(AllBlocks.FIRE_BRICKS_WALL);

            //Fuel Blocks
            entries.add(AllBlocks.COKE_BLOCK);

            //Metal Blocks
            //Order: full block -> chiseled -> grate -> cut -> stairs -> slab -> bars -> door -> trapdoor -> pressure plate
            entries.add(AllBlocks.IRON_PLATE_BLOCK);
            entries.add(AllBlocks.STEEL_BLOCK);
            entries.add(AllBlocks.STEEL_PLATE_BLOCK);
            entries.add(AllBlocks.STEEL_CHISELED_BLOCK);
            entries.add(AllBlocks.STEEL_GRATE);
            entries.add(AllBlocks.STEEL_CUT_BLOCK);
            entries.add(AllBlocks.STEEL_CUT_STAIRS);
            entries.add(AllBlocks.STEEL_CUT_SLAB);
            entries.add(AllBlocks.STEEL_DOOR);
            entries.add(AllBlocks.STEEL_TRAPDOOR);
            entries.add(AllBlocks.VERMILION_BLOCK);
        });
        //Colored Blocks
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(entries -> {
            //Cloth Blocks
            entries.add(AllBlocks.LINEN_BLOCK);
            entries.add(AllBlocks.LINEN_BLOCK_WHITE);
            entries.add(AllBlocks.LINEN_BLOCK_LIGHT_GRAY);
            entries.add(AllBlocks.LINEN_BLOCK_GRAY);
            entries.add(AllBlocks.LINEN_BLOCK_BLACK);
            entries.add(AllBlocks.LINEN_BLOCK_BROWN);
            entries.add(AllBlocks.LINEN_BLOCK_RED);
            entries.add(AllBlocks.LINEN_BLOCK_ORANGE);
            entries.add(AllBlocks.LINEN_BLOCK_YELLOW);
            entries.add(AllBlocks.LINEN_BLOCK_LIME);
            entries.add(AllBlocks.LINEN_BLOCK_GREEN);
            entries.add(AllBlocks.LINEN_BLOCK_CYAN);
            entries.add(AllBlocks.LINEN_BLOCK_LIGHT_BLUE);
            entries.add(AllBlocks.LINEN_BLOCK_BLUE);
            entries.add(AllBlocks.LINEN_BLOCK_PURPLE);
            entries.add(AllBlocks.LINEN_BLOCK_MAGENTA);
            entries.add(AllBlocks.LINEN_BLOCK_PINK);
        });
        //Functional Blocks
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.addAfter(Blocks.BLAST_FURNACE, AllBlocks.COKE_OVEN);
        });
        //Natural Blocks
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            //Natural Stone Blocks
            entries.add(AllBlocks.FLUXSTONE);
        });
        //Redstone Blocks
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            //Order: standard -> crossover -> buffer stop -> powered -> braking -> check -> detector -> chime -> activator -> locking
            //region Iron Rails
            entries.addAfter(Blocks.RAIL, AllBlocks.CROSSOVER_RAIL);
            entries.addAfter(AllBlocks.CROSSOVER_RAIL, AllBlocks.BUFFER_STOP_RAIL);
            entries.addAfter(Blocks.POWERED_RAIL, AllBlocks.BRAKING_RAIL);
            entries.addAfter(AllBlocks.BRAKING_RAIL, AllBlocks.CHECK_RAIL);
            entries.addAfter(Blocks.DETECTOR_RAIL, AllBlocks.CHIME_RAIL);
            entries.addAfter(Blocks.ACTIVATOR_RAIL, AllBlocks.LOCKING_RAIL);
            //endregion

            //region Copper Rail
            entries.addAfter(AllBlocks.LOCKING_RAIL, AllBlocks.COPPER_RAIL);
            entries.addAfter(AllBlocks.COPPER_RAIL, AllBlocks.COPPER_CROSSOVER_RAIL);
            entries.addAfter(AllBlocks.COPPER_CROSSOVER_RAIL, AllBlocks.COPPER_BUFFER_STOP_RAIL);
            entries.addAfter(AllBlocks.COPPER_BUFFER_STOP_RAIL, AllBlocks.COPPER_POWERED_RAIL);
            entries.addAfter(AllBlocks.COPPER_POWERED_RAIL, AllBlocks.COPPER_DETECTOR_RAIL);
            entries.addAfter(AllBlocks.COPPER_DETECTOR_RAIL, AllBlocks.COPPER_ACTIVATOR_RAIL);
            //endregion

            //region Steel Rail
            entries.addAfter(AllBlocks.COPPER_ACTIVATOR_RAIL, AllBlocks.STEEL_RAIL);
            entries.addAfter(AllBlocks.STEEL_RAIL, AllBlocks.STEEL_CROSSOVER_RAIL);
            entries.addAfter(AllBlocks.STEEL_CROSSOVER_RAIL, AllBlocks.STEEL_BUFFER_STOP_RAIL);
            entries.addAfter(AllBlocks.STEEL_BUFFER_STOP_RAIL, AllBlocks.STEEL_POWERED_RAIL);
            entries.addAfter(AllBlocks.STEEL_POWERED_RAIL, AllBlocks.STEEL_DETECTOR_RAIL);
            entries.addAfter(AllBlocks.STEEL_DETECTOR_RAIL, AllBlocks.STEEL_ACTIVATOR_RAIL);
            //endregion
        });
        //Tool Blocks
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            //Order: standard -> crossover -> buffer stop -> powered -> braking -> check -> detector -> chime -> activator -> locking
            //region Iron Rails
            entries.addAfter(Blocks.RAIL, AllBlocks.CROSSOVER_RAIL);
            entries.addAfter(AllBlocks.CROSSOVER_RAIL, AllBlocks.BUFFER_STOP_RAIL);
            entries.addAfter(Blocks.POWERED_RAIL, AllBlocks.BRAKING_RAIL);
            entries.addAfter(AllBlocks.BRAKING_RAIL, AllBlocks.CHECK_RAIL);
            entries.addAfter(Blocks.DETECTOR_RAIL, AllBlocks.CHIME_RAIL);
            entries.addAfter(Blocks.ACTIVATOR_RAIL, AllBlocks.LOCKING_RAIL);
            //endregion

            //region Copper Rail
            entries.addAfter(AllBlocks.LOCKING_RAIL, AllBlocks.COPPER_RAIL);
            entries.addAfter(AllBlocks.COPPER_RAIL, AllBlocks.COPPER_CROSSOVER_RAIL);
            entries.addAfter(AllBlocks.COPPER_CROSSOVER_RAIL, AllBlocks.COPPER_BUFFER_STOP_RAIL);
            entries.addAfter(AllBlocks.COPPER_BUFFER_STOP_RAIL, AllBlocks.COPPER_POWERED_RAIL);
            entries.addAfter(AllBlocks.COPPER_POWERED_RAIL, AllBlocks.COPPER_DETECTOR_RAIL);
            entries.addAfter(AllBlocks.COPPER_DETECTOR_RAIL, AllBlocks.COPPER_ACTIVATOR_RAIL);
            //endregion

            //region Steel Rail
            entries.addAfter(AllBlocks.COPPER_ACTIVATOR_RAIL, AllBlocks.STEEL_RAIL);
            entries.addAfter(AllBlocks.STEEL_RAIL, AllBlocks.STEEL_CROSSOVER_RAIL);
            entries.addAfter(AllBlocks.STEEL_CROSSOVER_RAIL, AllBlocks.STEEL_BUFFER_STOP_RAIL);
            entries.addAfter(AllBlocks.STEEL_BUFFER_STOP_RAIL, AllBlocks.STEEL_POWERED_RAIL);
            entries.addAfter(AllBlocks.STEEL_POWERED_RAIL, AllBlocks.STEEL_DETECTOR_RAIL);
            entries.addAfter(AllBlocks.STEEL_DETECTOR_RAIL, AllBlocks.STEEL_ACTIVATOR_RAIL);
            //endregion
        });
    }

    public static void registerAllOxidizableBlocks() {
        NotEnoughRails.LOGGER.info("Registering all oxidizable blocks");
        OxidizableBlocksRegistry.registerCopperBlockSet(new CopperBlockSet(
                AllBlocks.COPPER_ACTIVATOR_RAIL,
                AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED,
                AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED,
                AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED,
                AllBlocks.COPPER_ACTIVATOR_RAIL_WAXED,
                AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED_WAXED,
                AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED_WAXED,
                AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED_WAXED
        ));
        OxidizableBlocksRegistry.registerCopperBlockSet(new CopperBlockSet(
                AllBlocks.COPPER_BUFFER_STOP_RAIL,
                AllBlocks.COPPER_BUFFER_STOP_RAIL_EXPOSED,
                AllBlocks.COPPER_BUFFER_STOP_RAIL_WEATHERED,
                AllBlocks.COPPER_BUFFER_STOP_RAIL_OXIDIZED,
                AllBlocks.COPPER_BUFFER_STOP_RAIL_WAXED,
                AllBlocks.COPPER_BUFFER_STOP_RAIL_EXPOSED_WAXED,
                AllBlocks.COPPER_BUFFER_STOP_RAIL_WEATHERED_WAXED,
                AllBlocks.COPPER_BUFFER_STOP_RAIL_OXIDIZED_WAXED
        ));
        OxidizableBlocksRegistry.registerCopperBlockSet(new CopperBlockSet(
                AllBlocks.COPPER_CROSSOVER_RAIL,
                AllBlocks.COPPER_CROSSOVER_RAIL_EXPOSED,
                AllBlocks.COPPER_CROSSOVER_RAIL_WEATHERED,
                AllBlocks.COPPER_CROSSOVER_RAIL_OXIDIZED,
                AllBlocks.COPPER_CROSSOVER_RAIL_WAXED,
                AllBlocks.COPPER_CROSSOVER_RAIL_EXPOSED_WAXED,
                AllBlocks.COPPER_CROSSOVER_RAIL_WEATHERED_WAXED,
                AllBlocks.COPPER_CROSSOVER_RAIL_OXIDIZED_WAXED
        ));
        OxidizableBlocksRegistry.registerCopperBlockSet(new CopperBlockSet(
                AllBlocks.COPPER_DETECTOR_RAIL,
                AllBlocks.COPPER_DETECTOR_RAIL_EXPOSED,
                AllBlocks.COPPER_DETECTOR_RAIL_WEATHERED,
                AllBlocks.COPPER_DETECTOR_RAIL_OXIDIZED,
                AllBlocks.COPPER_DETECTOR_RAIL_WAXED,
                AllBlocks.COPPER_DETECTOR_RAIL_EXPOSED_WAXED,
                AllBlocks.COPPER_DETECTOR_RAIL_WEATHERED_WAXED,
                AllBlocks.COPPER_DETECTOR_RAIL_OXIDIZED_WAXED
        ));
        OxidizableBlocksRegistry.registerCopperBlockSet(new CopperBlockSet(
                AllBlocks.COPPER_POWERED_RAIL,
                AllBlocks.COPPER_POWERED_RAIL_EXPOSED,
                AllBlocks.COPPER_POWERED_RAIL_WEATHERED,
                AllBlocks.COPPER_POWERED_RAIL_OXIDIZED,
                AllBlocks.COPPER_POWERED_RAIL_WAXED,
                AllBlocks.COPPER_POWERED_RAIL_EXPOSED_WAXED,
                AllBlocks.COPPER_POWERED_RAIL_WEATHERED_WAXED,
                AllBlocks.COPPER_POWERED_RAIL_OXIDIZED_WAXED
        ));
        OxidizableBlocksRegistry.registerCopperBlockSet(new CopperBlockSet(
                AllBlocks.COPPER_RAIL,
                AllBlocks.COPPER_RAIL_EXPOSED,
                AllBlocks.COPPER_RAIL_WEATHERED,
                AllBlocks.COPPER_RAIL_OXIDIZED,
                AllBlocks.COPPER_RAIL_WAXED,
                AllBlocks.COPPER_RAIL_EXPOSED_WAXED,
                AllBlocks.COPPER_RAIL_WEATHERED_WAXED,
                AllBlocks.COPPER_RAIL_OXIDIZED_WAXED
        ));
    }

    private static Block registerBlockWithoutItem(String name, Function<AbstractBlock.Settings, Block> function) {
        return Registry.register(Registries.BLOCK, NotEnoughRails.identifier(name),
                function.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, NotEnoughRails.identifier(name)))));
    }

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> function) {
        Block block = Registry.register(Registries.BLOCK, NotEnoughRails.identifier(name),
                function.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, NotEnoughRails.identifier(name)))));
        registerBlockItem(name, block);
        return block;
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, NotEnoughRails.identifier(name),
                new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, NotEnoughRails.identifier(name)))));
    }
    //endregion

    //region Default Block Settings Methods
    private static AbstractBlock.Settings getDefaultFireBricksSettings(AbstractBlock.Settings settings) {
        return settings
                .strength(2.0F, 6.0F)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .mapColor(MapColor.TERRACOTTA_BROWN)
                .requiresTool();
    }

    private static AbstractBlock.Settings getDefaultFluxstoneSettings(AbstractBlock.Settings settings) {
        return settings
                .strength(1.5F, 5.0F)
                .sounds(BlockSoundGroup.DEEPSLATE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
                .requiresTool();
    }

    private static AbstractBlock.Settings getDefaultLinenBlockSettings(AbstractBlock.Settings settings) {
        return settings
                .strength(0.8F, 2.0F)
                .sounds(BlockSoundGroup.WOOL)
                .instrument(NoteBlockInstrument.FLUTE)
                .burnable();
    }
    //endregion
}
