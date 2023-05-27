package com.tzaranthony.genshingatcha.core.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CharacterProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<CharacterServer> CHARACTER = CapabilityManager.get(new CapabilityToken<CharacterServer>() {});
    private CharacterServer character = null;
    private final LazyOptional<CharacterServer> optCharacter = LazyOptional.of(this::getOrCreateCharacter);
    private CharacterServer getOrCreateCharacter() {
        if (this.character == null) {
            this.character = new CharacterServer();
        }
        return this.character;
    }


    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CHARACTER) {
            return optCharacter.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        getOrCreateCharacter().saveElementInfo(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        getOrCreateCharacter().loadElementInfo(tag);
    }
}