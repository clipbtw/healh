package net.healthindicator.healthindicator.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HealthConfig {
    public static boolean enabled = true;
    public static int offsetX = 0;
    public static int offsetY = 0;
    public static boolean centeredCrosshair = false;
    public static boolean roundHealth = false;
    public static float fontScale = 1.0f;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("healthindicator.json");

    public static void load() {
        try {
            if (!Files.exists(PATH)) {
                save();
                return;
            }
            Data d = GSON.fromJson(new String(Files.readAllBytes(PATH), StandardCharsets.UTF_8), Data.class);
            if (d != null) {
                enabled = d.enabled;
                offsetX = d.offsetX;
                offsetY = d.offsetY;
                centeredCrosshair = d.centeredCrosshair;
                roundHealth = d.roundHealth;
                fontScale = d.fontScale;
            }
        } catch (Exception ignored) {
        }
    }

    public static void save() {
        try {
            Data d = new Data();
            d.enabled = enabled;
            d.offsetX = offsetX;
            d.offsetY = offsetY;
            d.centeredCrosshair = centeredCrosshair;
            d.roundHealth = roundHealth;
            d.fontScale = fontScale;
            Files.write(PATH, GSON.toJson(d).getBytes(StandardCharsets.UTF_8));
        } catch (Exception ignored) {
        }
    }

    public static void reset() {
        enabled = true;
        offsetX = 0;
        offsetY = 0;
        centeredCrosshair = false;
        roundHealth = false;
        fontScale = 1.0f;
        save();
    }

    private static final class Data {
        boolean enabled;
        int offsetX;
        int offsetY;
        boolean centeredCrosshair;
        boolean roundHealth;
        float fontScale;
    }
}
