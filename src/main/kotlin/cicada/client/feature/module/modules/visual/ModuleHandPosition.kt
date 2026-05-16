package cicada.client.feature.module.modules.visual

import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.modules.visual.handposition.SettingHandPosition

// SCWGxD regrets everything he did. 18.04.2026 4:05.
object ModuleHandPosition : ClientModule("HandPosition", ModuleCategory.VISUAL) {
    val mainHandPosition = tree(SettingHandPosition("Main hand"))
    val offHandPosition = tree(SettingHandPosition("Off hand"))

    override fun listenEvents() = false
}