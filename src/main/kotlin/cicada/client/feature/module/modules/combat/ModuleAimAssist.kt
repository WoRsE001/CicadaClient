package cicada.client.feature.module.modules.combat

import cicada.client.event.Event
import cicada.client.event.events.EventMouseTurn
import cicada.client.event.events.EventTick
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.setting.preset.TargetFinder
import cicada.client.utils.client.player
import cicada.client.utils.math.random
import cicada.client.utils.rotation.rotation
import cicada.client.utils.rotation.rotationTo
import kotlin.math.roundToLong
import kotlin.math.sign

object ModuleAimAssist : ClientModule(
    "AimAssist",
    ModuleCategory.COMBAT
) {
    private val targetFinder = tree(TargetFinder())

    private val boostSpeed by floatRange("BoostSpeed", 1.3f..1.5f, 1f..10f)
    private val missSpeed  by floatRange("MissSpeed", 0.5f..0.7f, 0f..1f)

    private val vertical by boolean("Vertical", false)

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            targetFinder.updateTarget()
        }

        if (event is EventMouseTurn) {
            if (event.xo == 0.0 && event.yo == 0.0)
                return

            targetFinder.target?.let {
                val delta = (rotationTo(it.eyePosition) - player.rotation()).wrapped()

                val x = event.xo.sign.toFloat() == delta.y.sign
                val y = event.yo.sign.toFloat() == delta.x.sign

                val boostSpeed = boostSpeed.random()
                val missSpeed = missSpeed.random()

                val ySpeed = if (y) boostSpeed else missSpeed
                val xSpeed = if (x) boostSpeed else missSpeed

                event.xo = (event.xo * xSpeed).roundToLong().toDouble()
                if (vertical)
                    event.yo = (event.yo * ySpeed).roundToLong().toDouble()
            }
        }
    }
}