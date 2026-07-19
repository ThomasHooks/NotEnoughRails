package com.github.thomashooks.notenoughrails.util;

import com.github.thomashooks.notenoughrails.NotEnoughRails;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class AllItemTags {
    public static final TagKey<Item> LINEN_BLOCKS = create("linen_blocks");

    public static TagKey<Item> create(String name) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(NotEnoughRails.MOD_ID, name));
    }
}
