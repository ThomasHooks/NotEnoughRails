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
import com.github.thomashooks.notenoughrails.screen.QuernScreenHandler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class QuernScreen extends HandledScreen<QuernScreenHandler> {
    public static final Identifier SCREEN_TEXTURE = NotEnoughRails.identifier("textures/gui/container/simple_mill_gui.png");
    public static final Identifier POWER_GAUGE_TEXTURE = NotEnoughRails.identifier("textures/gui/container/power_gauge.png");
    private static final Identifier MILLING_PROGRESS_TEXTURE = Identifier.ofVanilla("container/furnace/burn_progress");

    public QuernScreen(QuernScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 153;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, SCREEN_TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
        if (this.handler.getPowerGauge() > 0.0F) {
            int textureHeight = 16;
            int textureWidth = 16;
            int gauge = MathHelper.ceil((handler.getPowerGauge()) * 16.0F);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, POWER_GAUGE_TEXTURE, this.x + 56, this.y + 41 + (textureHeight - gauge), 0, textureHeight - gauge, textureWidth, gauge, textureWidth, textureHeight);
        }
        if (this.handler.getProgressBar() > 0.0F) {
            int textureHeight = 16;
            int textureWidth = 24;
            int progress = MathHelper.ceil(handler.getProgressBar() * 24.0F);
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, MILLING_PROGRESS_TEXTURE, textureWidth, textureHeight, 0, 0, this.x + 79, this.y + 20, progress, textureHeight);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
