package cicada.client.feature.module.modules.combat.sprintreset.mode

import cicada.client.event.Event
import cicada.client.event.impl.MovementInputEvent

// SCWGxD regrets everything he did. 28.06.2026 7:11.
object SprintResetWTap : SprintResetMode("W ap") {
    override fun reset(event: Event): Boolean {
        if (event is MovementInputEvent) {
            event.forward = false
            return true
        }

        return false
    }
}