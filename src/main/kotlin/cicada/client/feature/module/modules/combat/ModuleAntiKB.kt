package cicada.client.feature.module.modules.combat

import cicada.client.event.Event
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.modules.combat.antikb.AntiKBJumpMode
import cicada.client.feature.module.modules.combat.antikb.AntiKBMotionMode
import cicada.client.feature.module.modules.combat.antikb.AntiKBReduceMode

// SCWGxD regrets everything he did. 17.04.2026 11:47.
object ModuleAntiKB : ClientModule("AntiKB", ModuleCategory.COMBAT) {
    private val mode by choice("Mode").apply {
        choice(AntiKBJumpMode).select()
        choice(AntiKBMotionMode)
        choice(AntiKBReduceMode)
    }

    override fun onEvent(event: Event) {
        mode?.onEvent(event)
    }
}