package cicada.client.feature.module.modules.movement

import cicada.client.event.Event
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.modules.movement.flight.FlightMotionMode

// SCWGxD regrets everything he did. 02.05.2026 5:13.
object ModuleFlight : ClientModule("Flight", ModuleCategory.MOVEMENT) {
    private val mode = choice("Mode").apply {
        choice(FlightMotionMode).select()
    }

    override fun onEnable() {
        mode.inner?.onEnable()
    }

    override fun onDisable() {
        mode.inner?.onDisable()
    }

    override fun onEvent(event: Event) {
        mode.inner?.onEvent(event)
    }
}