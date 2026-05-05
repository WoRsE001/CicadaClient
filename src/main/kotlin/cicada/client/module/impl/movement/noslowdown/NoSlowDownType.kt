package cicada.client.module.impl.movement.noslowdown

import cicada.client.event.Event
import cicada.client.event.impl.SlowDownEvent
import cicada.client.setting.ToggleableConfigureable
import cicada.client.utils.player

// SCWGxD regrets everything he did. 17.04.2026 13:34.
open class NoSlowDownType(
    name: String,
    defaultSlowDown:
    Float,
    private val noSlowDownType: SlowDownEvent.Type
) : ToggleableConfigureable(name, false) {
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