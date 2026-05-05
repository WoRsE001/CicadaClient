package cicada.client.module.impl.movement

import cicada.client.event.Event
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.module.impl.movement.flight.FlightMotionMode

// SCWGxD regrets everything he did. 02.05.2026 5:13.
object ModuleFlight : Module("Flight", Category.MOVEMENT) {
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