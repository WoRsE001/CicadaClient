package cicada.client.module.impl.visual

import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.module.impl.visual.handposition.SettingHandPosition

// SCWGxD regrets everything he did. 18.04.2026 4:05.
object ModuleHandPosition : Module("HandPosition", Category.VISUAL) {
    val mainHandPosition = tree(SettingHandPosition("Main hand"))
    val offHandPosition = tree(SettingHandPosition("Off hand"))

    override fun listenEvents() = false
}