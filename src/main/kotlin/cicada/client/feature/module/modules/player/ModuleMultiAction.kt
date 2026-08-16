package cicada.client.feature.module.modules.player

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory

// SCWGxD regrets everything he did. 19.04.2026 11:41.
object ModuleMultiAction : ClientModule("MultiAction", ModuleCategory.PLAYER) {
    private val actions = multiChoice("Actions")
    private val attackWhileUsing = actions.choice("AttackWhileUsing")
    private val breakingWhileUsing = actions.choice("BreakingWhileUsing")

    @JvmStatic
    fun mayAttackWhileUsing() =
        this.toggled && attackWhileUsing.toggled

    @JvmStatic
    fun mayBreakWhileUsing() =
        this.toggled && breakingWhileUsing.toggled
}