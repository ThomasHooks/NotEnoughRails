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
import com.github.thomashooks.notenoughrails.world.block.AllBlocks;
import com.github.thomashooks.notenoughrails.world.item.AllItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
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
                //Items
                //--------------------------------------------------------------------------------------------------------------

                //Bread
                List<ItemConvertible> BREAD_COOKABLES = List.of(AllItems.FLOUR);
                offerSmelting(BREAD_COOKABLES, RecipeCategory.FOOD, Items.BREAD, 0.35F, 200, NotEnoughRails.MOD_ID + ":bread");
                offerSmoking(BREAD_COOKABLES, RecipeCategory.FOOD, Items.BREAD, 0.35F, 100, NotEnoughRails.MOD_ID + ":bread");

                //Coke & Coke Block
                offerReversibleCompactingRecipes(RecipeCategory.MISC, AllItems.COKE,RecipeCategory.MISC, AllBlocks.COKE_BLOCK);

                //Copper Ingot
                List<ItemConvertible> COPPER_INGOT_SMELTABLES = List.of(AllItems.CRUSHED_COPPER_ORE);
                offerSmelting(COPPER_INGOT_SMELTABLES, RecipeCategory.MISC, Items.COPPER_INGOT, 0.7F, 200, NotEnoughRails.MOD_ID + ":copper_ingot");
                offerBlasting(COPPER_INGOT_SMELTABLES, RecipeCategory.MISC, Items.COPPER_INGOT, 0.7F, 100, NotEnoughRails.MOD_ID + ":copper_ingot");

                //Crushed Corite
                createShapeless(RecipeCategory.MISC, AllItems.CRUSHED_CORITE, 2)
                        .input(AllItems.CRUSHED_COPPER_ORE, 4)
                        .input(AllItems.CRUSHED_IRON_ORE, 1)
                        .input(AllItems.FLUX, 1)
                        .group(NotEnoughRails.MOD_ID + ":crushed_corite")
                        .criterion(hasItem(Items.IRON_ORE), conditionsFromItem(Items.IRON_ORE))
                        .offerTo(exporter);

                //Corite Ingot
                List<ItemConvertible> CORITE_INGOT_SMELTABLES = List.of(AllItems.CRUSHED_CORITE);
                offerSmelting(CORITE_INGOT_SMELTABLES, RecipeCategory.MISC, AllItems.CORITE_INGOT, 0.7F, 200, NotEnoughRails.MOD_ID + ":corite_ingot");
                offerBlasting(CORITE_INGOT_SMELTABLES, RecipeCategory.MISC, AllItems.CORITE_INGOT, 0.7F, 100, NotEnoughRails.MOD_ID + ":corite_ingot");
                offerReversibleCompactingRecipes(RecipeCategory.MISC, AllItems.CORITE_INGOT,RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_BLOCK);

                //Corite Plate
                offerReversibleCompactingRecipes(RecipeCategory.MISC, AllItems.CORITE_PLATE,RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_PLATE_BLOCK);

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
                        .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                        .offerTo(exporter);

                //Vermilion Ingot
                List<ItemConvertible> VERMILION_INGOT_SMELTABLES = List.of(AllItems.CRUSHED_VERMILION);
                offerSmelting(VERMILION_INGOT_SMELTABLES, RecipeCategory.MISC, AllItems.VERMILION_INGOT, 1.0F, 200, NotEnoughRails.MOD_ID + ":vermilion_ingot");
                offerBlasting(VERMILION_INGOT_SMELTABLES, RecipeCategory.MISC, AllItems.VERMILION_INGOT, 1.0F, 100, NotEnoughRails.MOD_ID + ":vermilion_ingot");
                offerReversibleCompactingRecipes(RecipeCategory.MISC, AllItems.VERMILION_INGOT,RecipeCategory.BUILDING_BLOCKS, AllBlocks.VERMILION_BLOCK);

                //Kaolin
                createShapeless(RecipeCategory.MISC, AllItems.KAOLIN, 1)
                        .input(Items.CLAY_BALL, 1)
                        .input(AllItems.FLUX, 3)
                        .group(NotEnoughRails.MOD_ID + ":kaolin")
                        .criterion(hasItem(AllBlocks.FLUXSTONE), conditionsFromItem(AllBlocks.FLUXSTONE))
                        .offerTo(exporter);

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

                //Blocks
                //--------------------------------------------------------------------------------------------------------------

                //Cut Corite
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_CUT_BLOCK, 4)
                        .input('c', AllBlocks.CORITE_BLOCK)
                        .pattern("cc")
                        .pattern("cc")
                        .group(NotEnoughRails.MOD_ID + ":corite_cut_block")
                        .criterion(hasItem(AllItems.CORITE_INGOT), conditionsFromItem(AllItems.CORITE_INGOT))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_CUT_BLOCK, AllBlocks.CORITE_BLOCK, 4);

                //Cut Corite Slab
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_CUT_SLAB, 6)
                        .input('c', AllBlocks.CORITE_CUT_BLOCK)
                        .pattern("ccc")
                        .group(NotEnoughRails.MOD_ID + ":corite_cut_slab")
                        .criterion(hasItem(AllItems.CORITE_INGOT), conditionsFromItem(AllItems.CORITE_INGOT))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_CUT_SLAB, AllBlocks.CORITE_CUT_BLOCK, 2);

                //Cut Corite Stairs
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_CUT_STAIRS, 4)
                        .input('c', AllBlocks.CORITE_CUT_BLOCK)
                        .pattern("c  ")
                        .pattern("cc ")
                        .pattern("ccc")
                        .group(NotEnoughRails.MOD_ID + ":corite_cut_stairs")
                        .criterion(hasItem(AllItems.CORITE_INGOT), conditionsFromItem(AllItems.CORITE_INGOT))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_CUT_STAIRS, AllBlocks.CORITE_CUT_BLOCK, 1);

                //Chiseled Corite
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_CHISELED_BLOCK, 1)
                        .input('c', AllBlocks.CORITE_CUT_SLAB)
                        .pattern("c")
                        .pattern("c")
                        .group(NotEnoughRails.MOD_ID + ":corite_chiseled_block")
                        .criterion(hasItem(AllItems.CORITE_INGOT), conditionsFromItem(AllItems.CORITE_INGOT))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_CHISELED_BLOCK, AllBlocks.CORITE_BLOCK, 4);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_CHISELED_BLOCK, AllBlocks.CORITE_CUT_BLOCK, 1);

                //Corite Door
                createDoorRecipe(AllBlocks.CORITE_DOOR, Ingredient.ofItem(AllItems.CORITE_INGOT))
                        .group(NotEnoughRails.MOD_ID + ":corite_door")
                        .criterion(hasItem(AllItems.CORITE_INGOT), conditionsFromItem(AllItems.CORITE_INGOT))
                        .offerTo(exporter);

                //Corite Grate
                createShaped(RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_GRATE, 4)
                        .input('c', AllBlocks.CORITE_BLOCK)
                        .pattern(" c ")
                        .pattern("c c")
                        .pattern(" c ")
                        .group(NotEnoughRails.MOD_ID + ":corite_grate")
                        .criterion(hasItem(AllItems.CORITE_INGOT), conditionsFromItem(AllItems.CORITE_INGOT))
                        .offerTo(exporter);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, AllBlocks.CORITE_GRATE, AllBlocks.CORITE_BLOCK, 4);

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
