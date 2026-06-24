package cicada.client.feature.module.modules.combat.antikb

import cicada.client.event.Event
import cicada.client.event.impl.MovementInputEvent
import cicada.client.setting.ChoiceValue
import cicada.client.utils.client.player

// SCWGxD regrets everything he did. 27.04.2026 9:58.
object AntiKBJumpMode : ChoiceValue.Choice("Jump") {
    private val hurtTime by intRange("Hurt time", 6..10, 0..10)

    override fun onEvent(event: Event) {
        if (event is MovementInputEvent && player.hurtTime >= hurtTime.first && player.hurtTime <= hurtTime.last && Math.random() < 0.7) {
            event.jump = true
        }
    }
}