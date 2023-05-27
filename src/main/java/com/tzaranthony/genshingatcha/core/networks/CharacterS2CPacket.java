package com.tzaranthony.genshingatcha.core.networks;

import com.tzaranthony.genshingatcha.core.capabilities.CharacterClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CharacterS2CPacket {
    private final int charID;
    private final int constRank;
    private final int mainTicks;
    private final int ultTicks;
    private final int dashTicks;

    public CharacterS2CPacket(int charID, int constRank, int main, int ult, int dash) {
        this.charID = charID;
        this.constRank = constRank;
        this.mainTicks = main;
        this.ultTicks = ult;
        this.dashTicks = dash;
    }

    public CharacterS2CPacket(FriendlyByteBuf buf) {
        this.charID = buf.readInt();
        this.constRank = buf.readInt();
        this.mainTicks = buf.readInt();
        this.ultTicks = buf.readInt();
        this.dashTicks = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.charID);
        buf.writeInt(this.constRank);
        buf.writeInt(this.mainTicks);
        buf.writeInt(this.ultTicks);
        buf.writeInt(this.dashTicks);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            CharacterClient.setElementFromServer(this.charID);
            CharacterClient.setConstRankFromServer(this.constRank);
            CharacterClient.setMainFromServer(this.mainTicks);
            CharacterClient.setUltFromServer(this.ultTicks);
            CharacterClient.setDashFromServer(this.dashTicks);
        });
        return true;
    }
}