package cicada.client.utils.player

import cicada.client.event.Event
import cicada.client.event.EventListener
import cicada.client.event.impl.AttackEvent
import cicada.client.event.impl.PacketEvent
import cicada.client.event.impl.PlayerStateUpdateEvent
import cicada.client.utils.client.player
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket

// SCWGxD regrets everything he did. 06.07.2026 8:54.
object LocalPlayerStateTracker : EventListener {
    var serverSprint = false
    var groundTicks = 0
    var airTicks = 0

    override fun onEvent(event: Event) {
        if (event is PacketEvent.Send) {
            val packet = event.packet

            if (packet is ServerboundPlayerCommandPacket) {
                if (packet.action == ServerboundPlayerCommandPacket.Action.START_SPRINTING) {
                    serverSprint = true
                } else if (packet.action == ServerboundPlayerCommandPacket.Action.STOP_SPRINTING) {
                    serverSprint = false
                }
            }
        }

        if (event is AttackEvent.Post) {
            serverSprint = false
        }

        if (event is PlayerStateUpdateEvent.Pre) {
            if (player.onGround()) {
                groundTicks++
                airTicks = 0
            } else {
                groundTicks = 0
                airTicks++
            }
        }
    }
}