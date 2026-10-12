package net.healthindicator.healthindicator.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.healthindicator.healthindicator.client.gui.HealthConfigScreen;
import net.healthindicator.healthindicator.config.HealthConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class HealthindicatorClient implements ClientModInitializer {
    // Добавлено при переносе: клавиша для открытия настроек (если нет ModMenu).
    private static KeyBinding openConfig;

    @Override
    public void onInitializeClient() {
        HealthConfig.load();
        openConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.healthindicator.config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, "key.categories.healthindicator"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfig.wasPressed()) {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.currentScreen == null) {
                    mc.openScreen(new HealthConfigScreen(null));
                }
            }
        });
    }
}
