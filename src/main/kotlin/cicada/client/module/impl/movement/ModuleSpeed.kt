package cicada.client.module.impl.movement

import cicada.client.event.Event
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.module.impl.movement.speed.SpeedMetaHVHMode

// SCWGxD regrets everything he did. 20.04.2026 3:55.
object ModuleSpeed : Module("Speed", Category.MOVEMENT) {
    private val mode by choice("Mode").apply {
        choice(SpeedMetaHVHMode)
    }

    override fun onEvent(event: Event) {
        mode?.onEvent(event)
    }
}