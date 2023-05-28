package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.client.screens.GatchaMachineScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class GGScreenRender {
    public static void renderScreens() {
        MenuScreens.register(GGMenus.GATCHA_MACHINE.get(), GatchaMachineScreen::new);
    }
}