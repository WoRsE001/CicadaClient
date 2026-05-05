package cicada.client.module.impl.combat.sprintreset

import cicada.client.event.Event
import cicada.client.event.impl.MovementInputEvent
import cicada.client.event.impl.PacketEvent
import cicada.client.event.impl.PlayerStateUpdateEvent
import cicada.client.event.impl.SendPosEvent
import cicada.client.event.impl.TickEvent
import cicada.client.utils.connection
import cicada.client.utils.player
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket

// created by dicves_recode on 29.12.2025
object SprintTap : SprintResetMode("Sprint tap") {
    override fun reset(event: Event): Boolean {
        if (event is PlayerStateUpdateEvent.Post) {
            player.isSprinting = false
            return true
        }

        return false
    }
}

object WTap : SprintResetMode("W tap") {
    override fun reset(event: Event): Boolean {
        if (event is MovementInputEvent) {
            event.forward = false
            return true
        }

        return false
    }
}

object Packet : SprintResetMode("Packet") {
    override fun startReset(event: Event): Boolean {
        if (event is SendPosEvent.Pre && player.isSprinting) {
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

object OnePacket : SprintResetMode("One packet") {
    override fun startReset(event: Event): Boolean {
        if (event is SendPosEvent.Pre && player.isSprinting) {
            connection.send(ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.START_SPRINTING))
            return true
        }

        return false
    }
}
