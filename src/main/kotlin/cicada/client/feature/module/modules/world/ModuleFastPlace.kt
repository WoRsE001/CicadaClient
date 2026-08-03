package cicada.client.feature.module.modules.world

import cicada.client.event.Event
import cicada.client.event.impl.EventGameLoop
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.mixin.accessors.AccessorMinecraft
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.math.gaussianRandom
import cicada.client.utils.math.random
import cicada.client.utils.time.Timer
import net.minecraft.world.item.BlockItem

object ModuleFastPlace : ClientModule("FastPlace", ModuleCategory.WORLD) {
    private val CPS by floatRange("CPS", 20f..20f, 1f..40f)
    private val randomType = choice("Random type")
    private val randomTypeDefault = randomType.choice("Default").select()
    private val randomTypeGaussian = randomType.choice("Gaussian")

    private val clickTimer = Timer()
    private var time = 0f
    private var clicks = 0
    
    override fun onEvent(event: Event) {
        if (event is EventGameLoop.Pre) {
            if (mc.options.keyUse.isDown && player.mainHandItem.item is BlockItem) {
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
                (mc as AccessorMinecraft).invokeStartUseItem()
            }
            clicks = 0
        }
    }
}