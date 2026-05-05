package cicada.client.module.impl.combat.antikb

import cicada.client.event.Event
import cicada.client.event.impl.AttackEvent
import cicada.client.setting.ChoiceValue
import cicada.client.utils.player

// SCWGxD regrets everything he did. 17.04.2026 11:57.
object AntiKBReduceMode : ChoiceValue.Choice("Reduce") {
    private val motionXZOnSprintHit by float("Motion XZ on sprint hit", 0.6f, -1f..1f)
    private val motionXZOnHit by float("Motion XZ on hit", 1f, -1f..1f)

    override fun onEvent(event: Event) {
        if (event !is AttackEvent.Pre) return

        if (player.hurtTime != 0) {
            if (player.isSprinting) {
                player.isSprinting = false
                player.deltaMovement.multiply(
                    motionXZOnSprintHit.toDouble(),
                    motionXZOnSprintHit.toDouble(),
                    motionXZOnSprintHit.toDouble()
                )
            } else
                player.deltaMovement.multiply(motionXZOnHit.toDouble(), motionXZOnHit.toDouble(), motionXZOnHit.toDouble())
        }
    }
}