package cicada.client.feature.module.modules.misc

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory

// SCWGxD regrets everything he did. 23.04.2026 14:12.
object ModuleFixes : ClientModule("Fixes", ModuleCategory.MISC) {
    val noMissTime by boolean("No miss time", false)
}