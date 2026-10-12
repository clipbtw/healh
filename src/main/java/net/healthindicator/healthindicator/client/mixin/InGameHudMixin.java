package net.healthindicator.healthindicator.client.mixin;

import net.healthindicator.healthindicator.client.ui.HealthBarGUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    private final HealthBarGUI hud = new HealthBarGUI(MinecraftClient.getInstance());

    @Inject(method = "render", at = @At("TAIL"))
    private void healthindicator$render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        this.hud.render(matrices);
    }
}
