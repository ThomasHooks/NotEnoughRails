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
package com.github.thomashooks.notenoughrails.data.models;

import com.github.thomashooks.notenoughrails.block.AllBlocks;
import com.github.thomashooks.notenoughrails.block.FlaxCropBlock;
import com.github.thomashooks.notenoughrails.block.property.AllProperties;
import com.github.thomashooks.notenoughrails.item.AllItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.block.enums.RailShape;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NotNull BlockStateModelGenerator modelGenerator) {
        registerDirectionalRail(AllBlocks.CHECK_RAIL, modelGenerator);
        modelGenerator.registerStraightRail(AllBlocks.CHIME_RAIL);
        modelGenerator.registerSimpleCubeAll(AllBlocks.COKE_BLOCK);
        modelGenerator.registerSimpleCubeAll(AllBlocks.CORITE_BLOCK);
        modelGenerator.registerSimpleCubeAll(AllBlocks.CORITE_CHISELED_BLOCK);
        BlockStateModelGenerator.BlockTexturePool coriteCutPool = modelGenerator.registerCubeAllModelTexturePool(AllBlocks.CORITE_CUT_BLOCK);
        coriteCutPool.slab(AllBlocks.CORITE_CUT_SLAB);
        coriteCutPool.stairs(AllBlocks.CORITE_CUT_STAIRS);
        registerPassiveFlatRail(AllBlocks.CORITE_CROSSOVER_RAIL, modelGenerator);
        modelGenerator.registerStraightRail(AllBlocks.CORITE_DETECTOR_RAIL);
        modelGenerator.registerDoor(AllBlocks.CORITE_DOOR);
        modelGenerator.registerSimpleCubeAll(AllBlocks.CORITE_GRATE);
        modelGenerator.registerAxisRotated(AllBlocks.CORITE_PLATE_BLOCK, TexturedModel.CUBE_COLUMN);
        modelGenerator.registerStraightRail(AllBlocks.CORITE_POWERED_RAIL);
        modelGenerator.registerTurnableRail(AllBlocks.CORITE_RAIL);
        modelGenerator.registerTrapdoor(AllBlocks.CORITE_TRAPDOOR);
        registerPassiveFlatRail(AllBlocks.CROSSOVER_RAIL, modelGenerator);
        modelGenerator.registerCrop(AllBlocks.FLAX_CROP, FlaxCropBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7);
        modelGenerator.registerAxisRotated(AllBlocks.FLUXSTONE, TexturedModel.CUBE_COLUMN);
        modelGenerator.registerAxisRotated(AllBlocks.FLUXSTONE_POLISHED, TexturedModel.CUBE_COLUMN);
        BlockStateModelGenerator.BlockTexturePool fluxstoneSmoothPool = modelGenerator.registerCubeAllModelTexturePool(AllBlocks.FLUXSTONE_SMOOTH);
        fluxstoneSmoothPool.slab(AllBlocks.FLUXSTONE_SMOOTH_SLAB);
        fluxstoneSmoothPool.stairs(AllBlocks.FLUXSTONE_SMOOTH_STAIRS);
        modelGenerator.registerAxisRotated(AllBlocks.IRON_PLATE_BLOCK, TexturedModel.CUBE_COLUMN);
        modelGenerator.registerSimpleCubeAll(AllBlocks.LINEN_BLOCK);
        modelGenerator.registerSimpleCubeAll(AllBlocks.VERMILION_BLOCK);
        modelGenerator.registerSimpleCubeAll(AllBlocks.WOODEN_FRAME);
    }

    @Override
    public void generateItemModels(@NotNull ItemModelGenerator modelGenerator) {
        modelGenerator.register(AllItems.BOOSTER_ROD, Models.GENERATED);
        modelGenerator.register(AllItems.BOOSTER_ROD_CORITE, Models.GENERATED);
        modelGenerator.register(AllItems.COKE, Models.GENERATED);
        modelGenerator.register(AllItems.COPPER_PLATE, Models.GENERATED);
        modelGenerator.register(AllItems.COPPER_ROD, Models.GENERATED);
        modelGenerator.register(AllItems.CORITE_INGOT, Models.GENERATED);
        modelGenerator.register(AllItems.CORITE_PLATE, Models.GENERATED);
        modelGenerator.register(AllItems.CORITE_ROD, Models.GENERATED);
        modelGenerator.register(AllItems.CRUSHED_COPPER_ORE, Models.GENERATED);
        modelGenerator.register(AllItems.CRUSHED_CORITE, Models.GENERATED);
        modelGenerator.register(AllItems.CRUSHED_GOLD_ORE, Models.GENERATED);
        modelGenerator.register(AllItems.CRUSHED_IRON_ORE, Models.GENERATED);
        modelGenerator.register(AllItems.CRUSHED_VERMILION, Models.GENERATED);
        modelGenerator.register(AllItems.FLAX, Models.GENERATED);
        modelGenerator.register(AllItems.FLAX_STRING, Models.GENERATED);
        modelGenerator.register(AllItems.FLUX, Models.GENERATED);
        modelGenerator.register(AllItems.GOLD_ROD, Models.GENERATED);
        modelGenerator.register(AllItems.IRON_PLATE, Models.GENERATED);
        modelGenerator.register(AllItems.IRON_ROD, Models.GENERATED);
        modelGenerator.register(AllItems.RAILROAD_TIE, Models.GENERATED);
        modelGenerator.register(AllItems.KAOLIN, Models.GENERATED);
        modelGenerator.register(AllItems.LINEN, Models.GENERATED);
        modelGenerator.register(AllItems.LINSEED_OIL, Models.GENERATED);
        modelGenerator.register(AllItems.VERMILION_INGOT, Models.GENERATED);
        modelGenerator.register(AllItems.VERMILION_ROD, Models.GENERATED);
    }

    public final void registerPassiveStraightRail(@NotNull Block rail, @NotNull BlockStateModelGenerator modelGenerator) {
        WeightedVariant railFlat = BlockStateModelGenerator.createWeightedVariant(modelGenerator.createSubModel(rail, "", Models.RAIL_FLAT, TextureMap::rail));
        WeightedVariant railRaisedNE = BlockStateModelGenerator.createWeightedVariant(modelGenerator.createSubModel(rail, "", Models.TEMPLATE_RAIL_RAISED_NE, TextureMap::rail));
        WeightedVariant railRaisedSW = BlockStateModelGenerator.createWeightedVariant(modelGenerator.createSubModel(rail, "", Models.TEMPLATE_RAIL_RAISED_SW, TextureMap::rail));
        modelGenerator.registerItemModel(rail);
        modelGenerator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(rail)
                .with(BlockStateVariantMap.models(Properties.STRAIGHT_RAIL_SHAPE).generate(shape -> {
                    return switch (shape) {
                        case NORTH_SOUTH -> railFlat;
                        case EAST_WEST -> railFlat.apply(BlockStateModelGenerator.ROTATE_Y_90);
                        case ASCENDING_EAST -> railRaisedNE.apply(BlockStateModelGenerator.ROTATE_Y_90);
                        case ASCENDING_WEST -> railRaisedSW.apply(BlockStateModelGenerator.ROTATE_Y_90);
                        case ASCENDING_NORTH -> railRaisedNE;
                        case ASCENDING_SOUTH -> railRaisedSW;
                        default -> throw new UnsupportedOperationException("Fix you generator!");
                    };
                }))
        );
    }

    public final void registerPassiveFlatRail(@NotNull Block rail, @NotNull BlockStateModelGenerator modelGenerator) {
        WeightedVariant railFlat = BlockStateModelGenerator.createWeightedVariant(modelGenerator.createSubModel(rail, "", Models.RAIL_FLAT, TextureMap::rail));
        modelGenerator.registerItemModel(rail);
        modelGenerator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(rail)
                .with(BlockStateVariantMap.models(AllProperties.FLAT_RAIL_SHAPE).generate(shape -> {
                    return switch (shape) {
                        case NORTH_SOUTH -> railFlat;
                        case EAST_WEST -> railFlat.apply(BlockStateModelGenerator.ROTATE_Y_90);
                        default -> throw new UnsupportedOperationException("Fix you generator!");
                    };
                }))
        );
    }

    public final void registerDirectionalRail(@NotNull Block rail, @NotNull BlockStateModelGenerator modelGenerator) {
        WeightedVariant railFlat = BlockStateModelGenerator.createWeightedVariant(modelGenerator.createSubModel(rail, "", Models.RAIL_FLAT, TextureMap::rail));
        WeightedVariant railRaisedNE = BlockStateModelGenerator.createWeightedVariant(modelGenerator.createSubModel(rail, "", Models.TEMPLATE_RAIL_RAISED_NE, TextureMap::rail));
        WeightedVariant railRaisedSW = BlockStateModelGenerator.createWeightedVariant(modelGenerator.createSubModel(rail, "", Models.TEMPLATE_RAIL_RAISED_SW, TextureMap::rail));
        WeightedVariant railFlatOn = BlockStateModelGenerator.createWeightedVariant(modelGenerator.createSubModel(rail, "_on", Models.RAIL_FLAT, TextureMap::rail));
        WeightedVariant railRaisedNEOn = BlockStateModelGenerator.createWeightedVariant(modelGenerator.createSubModel(rail, "_on", Models.TEMPLATE_RAIL_RAISED_NE, TextureMap::rail));
        WeightedVariant railRaisedSWOn = BlockStateModelGenerator.createWeightedVariant(modelGenerator.createSubModel(rail, "_on", Models.TEMPLATE_RAIL_RAISED_SW, TextureMap::rail));
        modelGenerator.registerItemModel(rail);
        modelGenerator.blockStateCollector.accept(
                VariantsBlockModelDefinitionCreator.of(rail)
                        .with(BlockStateVariantMap.models(Properties.POWERED, Properties.STRAIGHT_RAIL_SHAPE, Properties.HORIZONTAL_FACING).generate((powered, shape, facing) -> {
                            WeightedVariant variant = switch (shape) {
                                case ASCENDING_NORTH, ASCENDING_EAST -> powered ? railRaisedNEOn : railRaisedNE;
                                case ASCENDING_SOUTH, ASCENDING_WEST -> powered ? railRaisedSWOn : railRaisedSW;
                                case NORTH_SOUTH, EAST_WEST -> powered ? railFlatOn : railFlat;
                                default -> throw new UnsupportedOperationException("Fix you generator!");
                            };

                            if ((facing == Direction.SOUTH && shape == RailShape.ASCENDING_SOUTH) || (facing == Direction.WEST && shape == RailShape.ASCENDING_WEST))
                                variant = powered ? railRaisedNEOn : railRaisedNE;
                            else if ((facing == Direction.SOUTH && shape == RailShape.ASCENDING_NORTH) || (facing == Direction.WEST && shape == RailShape.ASCENDING_EAST))
                                variant = powered ? railRaisedSWOn : railRaisedSW;

                            return switch (facing) {
                                case NORTH -> variant;
                                case SOUTH -> variant.apply(BlockStateModelGenerator.ROTATE_Y_180);
                                case WEST -> variant.apply(BlockStateModelGenerator.ROTATE_Y_270);
                                case EAST -> variant.apply(BlockStateModelGenerator.ROTATE_Y_90);
                                default -> throw new UnsupportedOperationException("Fix you generator!");
                            };
                        }))
        );
    }
}
