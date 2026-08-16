package cicada.client.feature.module.modules.visual

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.visual.handposition.ValueHandPosition

// SCWGxD regrets everything he did. 18.04.2026 4:05.
object ModuleHandPosition : ClientModule("HandPosition", ModuleCategory.VISUAL) {
    val mainHandPosition = tree(ValueHandPosition("MainHand"))
    val offHandPosition = tree(ValueHandPosition("OffHand"))

    override fun shouldListenEvents() = false
}