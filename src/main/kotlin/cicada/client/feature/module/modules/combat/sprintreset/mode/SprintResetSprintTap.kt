package cicada.client.feature.module.modules.combat.sprintreset.mode

import cicada.client.event.Event
import cicada.client.event.impl.PlayerStateUpdateEvent
import cicada.client.utils.client.player

// SCWGxD regrets everything he did. 28.06.2026 7:09.
object SprintResetSprintTap : SprintResetMode("SprintTap") {
    override fun reset(event: Event): Boolean {
        if (event is PlayerStateUpdateEvent.Post) {
            player.isSprinting = false
            return true
        }

        return false
    }
}