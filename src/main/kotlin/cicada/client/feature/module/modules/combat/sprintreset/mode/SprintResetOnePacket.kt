package cicada.client.feature.module.modules.combat.sprintreset.mode

import cicada.client.event.Event
import cicada.client.event.events.EventSendSprint
import cicada.client.utils.client.player

// SCWGxD regrets everything he did. 28.06.2026 7:12.
object SprintResetOnePacket : SprintResetMode("OnePacket") {
    override fun startReset(event: Event): Boolean {
        if (event is EventSendSprint.Pre) {
            if (player.isSprinting)
                event.wasSprinting = false

            return true
        }

        return false
    }
}