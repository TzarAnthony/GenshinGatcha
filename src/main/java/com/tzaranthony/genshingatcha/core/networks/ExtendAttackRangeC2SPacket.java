package com.tzaranthony.genshingatcha.core.networks;

import com.tzaranthony.genshingatcha.core.items.util.IAttackReachExtending;
import com.tzaranthony.genshingatcha.core.util.tags.GGItemTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExtendAttackRangeC2SPacket {
    private final int tgtID;

    public ExtendAttackRangeC2SPacket(int tgtID) {
        this.tgtID = tgtID;
    }

    public ExtendAttackRangeC2SPacket(FriendlyByteBuf buf) {
        this.tgtID = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.tgtID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        ServerPlayer sPlayer = supplier.get().getSender();
        if (sPlayer == null) {
            return false;
        }

        Entity target = sPlayer.getLevel().getEntity(this.tgtID);
        ItemStack holding = sPlayer.getMainHandItem();
        if (target instanceof LivingEntity le && !sPlayer.isSpectator() && (holding.is(GGItemTags.CLAYMORES) || holding.is(GGItemTags.SPEARS))) {
            IAttackReachExtending reachItem = (IAttackReachExtending) holding.getItem();
            double d0 = reachItem.getAttackReach();

            double d1 = sPlayer.distanceToSqr(le);
            if (d1 < d0 * d0) {
                sPlayer.attack(le);
            }
        }
        return true;
    }
}