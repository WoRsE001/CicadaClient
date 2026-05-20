package cicada.client.feature.module.modules.movement.noslowdown

import cicada.client.event.Event
import cicada.client.event.impl.SlowDownEvent
import cicada.client.config.types.ToggleableConfigurable
import cicada.client.utils.client.player

// SCWGxD regrets everything he did. 17.04.2026 13:34.
open class NoSlowDownType(
    name: String,
    defaultSlowDown:
    Float,
    private val noSlowDownType: SlowDownEvent.Type
) : ToggleableConfigurable(name, false) {
    protected val sprintOnGround by boolean("Sprint on ground", false)
    protected val sprintInAir by boolean("Sprint in air", false)
    protected val slowDownOnGround by float("Slow down on ground", defaultSlowDown, 0.0f..1.0f)
    protected val slowDownInAir by float("Slow down in air", defaultSlowDown, 0.0f..1.0f)
    val mode = choice("Mode").apply {
        choice("None").select()
    }

    open fun onEvent(event: Event) {
        if (event is SlowDownEvent && event.type == noSlowDownType) {
            event.sprint = if (player.onGround()) sprintOnGround else sprintInAir
            event.slowDown = if (player.onGround()) slowDownOnGround else slowDownInAir
        }

        mode.inner?.onEvent(event)
    }
}