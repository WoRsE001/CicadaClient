package cicada.client.feature.module.modules.combat.sprintreset.mode

import cicada.client.event.Event
import cicada.client.event.events.EventPacket
import cicada.client.event.events.EventSendPos
import cicada.client.event.events.EventSendSprint
import cicada.client.event.events.EventTick
import cicada.client.utils.client.connection
import cicada.client.utils.client.player
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket

// SCWGxD regrets everything he did. 28.06.2026 7:11.
object SprintResetPacket : SprintResetMode("Packet") {
    override fun startReset(event: Event): Boolean {
        if (event is EventSendSprint.Pre) {
            event.cancel()
            connection.send(ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING))
            return true
        }

        return false
    }

    override fun stopReset(event: Event): Boolean {
        if (event is EventSendSprint.Pre) {
            if (player.isSprinting)
                event.wasSprinting = false

            return true
        }

        return false
    }
}