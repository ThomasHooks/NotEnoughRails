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
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class AllItems {
    public static final Item BOOSTER_ROD = registerItem("booster_rod", Item::new);
    public static final Item BOOSTER_ROD_COPPER = registerItem("booster_rod_copper", Item::new);
    public static final Item BOOSTER_ROD_CORITE = registerItem("booster_rod_corite", Item::new);
    public static final Item COKE = registerItem("coke", Item::new);
    public static final Item COPPER_PLATE = registerItem("copper_plate", Item::new);
    public static final Item COPPER_ROD = registerItem("copper_rod", Item::new);
    public static final Item CORITE_INGOT = registerItem("corite_ingot", Item::new);
    public static final Item CORITE_PLATE = registerItem("corite_plate", Item::new);
    public static final Item CORITE_ROD = registerItem("corite_rod", Item::new);
    public static final Item CRUSHED_COPPER_ORE = registerItem("crushed_copper_ore", Item::new);
    public static final Item CRUSHED_CORITE = registerItem("crushed_corite", Item::new);
    public static final Item CRUSHED_GOLD_ORE = registerItem("crushed_gold_ore", Item::new);
    public static final Item CRUSHED_IRON_ORE = registerItem("crushed_iron_ore", Item::new);
    public static final Item CRUSHED_VERMILION = registerItem("crushed_vermilion", Item::new);
    public static final Item FIRE_BRICK = registerItem("fire_brick", settings -> new Item(settings.fireproof()));
    public static final Item FLAX = registerItem("flax", Item::new);
    public static final Item FLAX_STRING = registerItem("flax_string", Item::new);
    public static final Item FLAXSEEDS = registerItem("flaxseed", createBlockItemWithUniqueName(AllBlocks.FLAX_CROP));
    public static final Item FLUX = registerItem("flux", Item::new);
    public static final Item GOLD_ROD = registerItem("gold_rod", Item::new);
    public static final Item IRON_PLATE = registerItem("iron_plate", Item::new);
    public static final Item IRON_ROD = registerItem("iron_rod", Item::new);
    public static final Item KAOLIN = registerItem("kaolin", settings -> new Item(settings.fireproof()));
    public static final Item LINEN = registerItem("linen", Item::new);
    public static final Item LINSEED_OIL = registerItem("linseed_oil",
            settings -> new Item(settings
                    .recipeRemainder(Items.GLASS_BOTTLE)
                    .food(AllFoodComponents.LINSEED_OIL, AllConsumableComponents.LINSEED_OIL)
                    .useRemainder(Items.GLASS_BOTTLE)
                    .maxCount(16)
            ));
    public static final Item RAILROAD_TIE = registerItem("railroad_tie", Item::new);
    public static final Item VERMILION_INGOT = registerItem("vermilion_ingot", Item::new);
    public static final Item VERMILION_ROD = registerItem("vermilion_rod", Item::new);

    public static void registerAll() {
        NotEnoughRails.LOGGER.info("Registering all Items for " + NotEnoughRails.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            //Fuel Items
            entries.addAfter(Items.CHARCOAL, AllItems.COKE);

            //Raw Dust Items
            entries.addAfter(AllItems.COKE, AllItems.FLUX);
            entries.addAfter(AllItems.FLUX, AllItems.KAOLIN);

            //Raw Ore

            //Raw Crushed Ore
            entries.addAfter(Items.RAW_GOLD, AllItems.CRUSHED_COPPER_ORE);
            entries.addAfter(AllItems.CRUSHED_COPPER_ORE, AllItems.CRUSHED_IRON_ORE);
            entries.addAfter(AllItems.CRUSHED_IRON_ORE, AllItems.CRUSHED_CORITE);
            entries.addAfter(AllItems.CRUSHED_CORITE, AllItems.CRUSHED_GOLD_ORE);
            entries.addAfter(AllItems.CRUSHED_GOLD_ORE, AllItems.CRUSHED_VERMILION);

            //Metal Ingots
            entries.addAfter(Items.IRON_INGOT, AllItems.CORITE_INGOT);
            entries.addAfter(Items.GOLD_INGOT, AllItems.VERMILION_INGOT);

            //Metal Plates
            entries.addAfter(Items.NETHERITE_INGOT, AllItems.COPPER_PLATE);
            entries.addAfter(AllItems.COPPER_PLATE, AllItems.IRON_PLATE);
            entries.addAfter(AllItems.IRON_PLATE, AllItems.CORITE_PLATE);

            //Metal Rods
            entries.addAfter(AllItems.CORITE_PLATE, AllItems.COPPER_ROD);
            entries.addAfter(AllItems.COPPER_ROD, AllItems.IRON_ROD);
            entries.addAfter(AllItems.IRON_ROD, AllItems.CORITE_ROD);
            entries.addAfter(AllItems.CORITE_ROD, AllItems.GOLD_ROD);
            entries.addAfter(AllItems.GOLD_ROD, AllItems.VERMILION_ROD);

            //Minecart Rails Crafting Items
            entries.addAfter(AllItems.VERMILION_ROD, BOOSTER_ROD_COPPER);
            entries.addAfter(AllItems.BOOSTER_ROD_COPPER, AllItems.BOOSTER_ROD);
            entries.addAfter(AllItems.BOOSTER_ROD, BOOSTER_ROD_CORITE);
            entries.addAfter(AllItems.BOOSTER_ROD_CORITE, RAILROAD_TIE);

            //Bricks
            entries.addAfter(Items.NETHER_BRICK, AllItems.FIRE_BRICK);

            //Crop Items
            entries.addAfter(Items.WHEAT, AllItems.FLAX);
            entries.addAfter(AllItems.FLAX, AllItems.FLAX_STRING);
            entries.addAfter(AllItems.FLAX_STRING, AllItems.LINEN);
            entries.addAfter(AllItems.LINEN, AllItems.LINSEED_OIL);

            //Misc Crafting Items
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            //Food Items
            entries.addAfter(Items.HONEY_BOTTLE, AllItems.LINSEED_OIL);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            //Crop Items
            entries.addAfter(Items.BEETROOT_SEEDS, AllItems.FLAXSEEDS);
        });
    }

    private static Item registerItem(@NotNull String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name)))));
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name),
                new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name)))));
    }

    private static Function<Item.Settings, Item> createBlockItemWithUniqueName(Block block) {
        return settings -> new BlockItem(block, settings.useItemPrefixedTranslationKey());
    }
}
