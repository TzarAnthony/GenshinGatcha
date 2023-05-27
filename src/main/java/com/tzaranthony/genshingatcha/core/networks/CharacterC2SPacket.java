package com.tzaranthony.genshingatcha.core.networks;

import com.tzaranthony.genshingatcha.core.capabilities.CharacterHelper;
import com.tzaranthony.genshingatcha.core.elements.Character;
import com.tzaranthony.genshingatcha.registries.GGCharacters;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CharacterC2SPacket {
    private final int mainTicks;
    private final int ultTicks;
    private final int dashTicks;

    public CharacterC2SPacket(int main, int ult, int dash) {
        this.mainTicks = main;
        this.ultTicks = ult;
        this.dashTicks = dash;
    }

    public CharacterC2SPacket(FriendlyByteBuf buf) {
        this.mainTicks = buf.readInt();
        this.ultTicks = buf.readInt();
        this.dashTicks = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.mainTicks);
        buf.writeInt(this.ultTicks);
        buf.writeInt(this.dashTicks);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Character e = GGCharacters.characterMap.get(CharacterHelper.getCharacter(player));
            if (this.mainTicks != -1) {
                CharacterHelper.setMainCooldown(this.mainTicks, player);
                e.performMainAttack(player);
            }
            if (this.ultTicks != -1) {
                CharacterHelper.setUltCooldown(this.ultTicks, player);
                e.performUltimateAttack(player);
            }
            if (this.dashTicks != -1) {
                CharacterHelper.setDashCooldown(this.dashTicks, player);
            }
        });
        return true;
    }
}