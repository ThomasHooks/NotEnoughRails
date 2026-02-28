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
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class AllItems {
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
    public static final Item FLUX = registerItem("flux", Item::new);
    public static final Item GOLD_ROD = registerItem("gold_rod", Item::new);
    public static final Item IRON_PLATE = registerItem("iron_plate", Item::new);
    public static final Item IRON_ROD = registerItem("iron_rod", Item::new);
    public static final Item KAOLIN = registerItem("kaolin", settings -> new Item(settings.fireproof()));
    public static final Item VERMILION_INGOT = registerItem("vermilion_ingot", Item::new);
    public static final Item VERMILION_ROD = registerItem("vermilion_rod", Item::new);

    public static void registerAll() {
        NotEnoughRails.LOGGER.info("Registering all Items for " + NotEnoughRails.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            //Raw Ore

            //Raw Crushed Ore
            entries.add(CRUSHED_COPPER_ORE);
            entries.add(CRUSHED_CORITE);
            entries.add(CRUSHED_GOLD_ORE);
            entries.add(CRUSHED_IRON_ORE);
            entries.add(CRUSHED_VERMILION);

            //Raw Dust Items
            entries.add(FLUX);
            entries.add(KAOLIN);

            //Metal Ingots
            entries.add(CORITE_INGOT);
            entries.add(VERMILION_INGOT);

            //Metal Plates
            entries.add(COPPER_PLATE);
            entries.add(CORITE_PLATE);
            entries.add(IRON_PLATE);

            //Metal Rods
            entries.add(COPPER_ROD);
            entries.add(CORITE_ROD);
            entries.add(GOLD_ROD);
            entries.add(IRON_ROD);
            entries.add(VERMILION_ROD);

            //Bricks

            //Crop Items

            //Misc Crafting Items

        });
    }

    private static Item registerItem(@NotNull String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name)))));
    }
}
