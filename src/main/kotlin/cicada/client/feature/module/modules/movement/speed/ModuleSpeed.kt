package cicada.client.feature.module.modules.movement.speed

import cicada.client.event.Event
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.movement.speed.mode.SpeedIntave
import cicada.client.feature.module.modules.movement.speed.mode.SpeedMotion

// SCWGxD regrets everything he did. 20.04.2026 3:55.
object ModuleSpeed : ClientModule("Speed", ModuleCategory.MOVEMENT) {
    private val mode by choice("Mode").apply {
        choice(SpeedMotion)
        choice(SpeedIntave).select()
    }

    override fun onEvent(event: Event) {
        mode?.onEvent(event)
    }
}