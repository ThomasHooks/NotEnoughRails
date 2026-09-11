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
package com.github.thomashooks.notenoughrails.recipe;

import com.github.thomashooks.notenoughrails.recipe.input.SimpleRecipeInput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public record BlastingRecipe(Ingredient ingredient, ItemStack result, int cookingTime)
        implements Recipe<SimpleRecipeInput> {
    public static final int DEFAULT_COOKING_TIME = 400;

    @Override
    public boolean matches(SimpleRecipeInput input, World world) { return this.ingredient.test(input.item()); }

    @Override
    public ItemStack craft(SimpleRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return this.result.copy();
    }

    @Override
    public String getGroup() { return "blasting"; }

    @Override
    public RecipeSerializer<? extends Recipe<SimpleRecipeInput>> getSerializer() {
        return AllRecipes.Serializers.BLASTING;
    }

    @Override
    public RecipeType<? extends Recipe<SimpleRecipeInput>> getType() { return AllRecipes.Types.BLASTING; }

    @Override
    public boolean isIgnoredInRecipeBook() { return true; }

    @Override
    public IngredientPlacement getIngredientPlacement() { return IngredientPlacement.NONE; }

    @Override
    public RecipeBookCategory getRecipeBookCategory() { return null; }

    public static class Serializer implements RecipeSerializer<BlastingRecipe> {
        public static final BlastingRecipe.Serializer INSTANCE = new BlastingRecipe.Serializer();
        private static final MapCodec<BlastingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(BlastingRecipe::ingredient),
                        ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(BlastingRecipe::result),
                        Codec.INT.fieldOf("cooking_time").orElse(DEFAULT_COOKING_TIME).forGetter(BlastingRecipe::cookingTime)
                ).apply(instance, BlastingRecipe::new)
        );
        private static final PacketCodec<RegistryByteBuf, BlastingRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, BlastingRecipe::ingredient,
                ItemStack.PACKET_CODEC, BlastingRecipe::result,
                PacketCodecs.INTEGER, BlastingRecipe::cookingTime,
                BlastingRecipe::new
        );

        @Override
        public MapCodec<BlastingRecipe> codec() { return CODEC; }

        @Override
        public PacketCodec<RegistryByteBuf, BlastingRecipe> packetCodec() { return PACKET_CODEC; }
    }

    public static class Type implements RecipeType<BlastingRecipe> {
        public static final BlastingRecipe.Type INSTANCE = new BlastingRecipe.Type();

        @Override
        public String toString() { return "blasting"; }
    }
}
