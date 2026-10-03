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
import com.github.thomashooks.notenoughrails.recipe.MillingRecipe;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.NotNull;

public record MillingRecipeBuilder(
        Ingredient input, Item output, int count, int millingTime, float bonus) {
    public static MillingRecipeBuilder create(Ingredient ingredient, Item result, int millingTime) {
        return new MillingRecipeBuilder(ingredient, result, 1, millingTime, 0.0F);
    }

    public static MillingRecipeBuilder create(Ingredient ingredient, Item result, int millingTime, float bonus) {
        return new MillingRecipeBuilder(ingredient, result, 1, millingTime, bonus);
    }

    public static MillingRecipeBuilder create(Ingredient ingredient, Item result, int count, int millingTime) {
        return new MillingRecipeBuilder(ingredient, result, count, millingTime, 0.0F);
    }

    public static MillingRecipeBuilder create(Ingredient ingredient, Item result, int count, int millingTime, float bonus) {
        return new MillingRecipeBuilder(ingredient, result, count, millingTime, bonus);
    }

    public void offerTo(@NotNull RecipeExporter exporter, String recipePath) {
        MillingRecipe recipe = new MillingRecipe(input, new ItemStack(output, count), millingTime, bonus);
        exporter.accept(
                RegistryKey.of(RegistryKeys.RECIPE, NotEnoughRails.identifier(recipePath)),
                recipe,
                null // TODO: Might add advancements later
        );
    }
}
