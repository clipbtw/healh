package net.healthindicator.healthindicator.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.healthindicator.healthindicator.client.gui.HealthConfigScreen;

public class HealthModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return HealthConfigScreen::new;
    }
}
