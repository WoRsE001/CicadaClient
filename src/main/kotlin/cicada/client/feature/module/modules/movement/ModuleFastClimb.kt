package cicada.client.feature.module.modules.movement

import cicada.client.event.Event
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.modules.movement.fastclimb.FastClimbMotionMode
import cicada.client.feature.module.modules.movement.fastclimb.FastClimbPoralMode
import cicada.client.utils.player

// SCWGxD regrets everything he did. 16.04.2026 10:25.
object ModuleFastClimb : ClientModule("FastClimb", ModuleCategory.MOVEMENT) {
    private val mode by choice("Mode").apply {
        choice(FastClimbMotionMode)
        choice(FastClimbPoralMode).select()
    }

    override fun onEvent(event: Event) {
        if (player.horizontalCollision && player.onClimbable())
            mode?.onEvent(event)
    }
}