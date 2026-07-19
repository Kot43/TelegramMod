package com.example.telegrammod;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import java.awt.Color;

public class TelegramGuiScreen extends Screen {
    private TextFieldWidget messageField;
    private long startTime;

    protected TelegramGuiScreen() {
        super(Text.translatable("gui.telegrammod.title"));
        this.startTime = System.currentTimeMillis();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.messageField = new TextFieldWidget(this.textRenderer, centerX - 100, centerY - 20, 200, 20, Text.literal(""));
        this.messageField.setMaxLength(256);
        this.addSelectableChild(this.messageField);
        this.setInitialFocus(this.messageField);

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.telegrammod.send"), button -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                TelegramApiHandler.sendMessage(message);
                messageField.setText("");
            }
        }).dimensions(centerX - 100, centerY + 10, 95, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            TelegramModClient.isHudPinned ? Text.translatable("gui.telegrammod.unpin") : Text.translatable("gui.telegrammod.pin"), 
            button -> {
                TelegramModClient.isHudPinned = !TelegramModClient.isHudPinned;
                button.setMessage(TelegramModClient.isHudPinned ? Text.translatable("gui.telegrammod.unpin") : Text.translatable("gui.telegrammod.pin"));
            }
        ).dimensions(centerX + 5, centerY + 10, 95, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        
        // Neon Glow Effect Logic
        float time = (System.currentTimeMillis() - startTime) / 1000f;
        int neonColor = getNeonColor(time);
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Draw neon border
        drawNeonRect(context, centerX - 110, centerY - 50, 220, 100, neonColor);
        
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, centerX, centerY - 40, 0xFFFFFF);
        this.messageField.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    private int getNeonColor(float time) {
        float hue = (time * 0.2f) % 1.0f;
        return Color.HSBtoRGB(hue, 1.0f, 1.0f);
    }

    private void drawNeonRect(DrawContext context, int x, int y, int w, int h, int color) {
        // Simple neon effect with multiple semi-transparent rectangles
        for (int i = 0; i < 3; i++) {
            int alpha = (3 - i) * 40;
            int c = (alpha << 24) | (color & 0x00FFFFFF);
            context.fill(x - i, y - i, x + w + i, y + h + i, c);
        }
        // Main border
        context.fill(x, y, x + w, y + 1, color); // Top
        context.fill(x, y + h - 1, x + w, y + h, color); // Bottom
        context.fill(x, y, x + 1, y + h, color); // Left
        context.fill(x + w - 1, y, x + w, y + h, color); // Right
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
