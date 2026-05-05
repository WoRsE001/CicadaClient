package cicada.client.module.impl.movement.fastclimb

import cicada.client.event.Event
import cicada.client.event.impl.PlayerStateUpdateEvent
import cicada.client.event.impl.SendPosEvent
import cicada.client.setting.ChoiceValue
import cicada.client.utils.player
import cicada.client.utils.player.velocityY

// SCWGxD regrets everything he did. 16.04.2026 10:28.
object FastClimbMotionMode : ChoiceValue.Choice("Motion") {
    private val motion by float("Motion", 1f, 0f..10f)

    override fun onEvent(event: Event) {
        if (event is PlayerStateUpdateEvent.Pre) {
            player.velocityY = motion.toDouble()
        }
    }
}

object FastClimbPoralMode : ChoiceValue.Choice("Poral") {
    private val motion by float("Motion", 0.6f, 0f..10f)

    override fun onEvent(event: Event) {
        if (event is PlayerStateUpdateEvent.Pre) {
            player.velocityY = motion.toDouble()
        }

        if (event is SendPosEvent.Pre) {
            event.ground = true
        }
    }
}