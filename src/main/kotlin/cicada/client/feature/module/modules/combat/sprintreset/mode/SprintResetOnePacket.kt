package cicada.client.feature.module.modules.combat.sprintreset.mode

import cicada.client.event.Event
import cicada.client.event.impl.SendPosEvent
import cicada.client.utils.client.connection
import cicada.client.utils.client.player
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket

// SCWGxD regrets everything he did. 28.06.2026 7:12.
object SprintResetOnePacket : SprintResetMode("One packet") {
    override fun startReset(event: Event): Boolean {
        if (event is SendPosEvent.Pre && player.isSprinting) {
            connection.send(ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.START_SPRINTING))
            return true
        }

        return false
    }
}