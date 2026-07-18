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
package com.github.thomashooks.notenoughrails.item;

import com.github.thomashooks.notenoughrails.NotEnoughRails;
import com.github.thomashooks.notenoughrails.block.AllBlocks;
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
                    .icon(()-> new ItemStack(AllBlocks.BUFFER_STOP_RAIL))
                    .displayName(Text.translatable("itemgroup." + NotEnoughRails.MOD_ID + ".main_item_group"))
                    .entries(AllItemGroups::displayItems)
                    .build()
    );

    public static void registerAll() {
        NotEnoughRails.LOGGER.info("Registering all Item Groups");
    }

    private static void displayItems(ItemGroup.DisplayContext displayContext, ItemGroup.Entries entries) {
        //Wood Blocks
        //Order: full block -> stairs -> slab -> wall -> fence -> fence gate -> door -> trapdoor -> pressure plate -> button
        entries.add(AllBlocks.WOODEN_FRAME);

        //Natural Stone Blocks
        entries.add(AllBlocks.FLUXSTONE);

        //Stone Blocks
        //Order: full block -> stairs -> slab -> wall -> fence -> fence gate -> door -> trapdoor -> pressure plate -> button
        entries.add(AllBlocks.FLUXSTONE_POLISHED);
        entries.add(AllBlocks.FLUXSTONE_SMOOTH);
        entries.add(AllBlocks.FLUXSTONE_SMOOTH_STAIRS);
        entries.add(AllBlocks.FLUXSTONE_SMOOTH_SLAB);

        //Fuel Blocks
        entries.add(AllBlocks.COKE_BLOCK);

        //Metal Blocks
        //Order: full block -> chiseled -> grate -> cut -> stairs -> slab -> bars -> door -> trapdoor -> pressure plate
        entries.add(AllBlocks.STEEL_BLOCK);
        entries.add(AllBlocks.STEEL_PLATE_BLOCK);
        entries.add(AllBlocks.STEEL_CHISELED_BLOCK);
        entries.add(AllBlocks.STEEL_GRATE);
        entries.add(AllBlocks.STEEL_CUT_BLOCK);
        entries.add(AllBlocks.STEEL_CUT_STAIRS);
        entries.add(AllBlocks.STEEL_CUT_SLAB);
        entries.add(AllBlocks.STEEL_DOOR);
        entries.add(AllBlocks.STEEL_TRAPDOOR);
        entries.add(AllBlocks.IRON_PLATE_BLOCK);
        entries.add(AllBlocks.VERMILION_BLOCK);

        //Cloth Blocks
        entries.add(AllBlocks.LINEN_BLOCK);

        //Storage Blocks

        //Ore Blocks

        //Machine Blocks - Power Transfer

        //Machine Blocks - Power Generators

        //Machine Blocks - Mills/Processors

        //Furnaces

        //Ladders/Scaffolding

        //Redstone Blocks

        //Item Transfer Blocks

        //region Minecart Rails
        //Order: standard -> crossover -> buffer stop -> powered -> braking -> check -> detector -> chime -> activator -> locking
        //region Iron Rails
        entries.add(AllBlocks.CROSSOVER_RAIL);
        entries.add(AllBlocks.BUFFER_STOP_RAIL);
        entries.add(AllBlocks.BRAKING_RAIL);
        entries.add(AllBlocks.CHECK_RAIL);
        entries.add(AllBlocks.CHIME_RAIL);
        //endregion

        //region Copper Rail
        entries.add(AllBlocks.COPPER_RAIL);
        entries.add(AllBlocks.COPPER_CROSSOVER_RAIL);
        entries.add(AllBlocks.COPPER_BUFFER_STOP_RAIL);
        entries.add(AllBlocks.COPPER_POWERED_RAIL);
        entries.add(AllBlocks.COPPER_DETECTOR_RAIL);
        entries.add(AllBlocks.COPPER_ACTIVATOR_RAIL);
        //endregion

        //region Copper Rail Exposed
        entries.add(AllBlocks.COPPER_RAIL_EXPOSED);
        entries.add(AllBlocks.COPPER_CROSSOVER_RAIL_EXPOSED);
        entries.add(AllBlocks.COPPER_BUFFER_STOP_RAIL_EXPOSED);
        entries.add(AllBlocks.COPPER_POWERED_RAIL_EXPOSED);
        entries.add(AllBlocks.COPPER_DETECTOR_RAIL_EXPOSED);
        entries.add(AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED);
        //endregion

        //region Copper Rail Weathered
        entries.add(AllBlocks.COPPER_RAIL_WEATHERED);
        entries.add(AllBlocks.COPPER_CROSSOVER_RAIL_WEATHERED);
        entries.add(AllBlocks.COPPER_BUFFER_STOP_RAIL_WEATHERED);
        entries.add(AllBlocks.COPPER_POWERED_RAIL_WEATHERED);
        entries.add(AllBlocks.COPPER_DETECTOR_RAIL_WEATHERED);
        entries.add(AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED);
        //endregion

        //region Copper Rail Oxidized
        entries.add(AllBlocks.COPPER_RAIL_OXIDIZED);
        entries.add(AllBlocks.COPPER_CROSSOVER_RAIL_OXIDIZED);
        entries.add(AllBlocks.COPPER_BUFFER_STOP_RAIL_OXIDIZED);
        entries.add(AllBlocks.COPPER_POWERED_RAIL_OXIDIZED);
        entries.add(AllBlocks.COPPER_DETECTOR_RAIL_OXIDIZED);
        entries.add(AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED);
        //endregion

        //region Waxed Copper Rail
        entries.add(AllBlocks.COPPER_RAIL_WAXED);
        entries.add(AllBlocks.COPPER_CROSSOVER_RAIL_WAXED);
        entries.add(AllBlocks.COPPER_BUFFER_STOP_RAIL_WAXED);
        entries.add(AllBlocks.COPPER_POWERED_RAIL_WAXED);
        entries.add(AllBlocks.COPPER_DETECTOR_RAIL_WAXED);
        entries.add(AllBlocks.COPPER_ACTIVATOR_RAIL_WAXED);
        //endregion

        //region Waxed Copper Rail Exposed
        entries.add(AllBlocks.COPPER_RAIL_EXPOSED_WAXED);
        entries.add(AllBlocks.COPPER_CROSSOVER_RAIL_EXPOSED_WAXED);
        entries.add(AllBlocks.COPPER_BUFFER_STOP_RAIL_EXPOSED_WAXED);
        entries.add(AllBlocks.COPPER_POWERED_RAIL_EXPOSED_WAXED);
        entries.add(AllBlocks.COPPER_DETECTOR_RAIL_EXPOSED_WAXED);
        entries.add(AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED_WAXED);
        //endregion

        //region Waxed Copper Rail Weathered
        entries.add(AllBlocks.COPPER_RAIL_WEATHERED_WAXED);
        entries.add(AllBlocks.COPPER_CROSSOVER_RAIL_WEATHERED_WAXED);
        entries.add(AllBlocks.COPPER_BUFFER_STOP_RAIL_WEATHERED_WAXED);
        entries.add(AllBlocks.COPPER_POWERED_RAIL_WEATHERED_WAXED);
        entries.add(AllBlocks.COPPER_DETECTOR_RAIL_WEATHERED_WAXED);
        entries.add(AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED_WAXED);
        //endregion

        //region Waxed Copper Rail Oxidized
        entries.add(AllBlocks.COPPER_RAIL_OXIDIZED_WAXED);
        entries.add(AllBlocks.COPPER_CROSSOVER_RAIL_OXIDIZED_WAXED);
        entries.add(AllBlocks.COPPER_BUFFER_STOP_RAIL_OXIDIZED_WAXED);
        entries.add(AllBlocks.COPPER_POWERED_RAIL_OXIDIZED_WAXED);
        entries.add(AllBlocks.COPPER_DETECTOR_RAIL_OXIDIZED_WAXED);
        entries.add(AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED_WAXED);
        //endregion

        //region Steel Rail
        entries.add(AllBlocks.STEEL_RAIL);
        entries.add(AllBlocks.STEEL_CROSSOVER_RAIL);
        entries.add(AllBlocks.STEEL_BUFFER_STOP_RAIL);
        entries.add(AllBlocks.STEEL_POWERED_RAIL);
        entries.add(AllBlocks.STEEL_DETECTOR_RAIL);
        entries.add(AllBlocks.STEEL_ACTIVATOR_RAIL);
        //endregion
        //endregion

        //Minecarts

        //Tools

        //Weapons

        //Armor

        //Fuel Items
        entries.add(AllItems.COKE);

        //Raw Dust Items
        entries.add(AllItems.FLUX);
        entries.add(AllItems.KAOLIN);

        //Raw Ore
        entries.add(AllItems.RAW_STEEL);

        //Crushed Ore
        entries.add(AllItems.CRUSHED_COPPER_ORE);
        entries.add(AllItems.CRUSHED_IRON_ORE);
        entries.add(AllItems.CRUSHED_GOLD_ORE);
        entries.add(AllItems.CRUSHED_VERMILION);

        //Metal Nuggets
        entries.add(AllItems.STEEL_NUGGET);

        //Metal Ingots
        entries.add(AllItems.STEEL_INGOT);
        entries.add(AllItems.VERMILION_INGOT);

        //Metal Plates
        entries.add(AllItems.COPPER_PLATE);
        entries.add(AllItems.IRON_PLATE);
        entries.add(AllItems.STEEL_PLATE);

        //Metal Rods
        entries.add(AllItems.COPPER_ROD);
        entries.add(AllItems.IRON_ROD);
        entries.add(AllItems.STEEL_ROD);
        entries.add(AllItems.GOLD_ROD);
        entries.add(AllItems.VERMILION_ROD);

        //Minecart Rails Crafting Items
        entries.add(AllItems.BOOSTER_ROD_COPPER);
        entries.add(AllItems.BOOSTER_ROD);
        entries.add(AllItems.BOOSTER_ROD_STEEL);
        entries.add(AllItems.RAILROAD_TIE);

        //Bricks
        entries.add(AllItems.FIRE_BRICK);

        //Crop Items
        entries.add(AllItems.FLAXSEEDS);
        entries.add(AllItems.FLAX);
        entries.add(AllItems.FLAX_STRING);
        entries.add(AllItems.LINEN);
        entries.add(AllItems.LINSEED_OIL);

        //Food

        //Misc Crafting Items
    }
}
