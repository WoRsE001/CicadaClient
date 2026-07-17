package cicada.client.feature.module.modules.movement

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory

object ModuleSprint : ClientModule(
    "Sprint",
    ModuleCategory.MOVEMENT
) {
    val whileBlindness by boolean("While blindness", false)
    val auto by boolean("Auto", true)
}