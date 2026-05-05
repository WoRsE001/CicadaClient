package cicada.client.module.impl.movement.flight

import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.setting.ChoiceValue
import cicada.client.utils.mc
import cicada.client.utils.player
import cicada.client.utils.player.velocityY
import cicada.client.utils.player.withStrafe

// SCWGxD regrets everything he did. 02.05.2026 5:21.
object FlightMotionMode : ChoiceValue.Choice("Motion") {
    private val hSpeed by float("Horizontal speed", 1f, 0f..10f)
    private val vSpeed by float("Vertical speed", 1f, 0f..10f)
    private val glideSpeed by float("Glide speed", 0f, -1f..1f)
    private val kickBypass by boolean("Kick bypass", false)

    var noMoveTick = 0

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            player.deltaMovement = player.deltaMovement.withStrafe(hSpeed.toDouble())
            player.velocityY = when {
                mc.options.keyJump.isDown -> vSpeed.toDouble()
                mc.options.keyShift.isDown -> (-vSpeed).toDouble()
                else -> glideSpeed.toDouble()
            }

            if (kickBypass) {
                if (noMoveTick > 70)
                    player.velocityY = -0.05

                if (player.deltaMovement.y < 0.05)
                    noMoveTick++
                else
                    noMoveTick = 0
            }
        }
    }
}