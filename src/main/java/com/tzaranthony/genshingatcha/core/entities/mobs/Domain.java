package com.tzaranthony.genshingatcha.core.entities.mobs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Domain extends Entity implements ElementalEntity {
    //started central block position
    //entites summoned
    //difficulty (determines rewards & what is summoned)
    //players active (must have provided resin)
    //max waves
    //wave count
    //wave spawn position

    public Domain(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return null;
    }

    @Override
    public void setElement(int elementId) {

    }

    @Override
    public int getElement() {
        return 0;
    }
}