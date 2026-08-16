package cicada.client.feature.module.modules.movement.noslowdown

import cicada.client.event.Event
import cicada.client.event.events.EventSlowDown
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.movement.noslowdown.mode.consume.ConsumeIntaveMode
import cicada.client.feature.module.modules.movement.noslowdown.mode.swordblock.SwordBlockIntaveMode

// SCWGxD regrets everything he did. 17.04.2026 12:10.
object ModuleNoSlowDown : ClientModule("NoSlowDown", ModuleCategory.MOVEMENT) {
    private val noSlowDownTypes = listOf(
        tree(NoSlowDownType("Bow", 0.2f, EventSlowDown.Type.Bow,

        )),

        tree(NoSlowDownType("Consume", 0.2f, EventSlowDown.Type.Consume,
            ConsumeIntaveMode()
        )),

        tree(NoSlowDownType("Hit", 0.6f, EventSlowDown.Type.Hit,

        )),

        tree(NoSlowDownType("Sneak", 0.3f, EventSlowDown.Type.Sneak,

        )),

        tree(NoSlowDownType("SwordBlock", 0.2f, EventSlowDown.Type.SwordBlock,
            SwordBlockIntaveMode()
        )),
    )

    override fun onEvent(event: Event) {
        for (noSlowDownType in noSlowDownTypes) {
            if (!noSlowDownType.toggled) continue
            noSlowDownType.onEvent(event)
        }
    }
}