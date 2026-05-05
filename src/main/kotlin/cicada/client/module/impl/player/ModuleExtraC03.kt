package cicada.client.module.impl.player

import cicada.client.event.Event
import cicada.client.event.impl.PacketEvent
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.utils.connection
import cicada.client.utils.network.sendInvisiblePacket
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

// SCWGxD regrets everything he did. 29.04.2026 11:17.
object ModuleExtraC03 : Module("ExtraC03", Category.PLAYER) {
    override fun onEvent(event: Event) {
        if (event is PacketEvent.Send) {
            val packet = event.packet
            if (packet is ServerboundMovePlayerPacket) {
                connection.sendInvisiblePacket(packet)
            }
        }
    }
}