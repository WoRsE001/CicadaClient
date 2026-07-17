package cicada.client.feature.module.modules.combat.sprintreset.mode

import cicada.client.event.Event
import cicada.client.event.impl.PacketEvent
import cicada.client.event.impl.SendPosEvent
import cicada.client.event.impl.TickEvent
import cicada.client.utils.client.connection
import cicada.client.utils.client.player
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket

// SCWGxD regrets everything he did. 28.06.2026 7:11.
object SprintResetPacket : SprintResetMode("Packet") {
    override fun startReset(event: Event): Boolean {
        if (event is SendPosEvent.Pre) {
            connection.send(ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING))
            return true
        }

        return false
    }

    override fun reset(event: Event): Boolean {
        if (event is TickEvent.Pre)
            return true

        if (event is PacketEvent.Send) {
            val packet = event.packet
            if (packet is ServerboundPlayerCommandPacket) {
                if (packet.action == ServerboundPlayerCommandPacket.Action.START_SPRINTING
                    || packet.action == ServerboundPlayerCommandPacket.Action.STOP_SPRINTING) {
                    event.cancel()
                }
            }
        }

        return false
    }

    override fun stopReset(event: Event): Boolean {
        if (event is SendPosEvent.Pre && player.isSprinting) {
            connection.send(ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.START_SPRINTING))
            return true
        }

        return false
    }
}