package cicada.client.module.impl.combat.sprintreset

import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.setting.ChoiceValue

// created by dicves_recode on 29.12.2025
abstract class SprintResetMode(name: String) : ChoiceValue.Choice(name) {
    open fun startReset(event: Event): Boolean { return true }
    open fun reset(event: Event): Boolean { return event is TickEvent.Pre }
    open fun stopReset(event: Event): Boolean { return true }
}
