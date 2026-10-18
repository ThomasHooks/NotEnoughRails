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

import com.github.thomashooks.notenoughrails.block.entity.RollingMillBlockEntity;
import com.github.thomashooks.notenoughrails.client.render.block.entity.model.RollerBlockModel;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RollingMillBlockEntityRenderer implements BlockEntityRenderer<RollingMillBlockEntity, RotatingShaftRenderState> {
    private final RollerBlockModel model;

    public RollingMillBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        model = new RollerBlockModel(context.getLayerModelPart(RollerBlockModel.LAYER_LOCATION));
    }

    @Override
    public RotatingShaftRenderState createRenderState() { return new RotatingShaftRenderState(); }

    @Override
    public void updateRenderState(RollingMillBlockEntity blockEntity, RotatingShaftRenderState state, float tickProgress, Vec3d cameraPos, ModelCommandRenderer.@Nullable CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.tickProgress = tickProgress;
        state.rotationAngle = blockEntity.getRotatingShaftProgress(tickProgress);
        state.axis = blockEntity.getAxis();
    }

    @Override
    public void render(RotatingShaftRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();

        float angle = renderState.rotationAngle;
        if (renderState.axis == Direction.Axis.Z) {
            matrices.translate(0.5F, 0.5F, 0.5F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
            matrices.translate(-0.5F, -0.5F, -0.5F);
            angle *= -1.0F;
        }
        matrices.translate(0.5F, 0.0F, 0.5F);

        queue.submitModel(model,
                angle,
                matrices, model.getLayer(RollerBlockModel.TEXTURE_LOCATION),
                renderState.lightmapCoordinates,
                OverlayTexture.DEFAULT_UV,
                0,
                renderState.crumblingOverlay
        );

        matrices.pop();
    }
}
