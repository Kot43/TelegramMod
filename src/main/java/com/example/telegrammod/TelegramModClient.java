package com.example.telegrammod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class TelegramModClient implements ClientModInitializer {
    public static KeyBinding openGuiKey;
    public static boolean isHudPinned = false;
    public static String telegramToken = "8815457650:AAEIshgwaQXMYM_95TDxmbNPyeJ51FNhGtE";
    public static String ownerId = "6024048993";

    @Override
    public void onInitializeClient() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.telegrammod.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "category.telegrammod.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                client.setScreen(new TelegramGuiScreen());
            }
        });

        net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (isHudPinned) {
                renderPinnedHud(context);
            }
        });

        // Notify that the mod has started
        TelegramApiHandler.sendMessage("🚀 Neon Telegram Mod started for " + net.minecraft.client.MinecraftClient.getInstance().getSession().getUsername());
    }

    private void renderPinnedHud(net.minecraft.client.gui.DrawContext context) {
        int x = 10;
        int y = 10;
        int color = 0xFF00FFCC; // Neon Cyan
        context.fill(x, y, x + 80, y + 20, 0x88000000);
        context.drawBorder(x, y, 80, 20, color);
        context.drawTextWithShadow(net.minecraft.client.MinecraftClient.getInstance().textRenderer, "TG: Active", x + 5, y + 6, color);
    }
}
