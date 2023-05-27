package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.entities.elements.AreaFireCloud;
import com.tzaranthony.genshingatcha.core.entities.elements.FallingMeteor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GGEntities {
    public static final DeferredRegister<EntityType<?>> reg = DeferredRegister.create(ForgeRegistries.ENTITIES, GenshinGacha.MOD_ID);

    // elements main
    public static final RegistryObject<EntityType<AreaFireCloud>> FIRE_CLOUD = reg.register("fire_cloud", () ->
            EntityType.Builder.<AreaFireCloud>of(AreaFireCloud::new, MobCategory.MISC).fireImmune().sized(0.5F, 2.0F).updateInterval(Integer.MAX_VALUE).build("fire_cloud")
    );

    // elements ultimate
    public static final RegistryObject<EntityType<FallingMeteor>> METEOR = reg.register("falling_meteor", () ->
            EntityType.Builder.<FallingMeteor>of(FallingMeteor::new, MobCategory.MISC).fireImmune().sized(1.0F, 1.0F).build("falling_meteor")
    );
}