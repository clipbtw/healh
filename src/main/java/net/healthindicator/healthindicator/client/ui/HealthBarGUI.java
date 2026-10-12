package net.healthindicator.healthindicator.client.ui;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.healthindicator.healthindicator.config.HealthConfig;
import net.healthindicator.healthindicator.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;

import java.util.Locale;

public class HealthBarGUI {
    private static final long DISPLAY_MS = 10000L;
    private static final long FADE_MS = 1500L;
    private static final long PENDING_MS = 500L;

    private final MinecraftClient client;
    private long lastAttack = 0L;
    private PlayerEntity player = null;
    private PlayerEntity pendingTarget = null;
    private long pendingTime = 0L;
    private float pendingHealth = 0.0f;
    private int pendingHurt = 0;

    public HealthBarGUI(MinecraftClient client) {
        this.client = client;
        AttackEntityCallback.EVENT.register((p, world, hand, entity, hitResult) -> {
            if (entity.getType() == EntityType.PLAYER) {
                this.pendingTarget = (PlayerEntity) entity;
                this.pendingTime = System.currentTimeMillis();
                this.pendingHealth = this.pendingTarget.getHealth() + this.pendingTarget.getAbsorptionAmount();
                this.pendingHurt = this.pendingTarget.hurtTime;
            } else {
                this.pendingTarget = null;
            }
            return ActionResult.PASS;
        });
    }

    private void updatePending() {
        if (this.pendingTarget == null) {
            return;
        }
        float cur = this.pendingTarget.getHealth() + this.pendingTarget.getAbsorptionAmount();
        if (cur < this.pendingHealth || this.pendingTarget.hurtTime > this.pendingHurt) {
            this.player = this.pendingTarget;
            this.lastAttack = System.currentTimeMillis();
            this.pendingTarget = null;
        } else if (System.currentTimeMillis() - this.pendingTime > PENDING_MS) {
            this.pendingTarget = null;
        }
    }

    public void render(MatrixStack matrices) {
        if (!HealthConfig.enabled) {
            return;
        }
        this.updatePending();
        long age = System.currentTimeMillis() - this.lastAttack;
        if (age > DISPLAY_MS || this.player == null) {
            return;
        }
        float absorption = this.player.getAbsorptionAmount();
        float total = this.player.getHealth() + absorption;
        String prefix = absorption > 0.0f ? Formatting.BOLD.toString() : "";
        String number = HealthConfig.roundHealth
                ? String.valueOf(Math.round(total))
                : String.format(Locale.US, "%.1f", total);
        String text = ColorUtil.getColor(total).toString() + prefix + number;
        int width = this.client.textRenderer.getWidth(text);
        int alpha = 255;
        if (age > 8500L) {
            alpha = (int) ((1.0f - (float) (age - 8500L) / (float) FADE_MS) * 255.0f);
            if (alpha < 4) {
                return;
            }
        }
        int color = alpha << 24 | 0xFF0000;
        Window window = this.client.getWindow();
        float cx;
        float cy;
        if (HealthConfig.centeredCrosshair) {
            double scaleFactor = window.getScaleFactor();
            cx = (float) ((double) window.getWidth() / scaleFactor / 2.0);
            cy = (float) ((double) window.getHeight() / scaleFactor / 2.0);
        } else {
            cx = (float) window.getScaledWidth() / 2.0f;
            cy = (float) window.getScaledHeight() / 2.0f;
        }
        float scale = HealthConfig.fontScale * (total < 10.0f ? 2.0f : 1.0f);
        int x = (int) (cx / scale - (float) width / 2.0f);
        int y = (int) (cy / scale) + 4;
        matrices.push();
        matrices.translate((float) HealthConfig.offsetX, (float) HealthConfig.offsetY + 2.0f, 0.0);
        matrices.scale(scale, scale, 1.0f);
        this.client.textRenderer.drawWithShadow(matrices, text, (float) x, (float) y, color);
        matrices.pop();
    }
}
