package cicada.client.feature.module.modules.movement

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory

// SCWGxD regrets everything he did. 22.07.2026 21:06.
object ModuleMovementHelper : ClientModule("MovementHelper", ModuleCategory.MOVEMENT) {
    val noJumpDelay by boolean("No jump delay", true)
    val snapTap by boolean("Snap tap", true)
}