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
import com.github.thomashooks.notenoughrails.recipe.CokingRecipe;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.NotNull;

public record CokingRecipeBuilder(Ingredient ingredient, ItemStack result, int cookingTime) {
    public static CokingRecipeBuilder create(Ingredient ingredient, ItemStack result, int cookingTime) {
        return new CokingRecipeBuilder(ingredient, result, cookingTime);
    }

    public void offerTo(@NotNull RecipeExporter exporter, String recipePath) {
        exporter.accept(RegistryKey.of(RegistryKeys.RECIPE, NotEnoughRails.identifier(recipePath)),
                new CokingRecipe(ingredient, result, cookingTime),
                null // TODO: Might add advancements later
        );
    }
}
