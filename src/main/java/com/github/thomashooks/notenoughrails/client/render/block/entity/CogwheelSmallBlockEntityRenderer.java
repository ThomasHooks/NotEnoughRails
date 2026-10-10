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
package com.github.thomashooks.notenoughrails.client.render.block.entity;

import com.github.thomashooks.notenoughrails.block.entity.CogwheelSmallBlockEntity;
import com.github.thomashooks.notenoughrails.client.render.block.entity.model.CogwheelSmallBlockModel;
import com.github.thomashooks.notenoughrails.client.render.block.entity.state.RotatingShaftRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class CogwheelSmallBlockEntityRenderer implements BlockEntityRenderer<CogwheelSmallBlockEntity, RotatingShaftRenderState> {
    private final CogwheelSmallBlockModel model;

    public CogwheelSmallBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.model = new CogwheelSmallBlockModel(context.getLayerModelPart(CogwheelSmallBlockModel.LAYER_LOCATION));
    }

    @Override
    public RotatingShaftRenderState createRenderState() { return new RotatingShaftRenderState(); }

    @Override
    public void updateRenderState(CogwheelSmallBlockEntity blockEntity, RotatingShaftRenderState state, float tickProgress, Vec3d cameraPos, ModelCommandRenderer.@Nullable CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.tickProgress = tickProgress;
        state.rotationAngle = blockEntity.getRotatingShaftProgress(tickProgress);
        state.axis = blockEntity.getCachedState().get(Properties.AXIS);
    }

    @Override
    public void render(RotatingShaftRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();

        if (renderState.axis != Direction.Axis.Y) {
            matrices.translate(0.5F, 0.5F, 0.5F);
            switch (renderState.axis) {
                case X:
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0F));
                    break;
                case Z:
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
                    break;
                case Y:
                    break;
            }
            matrices.translate(-0.5F, -0.5F, -0.5F);
        }
        matrices.translate(0.5F, 0.0F, 0.5F);
        queue.submitModel(model,
                renderState.rotationAngle,
                matrices, model.getLayer(CogwheelSmallBlockModel.TEXTURE_LOCATION),
                renderState.lightmapCoordinates,
                OverlayTexture.DEFAULT_UV,
                0,
                renderState.crumblingOverlay
        );

        matrices.pop();
    }
}
