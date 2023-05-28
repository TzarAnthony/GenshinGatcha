package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.blockEntities.GatchaMachineBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GGBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> reg = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, GenshinGacha.MOD_ID);

    public static final RegistryObject<BlockEntityType<GatchaMachineBE>> GATCHA_MACHINE = reg.register("gatcha_machine",
            () -> BlockEntityType.Builder.of(GatchaMachineBE::new, GGBlocks.GATCHA_MACHINE.get()).build(null));
}