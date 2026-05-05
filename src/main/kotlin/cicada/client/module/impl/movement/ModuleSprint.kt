package cicada.client.module.impl.movement

import cicada.client.module.Category
import cicada.client.module.Module

object ModuleSprint : Module(
    "Sprint",
    Category.MOVEMENT
) {
    val whileBlindness by boolean("While blindness", false)
    val auto by boolean("Auto", true)
}