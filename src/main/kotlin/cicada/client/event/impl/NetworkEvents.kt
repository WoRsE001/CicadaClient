package cicada.client.event.impl

import cicada.client.event.CancelableEvent
import cicada.client.event.Event
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ServerboundPongPacket
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 16.04.2026 10:17.
interface PacketEvent {
    object Send : CancelableEvent() {
        var packet: Packet<*> = ServerboundPongPacket(0) // заглушка
    }

    data class Receive(val packet: Packet<*>) : CancelableEvent()
}

interface SendPosEvent {
    object Pre: CancelableEvent() {
        var x = 0f
        var y = 0f
        var z = 0f
        var ground = false

        fun setPos(pos: Vec3) {
            x = pos.x.toFloat()
            y = pos.y.toFloat()
            z = pos.z.toFloat()
        }
    }

    object Post: Event
}