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

public record CokingRecipe(Ingredient ingredient, ItemStack result, int cookingTime)
        implements Recipe<SimpleRecipeInput> {
    public static final int DEFAULT_COOKING_TIME = 2400;

    @Override
    public boolean matches(SimpleRecipeInput input, World world) {
        return this.ingredient.test(input.item());
    }

    @Override
    public ItemStack craft(SimpleRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return this.result.copy();
    }

    @Override
    public String getGroup() { return "coking"; }

    @Override
    public RecipeSerializer<? extends Recipe<SimpleRecipeInput>> getSerializer() {
        return AllRecipes.Serializers.COKING;
    }

    @Override
    public RecipeType<? extends Recipe<SimpleRecipeInput>> getType() {
        return AllRecipes.Types.COKING;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.NONE;
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return null;
    }

    public static class Serializer implements RecipeSerializer<CokingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        private static final MapCodec<CokingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(CokingRecipe::ingredient),
                ItemStack.VALIDATED_UNCOUNTED_CODEC.fieldOf("result").forGetter(CokingRecipe::result),
                Codec.INT.fieldOf("cooking_time").orElse(DEFAULT_COOKING_TIME).forGetter(CokingRecipe::cookingTime)
                ).apply(instance, CokingRecipe::new)
        );
        private static final PacketCodec<RegistryByteBuf, CokingRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, CokingRecipe::ingredient,
                ItemStack.PACKET_CODEC, CokingRecipe::result,
                PacketCodecs.INTEGER, CokingRecipe::cookingTime,
                CokingRecipe::new
        );

        @Override
        public MapCodec<CokingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, CokingRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }

    public static class Type implements RecipeType<CokingRecipe> {
        public static final Type INSTANCE = new Type();

        @Override
        public String toString() {
            return "coking";
        }
    }
}
