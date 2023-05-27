package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class GGKeybinds {
    public static final String GG_KEY_CAT = "key.categories." + GenshinGacha.MOD_ID;
    public static final KeyMapping DASH = new KeyMapping("key." + GenshinGacha.MOD_ID + ".dash", GLFW.GLFW_KEY_X, GG_KEY_CAT);
    public static final KeyMapping ELEMENT_ART = new KeyMapping("key." + GenshinGacha.MOD_ID + ".activate_tool_element", GLFW.GLFW_KEY_Z, GG_KEY_CAT);
    public static final KeyMapping ULT = new KeyMapping("key." + GenshinGacha.MOD_ID + ".ult", GLFW.GLFW_KEY_C, GG_KEY_CAT);


    public static void registerKeybinds() {
        ClientRegistry.registerKeyBinding(DASH);
        ClientRegistry.registerKeyBinding(ELEMENT_ART);
        ClientRegistry.registerKeyBinding(ULT);
    }
}