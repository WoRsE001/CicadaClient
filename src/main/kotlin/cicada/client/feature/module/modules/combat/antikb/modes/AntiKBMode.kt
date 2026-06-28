package cicada.client.feature.module.modules.combat.antikb.modes

import cicada.client.event.Event
import cicada.client.setting.MultiChoiceValue

// SCWGxD regrets everything he did. 28.06.2026 6:49.
abstract class AntiKBMode(name: String) : MultiChoiceValue.Choice(name, false) {
    protected val chance by float("Chance", 1f, 0f..1f, "%")

    abstract fun onEvent(event: Event)
}