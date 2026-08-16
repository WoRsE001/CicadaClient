package cicada.client.mixin;

import cicada.client.event.events.EventPacket;
import cicada.client.packethandle.PacketHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// SCWGxD regrets everything he did. 16.04.2026 8:43.
@Mixin(Connection.class)
public class MixinConnection {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void callSendPacketEvent(Packet<?> packet, CallbackInfo ci) {
        if (Minecraft.getInstance().getConnection() == null)
            return;

        EventPacket.Send.INSTANCE.setPacket(packet);
        EventPacket.Send.INSTANCE.call();

        if (!EventPacket.Send.INSTANCE.getCanceled())
            PacketHandler.INSTANCE.handlePacket(packet);

        ci.cancel();
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;genericsFtw(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;)V", shift = At.Shift.BEFORE), cancellable = true)
    private static void callReceivePacketEvent(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
        EventPacket.Receive packetEvent = EventPacket.Receive.INSTANCE;
        packetEvent.setPacket(packet);
        packetEvent.call();

        if (packetEvent.getCanceled())
            ci.cancel();
    }
}
