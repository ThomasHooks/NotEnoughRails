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
package com.github.thomashooks.notenoughrails.world.item;

import com.github.thomashooks.notenoughrails.NotEnoughRails;
import com.github.thomashooks.notenoughrails.world.block.AllBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AllItemGroups {
    public static final ItemGroup MAIN_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(NotEnoughRails.MOD_ID, "main_item_group"), FabricItemGroup.builder()
                    .icon(()-> new ItemStack(AllItems.KAOLIN))
                    .displayName(Text.translatable("itemgroup." + NotEnoughRails.MOD_ID + ".main_item_group"))
                    .entries(AllItemGroups::displayItems)
                    .build()
    );

    public static void registerAll() {
        NotEnoughRails.LOGGER.info("Registering all Item Groups for " + NotEnoughRails.MOD_ID);
    }

    private static void displayItems(ItemGroup.DisplayContext displayContext, ItemGroup.Entries entries) {
        //Wood Blocks
        //Order: full block -> stairs -> slab -> wall -> fence -> fence gate -> door -> trapdoor -> pressure plate -> button
        entries.add(AllBlocks.WOODEN_FRAME);

        //Stone Blocks
        //Order: full block -> stairs -> slab -> wall -> fence -> fence gate -> door -> trapdoor -> pressure plate -> button
        entries.add(AllBlocks.FLUXSTONE);
        entries.add(AllBlocks.FLUXSTONE_POLISHED);

        //Fuel Blocks

        //Metal Blocks
        //Order: full block -> chiseled -> grate -> cut -> stairs -> slab -> bars -> door -> trapdoor -> pressure plate
        entries.add(AllBlocks.CORITE_BLOCK);
        entries.add(AllBlocks.CORITE_PLATE_BLOCK);
        entries.add(AllBlocks.CORITE_CHISELED_BLOCK);
        entries.add(AllBlocks.CORITE_GRATE);
        entries.add(AllBlocks.CORITE_CUT_BLOCK);
        entries.add(AllBlocks.CORITE_CUT_STAIRS);
        entries.add(AllBlocks.CORITE_CUT_SLAB);
        entries.add(AllBlocks.CORITE_DOOR);
        entries.add(AllBlocks.IRON_PLATE_BLOCK);
        entries.add(AllBlocks.VERMILION_BLOCK);

        //Cloth Blocks

        //Storage Blocks

        //Ore Blocks

        //Machine Blocks - Power Transfer

        //Machine Blocks - Power Generators

        //Machine Blocks - Mills/Processors

        //Furnaces

        //Ladders/Scaffolding

        //Redstone Blocks

        //Item Transfer Blocks

        //Minecart Rails

        //Minecarts

        //Tools

        //Weapons

        //Armor

        //Food

        //Fuel

        //Raw Ore

        //Raw Crushed Ore
        entries.add(AllItems.CRUSHED_COPPER_ORE);
        entries.add(AllItems.CRUSHED_CORITE);
        entries.add(AllItems.CRUSHED_GOLD_ORE);
        entries.add(AllItems.CRUSHED_IRON_ORE);
        entries.add(AllItems.CRUSHED_VERMILION);

        //Raw Dust Items
        entries.add(AllItems.FLUX);
        entries.add(AllItems.KAOLIN);

        //Metal Ingots
        entries.add(AllItems.CORITE_INGOT);
        entries.add(AllItems.VERMILION_INGOT);

        //Metal Plates
        entries.add(AllItems.COPPER_PLATE);
        entries.add(AllItems.CORITE_PLATE);
        entries.add(AllItems.IRON_PLATE);

        //Metal Rods
        entries.add(AllItems.COPPER_ROD);
        entries.add(AllItems.CORITE_ROD);
        entries.add(AllItems.GOLD_ROD);
        entries.add(AllItems.IRON_ROD);
        entries.add(AllItems.VERMILION_ROD);

        //Bricks

        //Crop Items

        //Misc Crafting Items

    }
}
