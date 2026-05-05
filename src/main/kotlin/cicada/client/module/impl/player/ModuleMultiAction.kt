package cicada.client.module.impl.player

import cicada.client.module.Category
import cicada.client.module.Module

// SCWGxD regrets everything he did. 19.04.2026 11:41.
object ModuleMultiAction : Module("MultiAction", Category.PLAYER) {
    private val actions = choice("Actions")
    private val attackWhileUsing = actions.choice("Attack while using")

    @JvmStatic
    fun mayAttackWhileUsing() =
        this.toggled && attackWhileUsing.selected()
}