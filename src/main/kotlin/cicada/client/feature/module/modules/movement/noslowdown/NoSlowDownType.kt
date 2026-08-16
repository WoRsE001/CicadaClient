package cicada.client.feature.module.modules.movement.noslowdown

import cicada.client.event.Event
import cicada.client.event.events.EventSlowDown
import cicada.client.setting.value.ChoiceValue
import cicada.client.setting.value.ToggleableConfigurable
import cicada.client.utils.client.player

// SCWGxD regrets everything he did. 17.04.2026 13:34.
open class NoSlowDownType(
    name: String,
    defaultSlowDown: Float,
    private val noSlowDownType: EventSlowDown.Type,
    vararg modes: ChoiceValue.Choice
) : ToggleableConfigurable(name, false) {
    protected val sprintOnGround by boolean("SprintOnGround", false)
    protected val sprintInAir by boolean("SprintInAir", false)
    protected val slowDownOnGround by float("SlowDownOnGround", defaultSlowDown, 0.0f..1.0f)
    protected val slowDownInAir by float("SlowDownInAir", defaultSlowDown, 0.0f..1.0f)
    val mode = choice("Mode").apply {
        choice("None").select()

        for (mode in modes) {
            choice(mode)
        }
    }

    open fun onEvent(event: Event) {
        if (event is EventSlowDown && event.type == noSlowDownType) {
            event.sprint = if (player.onGround()) sprintOnGround else sprintInAir
            event.slowDown = if (player.onGround()) slowDownOnGround else slowDownInAir
        }

        mode.inner?.onEvent(event)
    }
}