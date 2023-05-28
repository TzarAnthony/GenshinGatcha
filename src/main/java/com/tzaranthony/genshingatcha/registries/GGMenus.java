package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.container.GachaMachineMenu;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class GGMenus {
    public static final DeferredRegister<MenuType<?>> reg = DeferredRegister.create(Registry.MENU_REGISTRY, GenshinGacha.MOD_ID);

    public static final RegistryObject<MenuType<GachaMachineMenu>> GATCHA_MACHINE = reg.register("gatcha_machine", () -> IForgeMenuType.create((id, inv, data) -> new GachaMachineMenu(id, inv, data.readBlockPos())));
}