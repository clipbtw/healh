package net.healthindicator.healthindicator.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class SimpleCheckbox extends AbstractButtonWidget {
    private boolean checked;
    private final Consumer<Boolean> onChange;
    private final int fillColor;

    public SimpleCheckbox(int x, int y, int size, Text label, boolean checked, int fillColor, Consumer<Boolean> onChange) {
        super(x, y, size, size, label);
        this.checked = checked;
        this.fillColor = fillColor;
        this.onChange = onChange;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setCheckedSilently(boolean value) {
        this.checked = value;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.checked = !this.checked;
        if (this.onChange != null) {
            this.onChange.accept(this.checked);
        }
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int size = this.height;
        int x = this.x;
        int y = this.y;
        int border = this.isHovered() ? -1 : -6250336;
        int bg = -14803426;
        fill(matrices, x, y, x + size, y + size, bg);
        fill(matrices, x, y, x + size, y + 1, border);
        fill(matrices, x, y + size - 1, x + size, y + size, border);
        fill(matrices, x, y, x + 1, y + size, border);
        fill(matrices, x + size - 1, y, x + size, y + size, border);
        if (this.checked) {
            int pad = Math.max(2, size / 4);
            fill(matrices, x + pad, y + pad, x + size - pad, y + size - pad, this.fillColor);
        }
        if (!this.getMessage().getString().isEmpty()) {
            int tx = x + size + 6;
            int ty = y + (size - 8) / 2;
            drawTextWithShadow(matrices, MinecraftClient.getInstance().textRenderer, this.getMessage(), tx, ty, -1);
        }
    }
}
