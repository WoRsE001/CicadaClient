package cicada.client.feature.module.modules.movement.fastclimb.mode

import cicada.client.event.Event
import cicada.client.event.impl.PlayerStateUpdateEvent
import cicada.client.setting.ChoiceValue
import cicada.client.utils.client.player
import cicada.client.utils.player.velocityY

// SCWGxD regrets everything he did. 28.06.2026 7:17.
object FastClimbMotion : ChoiceValue.Choice("Motion") {
    private val motion by float("Motion", 1f, 0f..10f)

    override fun onEvent(event: Event) {
        if (event is PlayerStateUpdateEvent.Pre) {
            player.velocityY = motion.toDouble()
        }
    }
}