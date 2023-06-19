package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.blocks.ChallengeChest;
import com.tzaranthony.genshingatcha.core.blocks.GatchaMachine;
import com.tzaranthony.genshingatcha.core.items.ModeledBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class GGBlocks {
    public static final DeferredRegister<Block> reg = DeferredRegister.create(ForgeRegistries.BLOCKS, GenshinGacha.MOD_ID);

    public static final RegistryObject<Block> GATCHA_MACHINE = registerBlockAndItem("gatcha_machine", () -> new GatchaMachine(Standard(1.5F, 6.0F)));
    public static final RegistryObject<Block> CHALLENGE_CHEST = registerBlockAndModelItem("challenge_chest", () -> new ChallengeChest(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)));
    //TODO: domain summoning block -- like stone shaped. (fee to use?) starts a domain event, should I make a domain entity or just use the chest and check monster proximity? if you leave the enemies depspawn and you get no refund
    //TODO: add resin tree that gives resin for domains?

    public static BlockBehaviour.Properties Standard(float hardness, float resistance) {
        return BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.TUFF).strength(hardness, resistance);
    }

    public static RegistryObject<Block> registerBlockAndItem(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = reg.register(name, block);
        GGItems.reg.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties().tab(GenshinGacha.TAB)));
        return blockObj;
    }

    public static RegistryObject<Block> registerBlockAndModelItem(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = reg.register(name, block);
        GGItems.reg.register(name, () -> new ModeledBlockItem(blockObj.get(), new Item.Properties().tab(GenshinGacha.TAB)));
        return blockObj;
    }
}