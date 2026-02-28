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
    public static final Block CORITE_GRATE = registerBlock("corite_grate",
            settings -> new GrateBlock(settings
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.COPPER_GRATE)
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
                    .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
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
            entries.add(AllBlocks.CORITE_BLOCK);
            entries.add(AllBlocks.CORITE_GRATE);
            entries.add(AllBlocks.FLUXSTONE);
            entries.add(AllBlocks.VERMILION_BLOCK);
            entries.add(AllBlocks.WOODEN_FRAME);
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
