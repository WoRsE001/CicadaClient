package cicada.client.module.impl.misc

import cicada.client.module.Category
import cicada.client.module.Module

// SCWGxD regrets everything he did. 23.04.2026 14:12.
object ModuleFixes : Module("Fixes", Category.MISC) {
    val noMissTime by boolean("No miss time", false)
}