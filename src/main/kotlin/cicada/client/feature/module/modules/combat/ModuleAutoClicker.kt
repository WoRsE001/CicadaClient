package cicada.client.feature.module.modules.combat

import cicada.client.event.Event
import cicada.client.event.impl.EventGameLoop
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.mixin.accessors.AccessorMinecraft
import cicada.client.utils.client.mc
import cicada.client.utils.math.gaussianRandom
import cicada.client.utils.math.random
import cicada.client.utils.time.Timer

object ModuleAutoClicker : ClientModule("AutoClicker", ModuleCategory.COMBAT) {
    private val CPS by floatRange("CPS", 14f..16f, 1f..40f)
    private val randomType = choice("Random type")
    private val randomTypeDefault = randomType.choice("Default")
    private val randomTypeGaussian = randomType.choice("Gaussian").select()
    private val condition = multiChoice("Condition")
    private val whenClickMouse = condition.choice("When click mouse", true)

    private val clickTimer = Timer()
    private var time = 0f
    private var clicks = 0

    override fun onEvent(event: Event) {
        if (event is EventGameLoop.Pre) {
            if (
                (!whenClickMouse.toggled || mc.options.keyAttack.isDown)
            ) {
                clickTimer.reached(time) {
                    clicks++
                    if (randomTypeDefault.selected()) {
                        time = 1000 / CPS.random()
                    } else if (randomTypeGaussian.selected()) {
                        time = 1000 / CPS.gaussianRandom()
                    }
                }
            }
        }

        if (event is LegitClickTimingEvent) {
            repeat(clicks) {
                mc.missTime = 0
                (mc as AccessorMinecraft).invokeStartAttack()
            }

            clicks = 0
        }
    }
}