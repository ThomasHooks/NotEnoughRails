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
package com.github.thomashooks.notenoughrails.client.gui.screen.ingame;

import com.github.thomashooks.notenoughrails.NotEnoughRails;
import com.github.thomashooks.notenoughrails.screen.RefractoryFurnaceScreenHandler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class RefractoryFurnaceScreen extends HandledScreen<RefractoryFurnaceScreenHandler> {
    public static final Identifier TEXTURE = NotEnoughRails.identifier("textures/gui/container/refractory_furnace_gui.png");
    public static final Identifier LIT_PROGRESS_TEXTURE =Identifier.ofVanilla("container/furnace/lit_progress");
    private static final Identifier COOKING_PROGRESS_TEXTURE = Identifier.ofVanilla("container/furnace/burn_progress");

    public RefractoryFurnaceScreen(RefractoryFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
        if (this.handler.isBurning()) {
            int litTime = MathHelper.ceil((handler.getFuelProgress()) * 13.0F) + 1;
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, LIT_PROGRESS_TEXTURE, 14, 14, 0, 14 - litTime, this.x + 56, this.y + 36 + 14 - litTime, 14, litTime);
        }
        if (this.handler.getCookProgress() > 0.0F) {
            int progress = MathHelper.ceil(handler.getCookProgress() * 24.0F);
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, COOKING_PROGRESS_TEXTURE, 24, 16, 0, 0, this.x + 79, this.y + 34, progress, 16);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
