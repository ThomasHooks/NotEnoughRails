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
package com.github.thomashooks.notenoughrails.data.loot.modifier;

import com.github.thomashooks.notenoughrails.world.item.AllItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.component.ComponentPredicateTypes;
import net.minecraft.predicate.component.ComponentsPredicate;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.EnchantmentsPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;

public class FlaxseedsLootTables {
    private static final Identifier GRASS_BLOCK = Identifier.of("minecraft", "blocks/short_grass");
    private static final Identifier BUSH_BLOCK = Identifier.of("minecraft", "blocks/bush");

    public static void modify() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
            RegistryWrapper.Impl<Enchantment> impl = registry.getOrThrow(RegistryKeys.ENCHANTMENT);
            if (source.isBuiltin() && GRASS_BLOCK.equals(key.getValue())) {
                LootPool.Builder lootPoolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .conditionally(SurvivesExplosionLootCondition.builder())
                        .conditionally(RandomChanceLootCondition.builder(0.0625F))
                        .with(ItemEntry.builder(AllItems.FLAXSEEDS)
                                .conditionally(InvertedLootCondition.builder(createMatchingShearsCondition(registry)))
                                .conditionally(InvertedLootCondition.builder(createMatchingSilkTouchCondition(registry)))
                        )
                        .apply(ApplyBonusLootFunction.uniformBonusCount(impl.getOrThrow(Enchantments.FORTUNE), 2).build());
                tableBuilder.pool(lootPoolBuilder.build());
            } else if (source.isBuiltin() && BUSH_BLOCK.equals(key.getValue())) {
                LootPool.Builder lootPoolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .conditionally(SurvivesExplosionLootCondition.builder())
                        .conditionally(RandomChanceLootCondition.builder(0.125F))
                        .with(ItemEntry.builder(AllItems.FLAXSEEDS)
                                .conditionally(InvertedLootCondition.builder(createMatchingShearsCondition(registry)))
                                .conditionally(InvertedLootCondition.builder(createMatchingSilkTouchCondition(registry)))
                        )
                        .apply(ApplyBonusLootFunction.uniformBonusCount(impl.getOrThrow(Enchantments.FORTUNE), 2).build());
                tableBuilder.pool(lootPoolBuilder.build());
            }
        });
    }

    protected static LootCondition.Builder createMatchingShearsCondition(RegistryWrapper.WrapperLookup registry) {
        return MatchToolLootCondition.builder(ItemPredicate.Builder.create().items(registry.getOrThrow(RegistryKeys.ITEM), Items.SHEARS));
    }

    protected static LootCondition.Builder createMatchingSilkTouchCondition(RegistryWrapper.WrapperLookup registry) {
        return MatchToolLootCondition.builder(ItemPredicate.Builder.create().components(ComponentsPredicate.Builder.create()
                .partial(ComponentPredicateTypes.ENCHANTMENTS,
                        EnchantmentsPredicate.enchantments(
                                List.of(new EnchantmentPredicate(registry.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), NumberRange.IntRange.atLeast(1)))
                        )).build())
        );
    }
}
