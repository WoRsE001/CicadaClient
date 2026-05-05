package cicada.client.module.impl.movement

import cicada.client.event.Event
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.module.impl.movement.fastclimb.FastClimbMotionMode
import cicada.client.module.impl.movement.fastclimb.FastClimbPoralMode
import cicada.client.utils.player

// SCWGxD regrets everything he did. 16.04.2026 10:25.
object ModuleFastClimb : Module("FastClimb", Category.MOVEMENT) {
    private val mode by choice("Mode").apply {
        choice(FastClimbMotionMode)
        choice(FastClimbPoralMode).select()
    }

    override fun onEvent(event: Event) {
        if (player.horizontalCollision && player.onClimbable())
            mode?.onEvent(event)
    }
}