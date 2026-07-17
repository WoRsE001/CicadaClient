package cicada.client.feature.module.modules.movement.flight.mode

import cicada.client.event.Event
import cicada.client.event.impl.SendPosEvent
import cicada.client.event.impl.TickEvent
import cicada.client.setting.value.ChoiceValue
import cicada.client.setting.value.Configurable
import cicada.client.setting.value.ToggleableConfigurable
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.player.velocityY
import cicada.client.utils.player.withStrafe
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 02.05.2026 5:21.
object FlightMotion : ChoiceValue.Choice("Motion") {
    private val glideSpeed by float("Glide speed", 0f, -1f..1f)
    private val kickBypass by boolean("Kick bypass", false)
    private val saveMomentum by boolean("Save momentum", true)

    object BaseSpeed : Configurable("Base speed") {
        val horizontalSpeed by float("Horizontal", 1f, 0f..10f)
        val verticalSpeed by float("Vertical", 1f, 0f..10f)
    }

    object SprintSpeed : ToggleableConfigurable("SprintSpeed", false) {
        val horizontalSpeed by float("Horizontal", 5f, 0f..10f)
        val verticalSpeed by float("Vertical", 1f, 0f..10f)
    }

    var lastPos = Vec3(0.0, 0.0, 0.0)
    var noMoveTick = 0

    init {
        tree(BaseSpeed)
        tree(SprintSpeed)
    }

    override fun onDisable() {
        if (!saveMomentum) {
            player.deltaMovement = Vec3(0.0, 0.0, 0.0)
        }
    }

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            val useSprintSpeed = mc.options.keySprint.isDown && SprintSpeed.toggled
            val hSpeed =
                if (useSprintSpeed) SprintSpeed.horizontalSpeed else BaseSpeed.horizontalSpeed
            val vSpeed =
                if (useSprintSpeed) SprintSpeed.verticalSpeed else BaseSpeed.verticalSpeed

            player.deltaMovement = player.deltaMovement.withStrafe(speed = hSpeed.toDouble())
            player.velocityY = when {
                mc.options.keyJump.isDown -> vSpeed.toDouble()
                mc.options.keyShift.isDown -> (-vSpeed).toDouble()
                else -> glideSpeed.toDouble()
            }

            if (noMoveTick >= 40) {
                player.velocityY = -0.04
            }
        }

        if (event is SendPosEvent.Pre) {
            if (kickBypass) {
                if (player.position().distanceTo(lastPos) < 0.04) {
                    noMoveTick++
                } else {
                    noMoveTick = 0
                    lastPos = player.position()
                }
            }
        }
    }
}