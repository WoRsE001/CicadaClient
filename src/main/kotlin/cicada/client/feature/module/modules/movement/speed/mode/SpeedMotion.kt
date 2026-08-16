package cicada.client.feature.module.modules.movement.speed.mode

import cicada.client.event.Event
import cicada.client.event.events.EventTick
import cicada.client.setting.value.ChoiceValue
import cicada.client.utils.client.player
import cicada.client.utils.player.withStrafe

// SCWGxD regrets everything he did. 20.04.2026 3:59.
object SpeedMotion : ChoiceValue.Choice("Motion") {
    private val strength by float("Strength", 1f, 0.1f..1f)
    private val customSpeed by boolean("CustomSpeed", true)
    private val speed by float("Speed", 1f, 0.1f..10f).visible { customSpeed }

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            when {
                customSpeed -> player.deltaMovement = player.deltaMovement.withStrafe(
                    speed = speed.toDouble(),
                    strength = strength.toDouble()
                )
                else ->
                    player.deltaMovement = player.deltaMovement.withStrafe(strength = strength.toDouble())
            }
        }
    }
}