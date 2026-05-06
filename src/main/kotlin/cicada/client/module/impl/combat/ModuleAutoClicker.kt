package cicada.client.module.impl.combat

import cicada.client.event.Event
import cicada.client.event.impl.GameLoopEvent
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.mixin.accessors.AccessorMinecraft
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.utils.math.gaussianRandom
import cicada.client.utils.math.random
import cicada.client.utils.mc
import cicada.client.utils.time.Timer

object ModuleAutoClicker : Module("AutoClicker", Category.COMBAT) {
    private val CPS by floatRange("CPS", 20f..20f, 1f..40f)
    private val randomType = choice("Random type")
    private val randomTypeDefault = randomType.choice("Default").select()
    private val randomTypeGaussian = randomType.choice("Gaussian")
    private val condition = multiChoice("Condition")
    private val whenClickMouse = condition.choice("When click mouse", true)

    private val clickTimer = Timer()
    private var time = 0f
    private var clicks = 0

    override fun onEvent(event: Event) {
        if (event is GameLoopEvent.Pre) {
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