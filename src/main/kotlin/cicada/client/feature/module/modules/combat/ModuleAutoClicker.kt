package cicada.client.feature.module.modules.combat

import cicada.client.event.Event
import cicada.client.event.events.EventGameLoop
import cicada.client.event.events.EventClickTiming
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.utils.client.mc
import cicada.client.utils.math.gaussianRandom
import cicada.client.utils.math.random
import cicada.client.utils.time.Timer

object ModuleAutoClicker : ClientModule("AutoClicker", ModuleCategory.COMBAT) {
    private val CPS by floatRange("CPS", 20f..20f, 1f..40f)
    private val randomType = choice("RandomType")
    private val randomTypeDefault = randomType.choice("Default").select()
    private val randomTypeGaussian = randomType.choice("Gaussian")
    private val condition = multiChoice("Condition")
    private val whenClickMouse = condition.choice("WhenClickMouse", true)

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

        if (event is EventClickTiming) {
            repeat(clicks) {
                mc.missTime = 0
                mc.startAttack()
            }

            clicks = 0
        }
    }
}