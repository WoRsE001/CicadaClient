package cicada.client.event.events

import cicada.client.event.CancelableEvent
import cicada.client.event.Event
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ServerboundPongPacket
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 16.04.2026 10:17.
interface EventPacket {
    object Send : CancelableEvent() {
        var packet: Packet<*> = ServerboundPongPacket(0) // заглушка
    }

    object Receive : CancelableEvent() {
        lateinit var packet: Packet<*>
    }
}

interface EventSendPos {
    object Pre: CancelableEvent() {
        var x = 0.0
        var y = 0.0
        var z = 0.0
        var ground = false

        var pos: Vec3
            get() = Vec3(x, y, z)
            set(value) {
                x = value.x
                y = value.y
                z = value.z
            }

    }

    object Post: Event
}

interface EventSendSprint {
    object Pre: CancelableEvent() {
        var wasSprinting = false
    }

    object Post: Event
}

interface EventChatMessage {
    object Send : CancelableEvent() {
        lateinit var content: String
    }

    object Receive : CancelableEvent() {
        lateinit var content: String
    }
}