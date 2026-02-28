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
package com.github.thomashooks.notenoughrails.world.block;

import com.github.thomashooks.notenoughrails.NotEnoughRails;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class AllBlocks {
    public static final Block CORITE_BLOCK = registerBlock("corite_block",
            settings -> new Block(settings
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.COPPER)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.ORANGE)
                    .requiresTool()
            ));
    public static final Block CORITE_CHISELED = registerBlock("corite_chiseled_block",
            settings -> new Block(settings
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.COPPER)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.ORANGE)
                    .requiresTool()
            ));
    public static final Block CORITE_CUT = registerBlock("corite_cut_block",
            settings -> new Block(settings
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.COPPER)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.ORANGE)
                    .requiresTool()
            ));
    public static final Block CORITE_GRATE = registerBlock("corite_grate",
            settings -> new GrateBlock(settings
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.COPPER_GRATE)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .mapColor(MapColor.ORANGE)
                    .nonOpaque()
                    .requiresTool()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
            ));
    public static final Block FLUXSTONE = registerBlock("fluxstone",
            settings -> new Block(settings
                    .strength(1.5F, 5.0F)
                    .sounds(BlockSoundGroup.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
                    .requiresTool()
            ));
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

    public static void registerAll() {
        NotEnoughRails.LOGGER.info("Registering all Blocks for " + NotEnoughRails.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            //Wood Blocks
            //Order: full block -> stairs -> slab -> wall -> fence -> fence gate -> door -> trapdoor -> pressure plate -> button
            entries.add(AllBlocks.WOODEN_FRAME);

            //Stone Blocks
            //Order: full block -> stairs -> slab -> wall -> fence -> fence gate -> door -> trapdoor -> pressure plate -> button
            entries.add(AllBlocks.FLUXSTONE);

            //Fuel Blocks

            //Metal Blocks
            //Order: full block -> chiseled -> grate -> cut -> stairs -> slab -> bars -> door -> trapdoor -> pressure plate
            entries.add(AllBlocks.CORITE_BLOCK);
            entries.add(AllBlocks.CORITE_CHISELED);
            entries.add(AllBlocks.CORITE_GRATE);
            entries.add(AllBlocks.CORITE_CUT);
            entries.add(AllBlocks.VERMILION_BLOCK);
        });
    }

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> function) {
        Block block = Registry.register(Registries.BLOCK, Identifier.of(NotEnoughRails.MOD_ID, name),
                function.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(NotEnoughRails.MOD_ID, name)))));
        registerBlockItem(name, block);
        return block;
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name),
                new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name)))));
    }
}
