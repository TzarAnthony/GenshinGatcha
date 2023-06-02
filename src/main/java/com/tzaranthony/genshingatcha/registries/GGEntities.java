package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.entities.elements.AreaFireCloud;
import com.tzaranthony.genshingatcha.core.entities.elements.AreaFrostCloud;
import com.tzaranthony.genshingatcha.core.entities.elements.FallingMeteor;
import com.tzaranthony.genshingatcha.core.entities.projectiles.ElementalArrow;
import com.tzaranthony.genshingatcha.core.entities.projectiles.ExplodingThrownPotion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GGEntities {
    public static final DeferredRegister<EntityType<?>> reg = DeferredRegister.create(ForgeRegistries.ENTITIES, GenshinGacha.MOD_ID);

    // arrows
    public static final RegistryObject<EntityType<ElementalArrow>> ELECTRO_ARROW = reg.register("electro_arrow", () ->
            EntityType.Builder.<ElementalArrow>of(ElementalArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("electro_arrow")
    );
    public static final RegistryObject<EntityType<ExplodingThrownPotion>> EXPLODING_POTION = reg.register("exploding_potion", () ->
            EntityType.Builder.<ExplodingThrownPotion>of(ExplodingThrownPotion::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("exploding_potion")
    );

    // elements main
    public static final RegistryObject<EntityType<AreaFireCloud>> FIRE_CLOUD = reg.register("fire_cloud", () ->
            EntityType.Builder.<AreaFireCloud>of(AreaFireCloud::new, MobCategory.MISC).fireImmune().sized(0.5F, 2.0F).updateInterval(Integer.MAX_VALUE).build("fire_cloud")
    );
    public static final RegistryObject<EntityType<AreaFrostCloud>> FROST_CLOUD = reg.register("frost_cloud", () ->
            EntityType.Builder.<AreaFrostCloud>of(AreaFrostCloud::new, MobCategory.MISC).fireImmune().sized(0.5F, 2.0F).updateInterval(Integer.MAX_VALUE).build("frost_cloud")
    );

    // elements ultimate
    public static final RegistryObject<EntityType<FallingMeteor>> METEOR = reg.register("falling_meteor", () ->
            EntityType.Builder.<FallingMeteor>of(FallingMeteor::new, MobCategory.MISC).fireImmune().sized(1.0F, 1.0F).build("falling_meteor")
    );
}