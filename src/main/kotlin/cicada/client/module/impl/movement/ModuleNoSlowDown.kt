package cicada.client.module.impl.movement

import cicada.client.event.Event
import cicada.client.event.impl.SlowDownEvent
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.module.impl.movement.noslowdown.NoSlowDownType
import cicada.client.module.impl.movement.noslowdown.mode.consume.NoSlowDownIntaveConsumeMode

// SCWGxD regrets everything he did. 17.04.2026 12:10.
object ModuleNoSlowDown : Module("NoSlowDown", Category.MOVEMENT) {
    private val noSlowDownTypes = listOf(
        tree(NoSlowDownType("Bow", 0.2f, SlowDownEvent.Type.Bow)).apply { mode.apply {

        } },

        tree(NoSlowDownType("Consume", 1f, SlowDownEvent.Type.Consume)).apply { mode.apply {
            choice(NoSlowDownIntaveConsumeMode())
        } },

        tree(NoSlowDownType("Hit", 0.6f, SlowDownEvent.Type.Hit)).apply { mode.apply {

        } },

        tree(NoSlowDownType("Sneak", 1f, SlowDownEvent.Type.Sneak)).apply { mode.apply {

        } },

        tree(NoSlowDownType("SwordBlock", 0.2f, SlowDownEvent.Type.SwordBlock)).apply { mode.apply {

        } },
    )

    override fun onEvent(event: Event) {
        for (noSlowDownType in noSlowDownTypes) {
            if (!noSlowDownType.toggled) continue
            noSlowDownType.onEvent(event)
        }
    }
}