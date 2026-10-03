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

public record MillingRecipe(Ingredient ingredient, ItemStack result, int millingTime, float bonus)
        implements Recipe<SimpleRecipeInput> {
    public static final int DEFAULT_MILLING_TIME = 200;
    public static final float DEFAULT_BONUS = 0.0F;

    @Override
    public boolean matches(SimpleRecipeInput input, World world) { return this.ingredient.test(input.item()); }

    @Override
    public ItemStack craft(SimpleRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return this.result.copy();
    }

    public String getGroup() { return "milling"; }

    @Override
    public RecipeSerializer<? extends Recipe<SimpleRecipeInput>> getSerializer() {
        return AllRecipes.Serializers.MILLING;
    }

    @Override
    public RecipeType<? extends Recipe<SimpleRecipeInput>> getType() { return AllRecipes.Types.MILLING; }

    @Override
    public boolean isIgnoredInRecipeBook() { return true; }

    @Override
    public IngredientPlacement getIngredientPlacement() { return IngredientPlacement.NONE; }

    @Override
    public RecipeBookCategory getRecipeBookCategory() { return null; }

    public static class Serializer implements RecipeSerializer<MillingRecipe> {
        public static final MillingRecipe.Serializer INSTANCE = new MillingRecipe.Serializer();
        private static final MapCodec<MillingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(MillingRecipe::ingredient),
                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(MillingRecipe::result),
                Codec.INT.fieldOf("milling_time").orElse(DEFAULT_MILLING_TIME).forGetter(MillingRecipe::millingTime),
                Codec.FLOAT.fieldOf("bonus").orElse(DEFAULT_BONUS).forGetter(MillingRecipe::bonus)
                ).apply(instance, MillingRecipe::new)
        );
        private static final PacketCodec<RegistryByteBuf, MillingRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, MillingRecipe::ingredient,
                ItemStack.PACKET_CODEC, MillingRecipe::result,
                PacketCodecs.INTEGER, MillingRecipe::millingTime,
                PacketCodecs.FLOAT, MillingRecipe::bonus,
                MillingRecipe::new
        );

        @Override
        public MapCodec<MillingRecipe> codec() { return CODEC; }

        @Override
        public PacketCodec<RegistryByteBuf, MillingRecipe> packetCodec() { return PACKET_CODEC; }
    }

    public static class Type implements RecipeType<MillingRecipe> {
        public static final MillingRecipe.Type INSTANCE = new MillingRecipe.Type();

        @Override
        public String toString() { return "milling"; }
    }
}
