package cicada.client.feature.module.modules.combat.antikb

import cicada.client.event.Event
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.combat.antikb.modes.AntiKBJump
import cicada.client.feature.module.modules.combat.antikb.modes.AntiKBMode
import cicada.client.feature.module.modules.combat.antikb.modes.AntiKBMotion
import cicada.client.feature.module.modules.combat.antikb.modes.AntiKBReduce

// SCWGxD regrets everything he did. 17.04.2026 11:47.
object ModuleAntiKB : ClientModule("AntiKB", ModuleCategory.COMBAT) {
    private val modes by multiChoice("Modes").apply {
        choice(AntiKBJump)
        choice(AntiKBMotion)
        choice(AntiKBReduce)
    }

    override fun onEvent(event: Event) {
        for (mode in modes) {
            if (mode is AntiKBMode && mode.toggled) {
                mode.onEvent(event)
            }
        }
    }
}