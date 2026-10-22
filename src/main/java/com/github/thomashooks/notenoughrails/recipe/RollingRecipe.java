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

public record RollingRecipe(Ingredient ingredient, ItemStack result, int millingTime)
        implements Recipe<SimpleRecipeInput> {
    public static final int DEFAULT_MILLING_TIME = 200;

    public boolean matches(SimpleRecipeInput input, World world) { return this.ingredient.test(input.item()); }

    @Override
    public ItemStack craft(SimpleRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return this.result.copy();
    }

    public String getGroup() { return "rolling"; }

    @Override
    public RecipeSerializer<? extends Recipe<SimpleRecipeInput>> getSerializer() {
        return AllRecipes.Serializers.ROLLING;
    }

    @Override
    public RecipeType<? extends Recipe<SimpleRecipeInput>> getType() { return AllRecipes.Types.ROLLING; }

    @Override
    public boolean isIgnoredInRecipeBook() { return true; }

    @Override
    public IngredientPlacement getIngredientPlacement() { return IngredientPlacement.NONE; }

    @Override
    public RecipeBookCategory getRecipeBookCategory() { return null; }

    public static class Serializer implements RecipeSerializer<RollingRecipe> {
        public static final RollingRecipe.Serializer INSTANCE = new RollingRecipe.Serializer();
        private static final MapCodec<RollingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(RollingRecipe::ingredient),
                        ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(RollingRecipe::result),
                        Codec.INT.fieldOf("milling_time").orElse(DEFAULT_MILLING_TIME).forGetter(RollingRecipe::millingTime)
                ).apply(instance, RollingRecipe::new)
        );
        private static final PacketCodec<RegistryByteBuf, RollingRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, RollingRecipe::ingredient,
                ItemStack.PACKET_CODEC, RollingRecipe::result,
                PacketCodecs.INTEGER, RollingRecipe::millingTime,
                RollingRecipe::new
        );

        @Override
        public MapCodec<RollingRecipe> codec() { return CODEC; }

        @Override
        public PacketCodec<RegistryByteBuf, RollingRecipe> packetCodec() { return PACKET_CODEC; }
    }

    public static class Type implements RecipeType<RollingRecipe> {
        public static final RollingRecipe.Type INSTANCE = new RollingRecipe.Type();

        @Override
        public String toString() { return "rolling"; }
    }
}
