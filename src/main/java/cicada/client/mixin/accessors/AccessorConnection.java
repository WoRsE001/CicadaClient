package cicada.client.mixin.accessors;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// SCWGxD regrets everything he did. 06.07.2026 12:21.
@Mixin(Connection.class)
public interface AccessorConnection {
    @Invoker("genericsFtw")
    <T extends PacketListener> void invokeGenericsFtw(final Packet<T> packet, final PacketListener listener);
}