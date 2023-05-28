package com.tzaranthony.genshingatcha.core.networks;

import com.tzaranthony.genshingatcha.core.blockEntities.GatchaMachineBE;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ItemS2CPacket {
    private ItemStackHandler items;
    private BlockPos pos;

    public ItemS2CPacket(ItemStackHandler items, BlockPos pos) {
        this.items = items;
        this.pos = pos;
    }

    public ItemS2CPacket(FriendlyByteBuf buf) {
        read(buf);
    }

    public void read(FriendlyByteBuf buf) {
        List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        items = new ItemStackHandler(collection.size());
        for (int i = 0; i < collection.size(); i++) {
            items.insertItem(i, collection.get(i), false);
        }

        this.pos = buf.readBlockPos();
    }

    public void write(FriendlyByteBuf buf) {
        Collection<ItemStack> list = new ArrayList<>();
        for(int i = 0; i < items.getSlots(); i++) {
            list.add(items.getStackInSlot(i));
        }

        buf.writeCollection(list, FriendlyByteBuf::writeItem);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof GatchaMachineBE be) {
                be.setItemHandler(this.items);
            }
        });
        return true;
    }
}