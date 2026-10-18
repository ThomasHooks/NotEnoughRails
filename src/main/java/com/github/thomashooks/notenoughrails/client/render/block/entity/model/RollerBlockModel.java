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

Made with Blockbench 5.2.2
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
public class RollerBlockModel extends Model<Float> {
	private final ModelPart main;
	private final ModelPart bottom;
	private final ModelPart top;
	private static final String MAIN = "main";
	private static final String BOTTOM = "bottom";
	private static final String TOP = "top";
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(NotEnoughRails.identifier("roller"), MAIN);
	public static final Identifier TEXTURE_LOCATION = NotEnoughRails.identifier("textures/block/roller_ber.png");

	public RollerBlockModel(ModelPart root) {
		super(root, RenderLayers::entitySolid);

        this.main = root.getChild(MAIN);
		this.bottom = this.main.getChild(BOTTOM);
		this.top = this.main.getChild(TOP);
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData main = modelPartData.addChild(MAIN, ModelPartBuilder.create(), ModelTransform.origin(0.0F, 10.25F, 0.0F));
		main.addChild(BOTTOM,
				ModelPartBuilder
						.create()
						.uv(0, 0)
						.cuboid(-8.0F, -2.0F, -2.0F, 16.0F, 4.0F, 4.0F, new Dilation(0.0F)),
				ModelTransform.origin(0.0F, -2.25F, 0.0F)
		);
		main.addChild(TOP,
				ModelPartBuilder
						.create()
						.uv(0, 9)
						.cuboid(-5.0F, -2.0F, -2.0F, 10.0F, 4.0F, 4.0F, new Dilation(0.0F)),
				ModelTransform.origin(0.0F, 2.25F, 0.0F)
		);
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(Float angle) {
		super.setAngles(angle);
		this.bottom.pitch = (angle / 180.0F) * (float) Math.PI;
		this.top.pitch = (-angle / 180.0F) * (float) Math.PI;
	}
}