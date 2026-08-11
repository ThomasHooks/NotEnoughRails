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
package com.github.thomashooks.notenoughrails.util;

import com.github.thomashooks.notenoughrails.NotEnoughRails;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class AllItemTags {
    public static final TagKey<Item> LINEN_BLOCKS = create("linen_blocks");

    //region Common Item Tags
    public static final TagKey<Item> COPPER_DUSTS = createCommon("dusts/copper");
    public static final TagKey<Item> COPPER_PLATES = createCommon("plates/copper");
    public static final TagKey<Item> COPPER_RODS = createCommon("rods/copper");
    public static final TagKey<Item> GOLD_DUSTS = createCommon("dusts/gold");
    public static final TagKey<Item> GOLD_RODS = createCommon("rods/gold");
    public static final TagKey<Item> IRON_DUSTS = createCommon("dusts/iron");
    public static final TagKey<Item> IRON_PLATES = createCommon("plates/iron");
    public static final TagKey<Item> IRON_RODS = createCommon("rods/iron");
    public static final TagKey<Item> STEEL_INGOTS = createCommon("ingots/steel");
    public static final TagKey<Item> STEEL_NUGGETS = createCommon("nuggets/steel");
    public static final TagKey<Item> STEEL_PLATES = createCommon("plates/steel");
    public static final TagKey<Item> STEEL_RODS = createCommon("rods/steel");
    //endregion

    public static TagKey<Item> create(String name) {
        return TagKey.of(RegistryKeys.ITEM, NotEnoughRails.identifier(name));
    }

    public static TagKey<Item> createCommon(String name) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of("c", name));
    }
}
