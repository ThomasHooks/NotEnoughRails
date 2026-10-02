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

Made with Blockbench 5.2.1
 */
package com.github.thomashooks.notenoughrails.client.render.block.entity.model;

import com.github.thomashooks.notenoughrails.NotEnoughRails;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class QuernBlockModel extends Model<Float> {
	private final ModelPart main;
	private static final String MAIN = "main";
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(NotEnoughRails.identifier("quern"), MAIN);
	public static final Identifier TEXTURE_LOCATION = NotEnoughRails.identifier("textures/block/quern_ber.png");

	public QuernBlockModel(ModelPart root) {
		super(root, RenderLayers::entitySolid);

		this.main = root.getChild(MAIN);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild(MAIN,
				ModelPartBuilder
						.create()
						.uv(0, 21)
						.cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 16.0F, 4.0F, new Dilation(0.0F))
						.uv(0, 0)
						.cuboid(-8.0F, 0.0F, -8.0F, 16.0F, 4.0F, 16.0F, new Dilation(0.0F)),
				ModelTransform.origin(0.0F, 8.0F, 0.0F)
		);
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(Float angle) {
		super.setAngles(angle);
		this.main.yaw = (angle / 180.0F) * (float) Math.PI;
	}
}