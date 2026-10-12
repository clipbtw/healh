package net.healthindicator.healthindicator.util;

import net.minecraft.util.Formatting;

public class ColorUtil {
    public static Formatting getColor(float health) {
        if (health < 1.0f) {
            return Formatting.BLACK;
        }
        if (health <= 4.0f) {
            return Formatting.DARK_RED;
        }
        if (health <= 8.0f) {
            return Formatting.RED;
        }
        if (health <= 12.0f) {
            return Formatting.GOLD;
        }
        if (health <= 16.0f) {
            return Formatting.YELLOW;
        }
        return Formatting.GREEN;
    }
}
