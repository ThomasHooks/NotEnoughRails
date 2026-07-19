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
package com.github.thomashooks.notenoughrails.data.recipes;

import com.github.thomashooks.notenoughrails.NotEnoughRails;
import com.github.thomashooks.notenoughrails.block.AllBlocks;
import com.github.thomashooks.notenoughrails.item.AllItems;
import com.github.thomashooks.notenoughrails.util.AllBlockTags;
import com.github.thomashooks.notenoughrails.util.AllItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends FabricRecipeProvider {
    public RecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected net.minecraft.data.recipe.@NotNull RecipeGenerator getRecipeGenerator(RegistryWrapper.@NotNull WrapperLookup wrapperLookup, @NotNull RecipeExporter recipeExporter) {
        return new net.minecraft.data.recipe.RecipeGenerator(wrapperLookup, recipeExporter) {
            @Override
            public void generate() {
                NotEnoughRails.LOGGER.info("Generating all items recipes");
                //--------------------------------------------------------------------------------------------------------------
                // region Item Recipes

                //Booster Rod
                createShaped(RecipeCategory.TRANSPORTATION, AllItems.BOOSTER_ROD, 3)
                        .input('i', AllItems.IRON_ROD)
                        .input('g', AllItems.GOLD_ROD)
                        .input('v', AllItems.VERMILION_ROD)
                        .pattern("igv")
                        .group(NotEnoughRails.MOD_ID + ":booster_rod")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Booster Rod Copper
                createShaped(RecipeCategory.TRANSPORTATION, AllItems.BOOSTER_ROD_COPPER, 3)
                        .input('i', AllItems.COPPER_ROD)
                        .input('g', AllItems.GOLD_ROD)
                        .input('v', AllItems.VERMILION_ROD)
                        .pattern("igv")
                        .group(NotEnoughRails.MOD_ID + ":booster_rod_copper")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Booster Rod Steel
                createShaped(RecipeCategory.TRANSPORTATION, AllItems.BOOSTER_ROD_STEEL, 3)
                        .input('c', AllItems.STEEL_ROD)
                        .input('g', AllItems.GOLD_ROD)
                        .input('v', AllItems.VERMILION_ROD)
                        .pattern("cgv")
                        .group(NotEnoughRails.MOD_ID + ":booster_rod_steel")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Coke & Coke Block
                offerReversibleCompactingRecipes(RecipeCategory.MISC, AllItems.COKE,RecipeCategory.MISC, AllBlocks.COKE_BLOCK);

                //Copper Ingot
                List<ItemConvertible> COPPER_INGOT_SMELTABLES = List.of(AllItems.CRUSHED_COPPER_ORE);
                offerSmelting(COPPER_INGOT_SMELTABLES, RecipeCategory.MISC, Items.COPPER_INGOT, 0.7F, 200, NotEnoughRails.MOD_ID + ":copper_ingot");
                offerBlasting(COPPER_INGOT_SMELTABLES, RecipeCategory.MISC, Items.COPPER_INGOT, 0.7F, 100, NotEnoughRails.MOD_ID + ":copper_ingot");

                //Raw Steel
                createShapeless(RecipeCategory.MISC, AllItems.RAW_STEEL, 1)
                        .input(Items.IRON_NUGGET, 3)
                        .input(AllItems.COKE, 1)
                        .input(AllItems.FLUX, 1)
                        .group(NotEnoughRails.MOD_ID + ":raw_steel")
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .offerTo(exporter);

                //Steel Nugget
                createShapeless(RecipeCategory.MISC, AllItems.STEEL_NUGGET, 9)
                        .input(AllItems.STEEL_INGOT, 1)
                        .group(NotEnoughRails.MOD_ID + ":steel_nugget")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter, ":steel_nugget_from_steel_ingot");

                //Steel Ingot
                createShapeless(RecipeCategory.MISC, AllItems.STEEL_INGOT, 1)
                        .input(AllItems.STEEL_NUGGET, 9)
                        .group(NotEnoughRails.MOD_ID + ":steel_ingot")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.MISC, AllItems.STEEL_INGOT, 9)
                        .input(AllBlocks.STEEL_BLOCK, 1)
                        .group(NotEnoughRails.MOD_ID + ":steel_ingot")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter, ":steel_ingot_from_steel_block");

                //Steel Plate
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_PLATE_BLOCK, 1)
                        .input('p', AllItems.STEEL_PLATE)
                        .pattern("pp")
                        .pattern("pp")
                        .group(NotEnoughRails.MOD_ID + ":steel_plate_block")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.MISC, AllItems.STEEL_PLATE, 4)
                        .input(AllBlocks.STEEL_PLATE_BLOCK, 1)
                        .group(NotEnoughRails.MOD_ID + ":steel_plate")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter, ":steel_plate_from_steel_plate_block");

                //Flax String
                createShaped(RecipeCategory.MISC, AllItems.FLAX_STRING, 3)
                        .input('f', AllItems.FLAX)
                        .pattern("f  ")
                        .pattern("ff ")
                        .group(NotEnoughRails.MOD_ID + ":flax_string")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);

                //Gold Ingot
                List<ItemConvertible> GOLD_INGOT_SMELTABLES = List.of(AllItems.CRUSHED_GOLD_ORE);
                offerSmelting(GOLD_INGOT_SMELTABLES, RecipeCategory.MISC, Items.GOLD_INGOT, 1.0F, 200, NotEnoughRails.MOD_ID + ":gold_ingot");
                offerBlasting(GOLD_INGOT_SMELTABLES, RecipeCategory.MISC, Items.GOLD_INGOT, 1.0F, 100, NotEnoughRails.MOD_ID + ":gold_ingot");

                //Iron Ingot
                List<ItemConvertible> IRON_INGOT_SMELTABLES = List.of(AllItems.CRUSHED_IRON_ORE);
                offerSmelting(IRON_INGOT_SMELTABLES, RecipeCategory.MISC, Items.IRON_INGOT, 0.7F, 200, NotEnoughRails.MOD_ID + ":iron_ingot");
                offerBlasting(IRON_INGOT_SMELTABLES, RecipeCategory.MISC, Items.IRON_INGOT, 0.7F, 100, NotEnoughRails.MOD_ID + ":iron_ingot");

                //Iron Plate
                offerReversibleCompactingRecipes(RecipeCategory.MISC, AllItems.IRON_PLATE,RecipeCategory.BUILDING_BLOCKS, AllBlocks.IRON_PLATE_BLOCK);

                //Crushed Vermilion
                createShapeless(RecipeCategory.MISC, AllItems.CRUSHED_VERMILION, 1)
                        .input(AllItems.CRUSHED_COPPER_ORE, 1)
                        .input(Items.REDSTONE, 3)
                        .input(AllItems.FLUX, 1)
                        .group(NotEnoughRails.MOD_ID + ":crushed_vermilion")
                        .criterion(hasItem(Items.RAW_COPPER), conditionsFromItem(Items.RAW_COPPER))
                        .offerTo(exporter);

                //Crushed Vermilion
                createShapeless(RecipeCategory.MISC, AllItems.CRUSHED_VERMILION, 1)
                        .input(Items.RAW_COPPER, 1)
                        .input(Items.REDSTONE, 3)
                        .input(AllItems.FLUX, 1)
                        .group(NotEnoughRails.MOD_ID + ":crushed_vermilion")
                        .criterion(hasItem(Items.RAW_COPPER), conditionsFromItem(Items.RAW_COPPER))
                        .offerTo(exporter, ":crushed_vermilion_from_raw_ore");

                //Vermilion Ingot
                List<ItemConvertible> VERMILION_INGOT_SMELTABLES = List.of(AllItems.CRUSHED_VERMILION);
                offerSmelting(VERMILION_INGOT_SMELTABLES, RecipeCategory.MISC, AllItems.VERMILION_INGOT, 1.0F, 200, NotEnoughRails.MOD_ID + ":vermilion_ingot");
                offerBlasting(VERMILION_INGOT_SMELTABLES, RecipeCategory.MISC, AllItems.VERMILION_INGOT, 1.0F, 100, NotEnoughRails.MOD_ID + ":vermilion_ingot");
                offerReversibleCompactingRecipes(RecipeCategory.MISC, AllItems.VERMILION_INGOT,RecipeCategory.BUILDING_BLOCKS, AllBlocks.VERMILION_BLOCK);

                //Kaolin
                createShapeless(RecipeCategory.MISC, AllItems.KAOLIN, 1)
                        .input(Items.CLAY_BALL, 1)
                        .input(AllItems.FLUX, 1)
                        .input(Blocks.NETHERRACK, 1)
                        .group(NotEnoughRails.MOD_ID + ":kaolin")
                        .criterion(hasItem(AllBlocks.FLUXSTONE), conditionsFromItem(AllBlocks.FLUXSTONE))
                        .offerTo(exporter);

                List<ItemConvertible> FIRE_BRICK_SMELTABLES = List.of(AllItems.KAOLIN);
                offerSmelting(FIRE_BRICK_SMELTABLES, RecipeCategory.MISC, AllItems.FIRE_BRICK, 0.15F, 200, NotEnoughRails.MOD_ID + ":fire_brick");
                offerBlasting(FIRE_BRICK_SMELTABLES, RecipeCategory.MISC, AllItems.FIRE_BRICK, 0.15F, 100, NotEnoughRails.MOD_ID + ":fire_brick");

                //Linen
                createShapeless(RecipeCategory.MISC, AllItems.LINEN, 1)
                        .input(AllItems.FLAX_STRING, 9)
                        .group(NotEnoughRails.MOD_ID + ":linen")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);

                //Linseed Oil
                createShapeless(RecipeCategory.MISC, AllItems.LINSEED_OIL, 1)
                        .input(AllItems.FLAXSEEDS, 6)
                        .input(Items.GLASS_BOTTLE, 1)
                        .group(NotEnoughRails.MOD_ID + ":linseed_oil")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);

                //Railroad Tie
                createShaped(RecipeCategory.TRANSPORTATION, AllItems.RAILROAD_TIE, 6)
                        .input('o', AllItems.LINSEED_OIL)
                        .input('s', ItemTags.WOODEN_SLABS)
                        .pattern(" o ")
                        .pattern("sss")
                        .group(NotEnoughRails.MOD_ID + ":railroad_tie")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);

                // endregion

                //--------------------------------------------------------------------------------------------------------------
                // region Block Recipes
                NotEnoughRails.LOGGER.info("Generating all block recipes");

                //Activator Rail
                createShaped(RecipeCategory.TRANSPORTATION, Blocks.ACTIVATOR_RAIL, 8)
                        .input('i', AllItems.IRON_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('r', Items.REDSTONE_TORCH)
                        .pattern("i i")
                        .pattern("iti")
                        .pattern("iri")
                        .group(NotEnoughRails.MOD_ID + ":activator_rail")
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .offerTo(exporter);

                //Braking Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.BRAKING_RAIL, 16)
                        .input('i', AllItems.IRON_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('b', AllItems.FIRE_BRICK)
                        .input('r', Items.REDSTONE_TORCH)
                        .pattern("i i")
                        .pattern("btb")
                        .pattern("iri")
                        .group(NotEnoughRails.MOD_ID + ":braking_rail")
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .offerTo(exporter);

                //Buffer Stop Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.BUFFER_STOP_RAIL, 8)
                        .input('i', AllItems.IRON_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('x', Blocks.IRON_BLOCK)
                        .pattern("iti")
                        .pattern("ixi")
                        .pattern("iti")
                        .group(NotEnoughRails.MOD_ID + ":buffer_stop_rail")
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .offerTo(exporter);

                //Check Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.CHECK_RAIL, 16)
                        .input('i', AllItems.IRON_ROD)
                        .input('l', AllItems.BOOSTER_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('r', Items.REPEATER)
                        .pattern("i i")
                        .pattern("ltl")
                        .pattern("iri")
                        .group(NotEnoughRails.MOD_ID + ":check_rail")
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .offerTo(exporter);

                //Chime Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.CHIME_RAIL, 8)
                        .input('i', AllItems.IRON_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('p', Items.STONE_PRESSURE_PLATE)
                        .input('n', Items.NOTE_BLOCK)
                        .pattern("ipi")
                        .pattern("iti")
                        .pattern("ini")
                        .group(NotEnoughRails.MOD_ID + ":chime_rail")
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.TRANSPORTATION, AllBlocks.CHIME_RAIL, 8)
                        .input(Items.NOTE_BLOCK, 1)
                        .input(Items.DETECTOR_RAIL, 8)
                        .group(NotEnoughRails.MOD_ID + ":chime_rail")
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .offerTo(exporter, ":chime_rail_from_detector_rails");

                //Crossover Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.CROSSOVER_RAIL, 8)
                        .input('i', AllItems.IRON_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .pattern("iii")
                        .pattern("iti")
                        .pattern("iii")
                        .group(NotEnoughRails.MOD_ID + ":crossover_rail")
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .offerTo(exporter);

                //Steel Block
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_BLOCK, 1)
                        .input('i', AllItems.STEEL_INGOT)
                        .pattern("iii")
                        .pattern("iii")
                        .pattern("iii")
                        .group(NotEnoughRails.MOD_ID + ":steel_block")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);

                //Cut Steel
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_CUT_BLOCK, 4)
                        .input('c', AllBlocks.STEEL_BLOCK)
                        .pattern("cc")
                        .pattern("cc")
                        .group(NotEnoughRails.MOD_ID + ":steel_cut_block")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_CUT_BLOCK, AllBlocks.STEEL_BLOCK, 4);

                //Cut Steel Slab
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_CUT_SLAB, 6)
                        .input('c', AllBlocks.STEEL_CUT_BLOCK)
                        .pattern("ccc")
                        .group(NotEnoughRails.MOD_ID + ":steel_cut_slab")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_CUT_SLAB, AllBlocks.STEEL_CUT_BLOCK, 2);

                //Cut Steel Stairs
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_CUT_STAIRS, 4)
                        .input('c', AllBlocks.STEEL_CUT_BLOCK)
                        .pattern("c  ")
                        .pattern("cc ")
                        .pattern("ccc")
                        .group(NotEnoughRails.MOD_ID + ":steel_cut_stairs")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_CUT_STAIRS, AllBlocks.STEEL_CUT_BLOCK, 1);

                //Chiseled Steel
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_CHISELED_BLOCK, 1)
                        .input('c', AllBlocks.STEEL_CUT_SLAB)
                        .pattern("c")
                        .pattern("c")
                        .group(NotEnoughRails.MOD_ID + ":steel_chiseled_block")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_CHISELED_BLOCK, AllBlocks.STEEL_BLOCK, 4);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_CHISELED_BLOCK, AllBlocks.STEEL_CUT_BLOCK, 1);

                //Copper Activator Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_ACTIVATOR_RAIL, 8)
                        .input('i', AllItems.COPPER_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('r', Items.REDSTONE_TORCH)
                        .pattern("i i")
                        .pattern("iti")
                        .pattern("iri")
                        .group(NotEnoughRails.MOD_ID + ":copper_activator_rail")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Copper Activator Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_ACTIVATOR_RAIL_WAXED, 8)
                        .input('r', AllBlocks.COPPER_ACTIVATOR_RAIL)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_activator_copper_rail")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Exposed Copper Activator Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_ACTIVATOR_RAIL_EXPOSED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_activator_rail_exposed")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Weathered Copper Activator Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_ACTIVATOR_RAIL_WEATHERED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_activator_rail_weathered")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Oxidized Copper Activator Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_ACTIVATOR_RAIL_OXIDIZED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_activator_rail_oxidized")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Copper Buffer Stop Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_BUFFER_STOP_RAIL, 8)
                        .input('i', AllItems.COPPER_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('x', Blocks.IRON_BLOCK)
                        .pattern("iti")
                        .pattern("ixi")
                        .pattern("iti")
                        .group(NotEnoughRails.MOD_ID + ":copper_buffer_stop_rail")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Copper Buffer Stop Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_BUFFER_STOP_RAIL_WAXED, 8)
                        .input('r', AllBlocks.COPPER_BUFFER_STOP_RAIL)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_buffer_stop_copper_rail")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Exposed Copper Buffer Stop Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_BUFFER_STOP_RAIL_EXPOSED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_BUFFER_STOP_RAIL_EXPOSED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_buffer_stop_rail_exposed")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Weathered Copper Buffer Stop Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_BUFFER_STOP_RAIL_WEATHERED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_BUFFER_STOP_RAIL_WEATHERED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_buffer_stop_rail_weathered")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Oxidized Copper Buffer Stop Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_BUFFER_STOP_RAIL_OXIDIZED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_BUFFER_STOP_RAIL_OXIDIZED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_buffer_stop_rail_oxidized")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Copper Crossover Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_CROSSOVER_RAIL, 8)
                        .input('c', AllItems.COPPER_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .pattern("ccc")
                        .pattern("ctc")
                        .pattern("ccc")
                        .group(NotEnoughRails.MOD_ID + ":copper_crossover_rail")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Copper Crossover Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_CROSSOVER_RAIL_WAXED, 8)
                        .input('r', AllBlocks.COPPER_CROSSOVER_RAIL)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_crossover_copper_rail")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Exposed Copper Crossover Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_CROSSOVER_RAIL_EXPOSED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_CROSSOVER_RAIL_EXPOSED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_crossover_rail_exposed")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Weathered Copper Crossover Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_CROSSOVER_RAIL_WEATHERED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_CROSSOVER_RAIL_WEATHERED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_crossover_rail_weathered")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Oxidized Copper Crossover Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_CROSSOVER_RAIL_OXIDIZED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_CROSSOVER_RAIL_OXIDIZED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_crossover_rail_oxidized")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Copper Detector Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_DETECTOR_RAIL, 8)
                        .input('i', AllItems.COPPER_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('r', Items.REDSTONE)
                        .input('p', Items.STONE_PRESSURE_PLATE)
                        .pattern("ipi")
                        .pattern("iti")
                        .pattern("iri")
                        .group(NotEnoughRails.MOD_ID + ":copper_detector_rail")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Copper Detector Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_DETECTOR_RAIL_WAXED, 8)
                        .input('r', AllBlocks.COPPER_DETECTOR_RAIL)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_detector_copper_rail")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Waxed Exposed Copper Detector Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_DETECTOR_RAIL_EXPOSED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_DETECTOR_RAIL_EXPOSED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_detector_rail_exposed")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Waxed Weathered Copper Detector Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_DETECTOR_RAIL_WEATHERED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_DETECTOR_RAIL_WEATHERED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_detector_rail_weathered")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Waxed Oxidized Copper Detector Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_DETECTOR_RAIL_OXIDIZED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_DETECTOR_RAIL_OXIDIZED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_detector_rail_oxidized")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Copper Powered Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_POWERED_RAIL, 16)
                        .input('b', AllItems.BOOSTER_ROD_COPPER)
                        .input('t', AllItems.RAILROAD_TIE)
                        .pattern("b b")
                        .pattern("btb")
                        .pattern("b b")
                        .group(NotEnoughRails.MOD_ID + ":copper_powered_rail")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Waxed Copper Powered Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_POWERED_RAIL_WAXED, 8)
                        .input('r', AllBlocks.COPPER_POWERED_RAIL)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_powered_copper_rail")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Waxed Exposed Copper Powered Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_POWERED_RAIL_EXPOSED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_POWERED_RAIL_EXPOSED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_powered_rail_exposed")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Waxed Weathered Copper Powered Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_POWERED_RAIL_WEATHERED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_POWERED_RAIL_WEATHERED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_powered_rail_weathered")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Waxed Oxidized Copper Powered Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_POWERED_RAIL_OXIDIZED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_POWERED_RAIL_OXIDIZED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_powered_rail_oxidized")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Copper Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_RAIL, 24)
                        .input('i', AllItems.COPPER_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .pattern("i i")
                        .pattern("iti")
                        .pattern("i i")
                        .group(NotEnoughRails.MOD_ID + ":copper_rail")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Copper Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_RAIL_WAXED, 8)
                        .input('r', AllBlocks.COPPER_RAIL)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_rail")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Exposed Copper Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_RAIL_EXPOSED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_RAIL_EXPOSED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_rail_exposed")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Weathered Copper Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_RAIL_WEATHERED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_RAIL_WEATHERED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_rail_weathered")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Waxed Oxidized Copper Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.COPPER_RAIL_OXIDIZED_WAXED, 8)
                        .input('r', AllBlocks.COPPER_RAIL_OXIDIZED)
                        .input('w', Items.HONEYCOMB)
                        .pattern("rrr")
                        .pattern("rwr")
                        .pattern("rrr")
                        .group(NotEnoughRails.MOD_ID + ":waxed_copper_rail_oxidized")
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);

                //Linen Blocks
                createShaped(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK, 1)
                        .input('#', AllItems.LINEN)
                        .pattern("##")
                        .pattern("##")
                        .group(NotEnoughRails.MOD_ID + ":linen_block")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_WHITE, 1)
                        .input(Items.WHITE_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_LIGHT_GRAY, 1)
                        .input(Items.LIGHT_GRAY_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_GRAY, 1)
                        .input(Items.GRAY_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_BLACK, 1)
                        .input(Items.BLACK_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_BROWN, 1)
                        .input(Items.BROWN_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_RED, 1)
                        .input(Items.RED_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_ORANGE, 1)
                        .input(Items.ORANGE_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_YELLOW, 1)
                        .input(Items.YELLOW_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_LIME, 1)
                        .input(Items.LIME_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_GREEN, 1)
                        .input(Items.GREEN_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_CYAN, 1)
                        .input(Items.CYAN_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_LIGHT_BLUE, 1)
                        .input(Items.LIGHT_BLUE_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_BLUE, 1)
                        .input(Items.BLUE_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_PURPLE, 1)
                        .input(Items.PURPLE_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_MAGENTA, 1)
                        .input(Items.MAGENTA_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);
                createShapeless(RecipeCategory.DECORATIONS, AllBlocks.LINEN_BLOCK_PINK, 1)
                        .input(Items.PINK_DYE, 1)
                        .input(AllItemTags.LINEN_BLOCKS)
                        .group(NotEnoughRails.MOD_ID + ":linen_block_dyed")
                        .criterion(hasItem(AllItems.FLAX), conditionsFromItem(AllItems.FLAX))
                        .offerTo(exporter);

                //Steel Crossover Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.STEEL_CROSSOVER_RAIL, 8)
                        .input('i', AllItems.STEEL_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .pattern("iii")
                        .pattern("iti")
                        .pattern("iii")
                        .group(NotEnoughRails.MOD_ID + ":steel_crossover_rail")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);

                //Steel Activator Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.STEEL_ACTIVATOR_RAIL, 8)
                        .input('i', AllItems.STEEL_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('r', Items.REDSTONE_TORCH)
                        .pattern("i i")
                        .pattern("iti")
                        .pattern("iri")
                        .group(NotEnoughRails.MOD_ID + ":steel_activator_rail")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);

                //Steel Buffer Stop Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.STEEL_BUFFER_STOP_RAIL, 8)
                        .input('i', AllItems.STEEL_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('x', Blocks.IRON_BLOCK)
                        .pattern("iti")
                        .pattern("ixi")
                        .pattern("iti")
                        .group(NotEnoughRails.MOD_ID + ":steel_buffer_stop_rail")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);

                //Steel Detector Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.STEEL_DETECTOR_RAIL, 8)
                        .input('i', AllItems.STEEL_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('r', Items.REDSTONE)
                        .input('p', Items.STONE_PRESSURE_PLATE)
                        .pattern("ipi")
                        .pattern("iti")
                        .pattern("iri")
                        .group(NotEnoughRails.MOD_ID + ":steel_detector_rail")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);

                //Steel Door
                createDoorRecipe(AllBlocks.STEEL_DOOR, Ingredient.ofItem(AllItems.STEEL_INGOT))
                        .group(NotEnoughRails.MOD_ID + ":steel_door")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);

                //Steel Grate
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_GRATE, 4)
                        .input('c', AllBlocks.STEEL_BLOCK)
                        .pattern(" c ")
                        .pattern("c c")
                        .pattern(" c ")
                        .group(NotEnoughRails.MOD_ID + ":steel_grate")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.STEEL_GRATE, AllBlocks.STEEL_BLOCK, 4);

                //Steel Powered Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.STEEL_POWERED_RAIL, 16)
                        .input('b', AllItems.BOOSTER_ROD_STEEL)
                        .input('t', AllItems.RAILROAD_TIE)
                        .pattern("b b")
                        .pattern("btb")
                        .pattern("b b")
                        .group(NotEnoughRails.MOD_ID + ":steel_powered_rail")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Steel Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.STEEL_RAIL, 24)
                        .input('i', AllItems.STEEL_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .pattern("i i")
                        .pattern("iti")
                        .pattern("i i")
                        .group(NotEnoughRails.MOD_ID + ":steel_rail")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);

                //Steel Trapdoor
                createTrapdoorRecipe(AllBlocks.STEEL_TRAPDOOR, Ingredient.ofItem(AllItems.STEEL_INGOT))
                        .group(NotEnoughRails.MOD_ID + ":steel_trapdoor")
                        .criterion(hasItem(AllItems.STEEL_INGOT), conditionsFromItem(AllItems.STEEL_INGOT))
                        .offerTo(exporter);

                //Detector Rail
                createShaped(RecipeCategory.TRANSPORTATION, Blocks.DETECTOR_RAIL, 8)
                        .input('i', AllItems.IRON_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('r', Items.REDSTONE)
                        .input('p', Items.STONE_PRESSURE_PLATE)
                        .pattern("ipi")
                        .pattern("iti")
                        .pattern("iri")
                        .group(NotEnoughRails.MOD_ID + ":detector_rail")
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .offerTo(exporter);

                //Polished Fluxstone
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.FLUXSTONE_POLISHED, 4)
                        .input('c', AllBlocks.FLUXSTONE)
                        .pattern("cc")
                        .pattern("cc")
                        .group(NotEnoughRails.MOD_ID + ":fluxstone_polished")
                        .criterion(hasItem(AllBlocks.FLUXSTONE), conditionsFromItem(AllBlocks.FLUXSTONE))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.FLUXSTONE_POLISHED, AllBlocks.FLUXSTONE, 1);

                List<ItemConvertible> FULXSTONE_SMOOTH_SMELTABLES = List.of(AllBlocks.FLUXSTONE);
                offerSmelting(FULXSTONE_SMOOTH_SMELTABLES, RecipeCategory.MISC, AllBlocks.FLUXSTONE_SMOOTH, 0.15F, 200, NotEnoughRails.MOD_ID + ":fluxstone_smooth");
                offerBlasting(FULXSTONE_SMOOTH_SMELTABLES, RecipeCategory.MISC, AllBlocks.FLUXSTONE_SMOOTH, 0.15F, 100, NotEnoughRails.MOD_ID + ":fluxstone_smooth");

                //Polished Fluxstone Slab
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.FLUXSTONE_SMOOTH_SLAB, 6)
                        .input('c', AllBlocks.FLUXSTONE_SMOOTH)
                        .pattern("ccc")
                        .group(NotEnoughRails.MOD_ID + ":fluxstone_smooth_slab")
                        .criterion(hasItem(AllBlocks.FLUXSTONE), conditionsFromItem(AllBlocks.FLUXSTONE))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.FLUXSTONE_SMOOTH_SLAB, AllBlocks.FLUXSTONE_SMOOTH, 2);

                //Polished Fluxstone Stairs
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.FLUXSTONE_SMOOTH_STAIRS, 4)
                        .input('c', AllBlocks.FLUXSTONE_SMOOTH)
                        .pattern("c  ")
                        .pattern("cc ")
                        .pattern("ccc")
                        .group(NotEnoughRails.MOD_ID + ":fluxstone_smooth_stairs")
                        .criterion(hasItem(AllBlocks.FLUXSTONE), conditionsFromItem(AllBlocks.FLUXSTONE))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.FLUXSTONE_SMOOTH_STAIRS, AllBlocks.FLUXSTONE_SMOOTH, 1);

                //Powered Rail
                createShaped(RecipeCategory.TRANSPORTATION, Blocks.POWERED_RAIL, 16)
                        .input('b', AllItems.BOOSTER_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .pattern("b b")
                        .pattern("btb")
                        .pattern("b b")
                        .group(NotEnoughRails.MOD_ID + ":powered_rail")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Locking Rail
                createShaped(RecipeCategory.TRANSPORTATION, AllBlocks.LOCKING_RAIL, 8)
                        .input('i', AllItems.IRON_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .input('b', AllItems.BOOSTER_ROD)
                        .input('p', Blocks.PISTON)
                        .pattern("iti")
                        .pattern("bpb")
                        .pattern("iti")
                        .group(NotEnoughRails.MOD_ID + ":locking_rail")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(exporter);

                //Rail
                createShaped(RecipeCategory.TRANSPORTATION, Blocks.RAIL, 24)
                        .input('i', AllItems.IRON_ROD)
                        .input('t', AllItems.RAILROAD_TIE)
                        .pattern("i i")
                        .pattern("iti")
                        .pattern("i i")
                        .group(NotEnoughRails.MOD_ID + ":rail")
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .offerTo(exporter);
                // endregion
            }

            public void offerSmoking(List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
                this.offerMultipleOptions(RecipeSerializer.SMOKING, SmokingRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_smoking");
            }
        };
    }

    @Override
    public String getName() {
        return NotEnoughRails.MOD_ID + ".recipes";
    }
}
