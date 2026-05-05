package cicada.client.module.impl.combat

import cicada.client.event.Event
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.module.impl.combat.antikb.AntiKBJumpMode
import cicada.client.module.impl.combat.antikb.AntiKBMotionMode
import cicada.client.module.impl.combat.antikb.AntiKBReduceMode

// SCWGxD regrets everything he did. 17.04.2026 11:47.
object ModuleAntiKB : Module("AntiKB", Category.COMBAT) {
    private val mode by choice("Mode").apply {
        choice(AntiKBJumpMode).select()
        choice(AntiKBMotionMode)
        choice(AntiKBReduceMode)
    }

    override fun onEvent(event: Event) {
        mode?.onEvent(event)
    }
}