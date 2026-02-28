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
            entries.add(COPPER_PLATE);
            entries.add(COPPER_ROD);
            entries.add(CORITE_INGOT);
            entries.add(CORITE_PLATE);
            entries.add(CORITE_ROD);
            entries.add(CRUSHED_COPPER_ORE);
            entries.add(CRUSHED_CORITE);
            entries.add(CRUSHED_GOLD_ORE);
            entries.add(CRUSHED_IRON_ORE);
            entries.add(CRUSHED_VERMILION);
            entries.add(FLUX);
            entries.add(GOLD_ROD);
            entries.add(IRON_PLATE);
            entries.add(IRON_ROD);
            entries.add(KAOLIN);
            entries.add(VERMILION_INGOT);
            entries.add(VERMILION_ROD);
        });
    }

    private static Item registerItem(@NotNull String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name)))));
    }

    private static Item registerItem(@NotNull String name, @NotNull Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name), item);
    }
}
