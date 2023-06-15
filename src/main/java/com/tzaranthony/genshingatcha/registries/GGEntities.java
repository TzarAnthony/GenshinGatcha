package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.entities.elements.character.FallingMeteorZhongLi;
import com.tzaranthony.genshingatcha.core.entities.elements.character.FireCloudDiluc;
import com.tzaranthony.genshingatcha.core.entities.elements.character.FrostCloudQiqi;
import com.tzaranthony.genshingatcha.core.entities.elements.mobs.*;
import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.ElementalArrow;
import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.ExplodingThrownPotion;
import com.tzaranthony.genshingatcha.core.entities.mobs.slimes.*;
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
    public static final RegistryObject<EntityType<ExplodingThrownPotion>> EXPLODING_POTION = reg.register("exploding_potion", () ->
            EntityType.Builder.<ExplodingThrownPotion>of(ExplodingThrownPotion::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("exploding_potion")
    );
    public static final RegistryObject<EntityType<ElementalArrow>> ELEMENTAL_ARROW = reg.register("elemental_arrow", () ->
            EntityType.Builder.<ElementalArrow>of(ElementalArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("elemental_arrow")
    );
    public static final RegistryObject<EntityType<PoisonDendroSeed>> DENDRO_SLIME_SEED = reg.register("dendro_seed", () ->
            EntityType.Builder.<PoisonDendroSeed>of(PoisonDendroSeed::new, MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(20).build("dendro_seed")
    );
    public static final RegistryObject<EntityType<Pyroball>> PYROBALL = reg.register("pyroball", () ->
            EntityType.Builder.<Pyroball>of(Pyroball::new, MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(20).build("pyroball")
    );
    public static final RegistryObject<EntityType<RockProjectile>> GEO_ROCK = reg.register("geo_rock", () ->
            EntityType.Builder.<RockProjectile>of(RockProjectile::new, MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(20).build("geo_rock")
    );
    public static final RegistryObject<EntityType<Waterball>> WATERBALL = reg.register("waterball", () ->
            EntityType.Builder.<Waterball>of(Waterball::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(20).build("waterball")
    );
    public static final RegistryObject<EntityType<Windball>> WIND_BALL = reg.register("windball", () ->
            EntityType.Builder.<Windball>of(Windball::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(20).build("windball")
    );
    //HYDRO WATER BALL


    // elements area effect
    public static final RegistryObject<EntityType<FireCloudDiluc>> FIRE_CLOUD = reg.register("fire_cloud", () ->
            EntityType.Builder.<FireCloudDiluc>of(FireCloudDiluc::new, MobCategory.MISC).fireImmune().sized(0.5F, 2.0F).updateInterval(Integer.MAX_VALUE).build("fire_cloud")
    );
    public static final RegistryObject<EntityType<FrostCloudQiqi>> FROST_CLOUD = reg.register("frost_cloud", () ->
            EntityType.Builder.<FrostCloudQiqi>of(FrostCloudQiqi::new, MobCategory.MISC).fireImmune().sized(0.5F, 2.0F).updateInterval(Integer.MAX_VALUE).build("frost_cloud")
    );
    public static final RegistryObject<EntityType<FallingMeteorZhongLi>> METEOR = reg.register("falling_meteor", () ->
            EntityType.Builder.<FallingMeteorZhongLi>of(FallingMeteorZhongLi::new, MobCategory.MISC).fireImmune().sized(1.0F, 1.0F).build("falling_meteor")
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