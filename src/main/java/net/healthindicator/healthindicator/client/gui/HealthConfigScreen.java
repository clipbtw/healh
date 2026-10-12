package net.healthindicator.healthindicator.client.gui;

import net.healthindicator.healthindicator.config.HealthConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HealthConfigScreen extends Screen {
    private static final int BG_TOP = 1182244;
    private static final int BG_BOTTOM = 2366010;
    private static final int PANEL = 2168888;
    private static final int ACCENT = 11754751;
    private static final int GREEN_FILL = -9712817;
    private static final int ACCENT_FILL = -5022465;
    private static final int PANEL_W = 300;
    private static final int PANEL_H = 242;
    private static final int HEADER_H = 30;
    private static final int OFFSET_RANGE = 200;
    private static final float FONT_MIN = 0.5f;
    private static final float FONT_MAX = 3.0f;

    private final Screen parent;
    private float backgroundAlpha = 0.0f;
    private int panelLeft;
    private int panelTop;
    private int panelRight;
    private int panelBottom;
    private int headerLineY;
    private int footerLineY;

    public HealthConfigScreen(Screen parent) {
        super(new LiteralText("Health indicator"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.panelLeft = (this.width - PANEL_W) / 2;
        this.panelTop = (this.height - PANEL_H) / 2;
        this.panelRight = this.panelLeft + PANEL_W;
        this.panelBottom = this.panelTop + PANEL_H;
        this.headerLineY = this.panelTop + HEADER_H;
        int pad = 16;
        int x = this.panelLeft + pad;
        int w = PANEL_W - 2 * pad;
        int y = this.headerLineY + 12;

        this.addButton(new SimpleCheckbox(x, y, 16, new LiteralText("Включить индикатор HP"), HealthConfig.enabled, GREEN_FILL, v -> {
            HealthConfig.enabled = v;
            HealthConfig.save();
        }));
        y += 28;
        this.addButton(new SimpleCheckbox(x, y, 16, new LiteralText("Округлять здоровье"), HealthConfig.roundHealth, GREEN_FILL, v -> {
            HealthConfig.roundHealth = v;
            HealthConfig.save();
        }));
        y += 28;
        this.addButton(new OffsetSlider(x, y, w, 20, true));
        y += 24;
        this.addButton(new OffsetSlider(x, y, w, 20, false));
        y += 24;
        this.addButton(new FontSlider(x, y, w, 20));
        y += 28;
        this.addButton(new SimpleCheckbox(x, y, 16, new LiteralText("Привязка к центру прицела"), HealthConfig.centeredCrosshair, ACCENT_FILL, v -> {
            HealthConfig.centeredCrosshair = v;
            HealthConfig.save();
        }));
        y += 26;
        this.footerLineY = y;
        int gap = 8;
        int half = (w - gap) / 2;
        y += 10;
        this.addButton(new ButtonWidget(x, y, half, 20,
                new LiteralText("\u27f2 Сбросить").formatted(Formatting.GOLD), b -> {
            HealthConfig.reset();
            this.init(this.client, this.width, this.height);
        }));
        this.addButton(new ButtonWidget(x + half + gap, y, half, 20,
                new LiteralText("\u2716 Закрыть").formatted(Formatting.RED), b -> this.onClose()));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.backgroundAlpha = Math.min(1.0f, this.backgroundAlpha + delta * 0.15f);
        this.fillGradient(matrices, 0, 0, this.width, this.height, this.argb(0.55f, BG_TOP), this.argb(0.65f, BG_BOTTOM));
        fill(matrices, this.panelLeft, this.panelTop, this.panelRight, this.panelBottom, this.argb(0.95f, PANEL));
        fill(matrices, this.panelLeft, this.panelTop, this.panelRight, this.panelTop + 1, this.argb(1.0f, ACCENT));
        fill(matrices, this.panelLeft, this.panelBottom - 1, this.panelRight, this.panelBottom, this.argb(1.0f, ACCENT));
        fill(matrices, this.panelLeft, this.panelTop, this.panelLeft + 1, this.panelBottom, this.argb(1.0f, ACCENT));
        fill(matrices, this.panelRight - 1, this.panelTop, this.panelRight, this.panelBottom, this.argb(1.0f, ACCENT));
        fill(matrices, this.panelLeft, this.headerLineY, this.panelRight, this.headerLineY + 2, this.argb(1.0f, ACCENT));
        fill(matrices, this.panelLeft, this.footerLineY, this.panelRight, this.footerLineY + 2, this.argb(1.0f, ACCENT));
        Text title = new LiteralText("Health Indicator").formatted(Formatting.BOLD);
        drawCenteredText(matrices, this.textRenderer, title, (this.panelLeft + this.panelRight) / 2, this.panelTop + 11, -1);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private int argb(float a, int rgb) {
        int alpha = (int) (this.backgroundAlpha * a * 255.0f);
        return Math.min(255, Math.max(0, alpha)) << 24 | rgb & 0xFFFFFF;
    }

    @Override
    public void onClose() {
        HealthConfig.save();
        if (this.client != null) {
            this.client.openScreen(this.parent);
        } else {
            super.onClose();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static final class OffsetSlider extends SliderWidget {
        private final boolean xAxis;

        OffsetSlider(int x, int y, int width, int height, boolean xAxis) {
            super(x, y, width, height, LiteralText.EMPTY, toSliderValue(xAxis ? HealthConfig.offsetX : HealthConfig.offsetY));
            this.xAxis = xAxis;
            this.updateMessage();
        }

        private static double toSliderValue(int offset) {
            return (double) (offset + OFFSET_RANGE) / (double) (OFFSET_RANGE * 2);
        }

        private int currentOffset() {
            return (int) Math.round(this.value * (OFFSET_RANGE * 2) - OFFSET_RANGE);
        }

        @Override
        protected void updateMessage() {
            String label = this.xAxis ? "Смещение X: " : "Смещение Y: ";
            this.setMessage(new LiteralText(label + this.currentOffset()));
        }

        @Override
        protected void applyValue() {
            if (this.xAxis) {
                HealthConfig.offsetX = this.currentOffset();
            } else {
                HealthConfig.offsetY = this.currentOffset();
            }
            HealthConfig.save();
        }
    }

    private static final class FontSlider extends SliderWidget {
        FontSlider(int x, int y, int width, int height) {
            super(x, y, width, height, LiteralText.EMPTY, (double) ((HealthConfig.fontScale - FONT_MIN) / (FONT_MAX - FONT_MIN)));
            this.updateMessage();
        }

        private float currentScale() {
            float scale = (float) (FONT_MIN + this.value * (FONT_MAX - FONT_MIN));
            return (float) Math.round(scale * 10.0f) / 10.0f;
        }

        @Override
        protected void updateMessage() {
            this.setMessage(new LiteralText("Размер шрифта: " + this.currentScale() + "x"));
        }

        @Override
        protected void applyValue() {
            HealthConfig.fontScale = this.currentScale();
            HealthConfig.save();
        }
    }
}
