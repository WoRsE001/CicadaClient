package cicada.client.feature.module.modules.player.phase

import cicada.client.event.Event
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.player.phase.mode.*

// SCWGxD regrets everything he did. 24.06.2026 14:53.
object ModulePhase : ClientModule("Phase", ModuleCategory.PLAYER) {
    private val mode = choice("Mode").apply {
        choice(PhaseNoCollision)
        choice(PhaseIntave).select()
    }

    override fun onEnable() {
        mode.inner?.onEnable()
    }

    override fun onEvent(event: Event) {
        mode.inner?.onEvent(event)
    }
}