package com.direwolf20.mininggadgets.common.network.Packets;

import com.direwolf20.mininggadgets.common.gadget.upgrade.Upgrade;
import com.direwolf20.mininggadgets.common.gadget.upgrade.UpgradeTools;
import com.direwolf20.mininggadgets.common.items.MiningGadget;
import com.direwolf20.mininggadgets.common.util.MiscTools;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateUpgrade {
    private final ItemStack stack;
    private final String upgrade;

    public PacketUpdateUpgrade(ItemStack stack, String upgrade) {
        this.stack = stack;
        this.upgrade = upgrade;
    }

    public static void encode(PacketUpdateUpgrade msg, PacketBuffer buffer) {
        buffer.writeItemStack(msg.stack);
        buffer.writeString(msg.upgrade);
    }

    public static PacketUpdateUpgrade decode(PacketBuffer buffer) {
        return new PacketUpdateUpgrade(buffer.readItemStack(), buffer.readString());
    }

    public static class Handler {
        public static void handle(PacketUpdateUpgrade msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                Upgrade upgrade = UpgradeTools.getUpgradeByName(msg.upgrade);
                if( upgrade == null )
                    return;

                ItemStack stack = MiscTools.getGadget(player);
                UpgradeTools.setUpgrade(stack, upgrade); //todo: change.
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
