package cicada.client.module.impl.movement.speed

import cicada.client.event.Event
import cicada.client.event.impl.PlayerStateUpdateEvent
import cicada.client.setting.ChoiceValue
import cicada.client.utils.player
import cicada.client.utils.player.airTicks
import cicada.client.utils.player.velocityY

// SCWGxD regrets everything he did. 20.04.2026 3:59.
object SpeedMetaHVHMode : ChoiceValue.Choice("MetaHVH") {
    private val speed by float("Speed", 0.2f, 0f..3f)
    private val height by float("Height", 0.42f, 0f..3f)

    override fun onEvent(event: Event) {
        if (event is PlayerStateUpdateEvent.Pre) {
            if (player.airTicks > 2) {
                player.velocityY -= .02
            }
        }
    }
}