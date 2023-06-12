package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.entities.elements.AreaFireCloud;
import com.tzaranthony.genshingatcha.core.entities.elements.AreaFrostCloud;
import com.tzaranthony.genshingatcha.core.entities.elements.FallingMeteor;
import com.tzaranthony.genshingatcha.core.entities.mobs.slimes.*;
import com.tzaranthony.genshingatcha.core.entities.projectiles.ElementalArrow;
import com.tzaranthony.genshingatcha.core.entities.projectiles.ExplodingThrownPotion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = GenshinGacha.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GGEntities {
    public static final DeferredRegister<EntityType<?>> reg = DeferredRegister.create(ForgeRegistries.ENTITIES, GenshinGacha.MOD_ID);

    // mobs
    public static final RegistryObject<EntityType<CryoSlime>> CRYO_SLIME = reg.register("cryo_slime", () ->
            EntityType.Builder.of(CryoSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build("cryo_slime")
    );
    public static final RegistryObject<EntityType<PyroSlime>> PYRO_SLIME = reg.register("pyro_slime", () ->
            EntityType.Builder.of(PyroSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build("pyro_slime")
    );
    public static final RegistryObject<EntityType<ElectroSlime>> ELECTRO_SLIME = reg.register("electro_slime", () ->
            EntityType.Builder.of(ElectroSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build("electro_slime")
    );
    public static final RegistryObject<EntityType<GeoSlime>> GEO_SLIME = reg.register("geo_slime", () ->
            EntityType.Builder.of(GeoSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build("geo_slime")
    );
    public static final RegistryObject<EntityType<HydroSlime>> HYDRO_SLIME = reg.register("hydro_slime", () ->
            EntityType.Builder.of(HydroSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build("hydro_slime")
    );
    public static final RegistryObject<EntityType<DendroSlime>> DENDRO_SLIME = reg.register("dendro_slime", () ->
            EntityType.Builder.of(DendroSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build("dendro_slime")
    );
    public static final RegistryObject<EntityType<AnemoSlime>> ANEMO_SLIME = reg.register("anemo_slime", () ->
            EntityType.Builder.of(AnemoSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build("anemo_slime")
    );

    // arrows
    public static final RegistryObject<EntityType<ElementalArrow>> ELEMENTAL_ARROW = reg.register("elemental_arrow", () ->
            EntityType.Builder.<ElementalArrow>of(ElementalArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("elemental_arrow")
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

    @SubscribeEvent
    public static void bakeAttributes(EntityAttributeCreationEvent creationEvent) {
        creationEvent.put(CRYO_SLIME.get(), Monster.createMonsterAttributes().build());
        creationEvent.put(PYRO_SLIME.get(), Monster.createMonsterAttributes().build());
        creationEvent.put(ELECTRO_SLIME.get(), Monster.createMonsterAttributes().build());
        creationEvent.put(GEO_SLIME.get(), Monster.createMonsterAttributes().build());
        creationEvent.put(HYDRO_SLIME.get(), Monster.createMonsterAttributes().build());
        creationEvent.put(DENDRO_SLIME.get(), Monster.createMonsterAttributes().build());
        creationEvent.put(ANEMO_SLIME.get(), Monster.createMonsterAttributes().build());
    }
}