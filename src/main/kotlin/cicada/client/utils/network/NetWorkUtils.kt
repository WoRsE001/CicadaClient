package cicada.client.utils.network

import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.network.protocol.Packet

// SCWGxD regrets everything he did. 21.04.2026 10:01.
fun ClientPacketListener.sendInvisiblePacket(packet: Packet<*>) {
    this.connection.send(packet, null)
}