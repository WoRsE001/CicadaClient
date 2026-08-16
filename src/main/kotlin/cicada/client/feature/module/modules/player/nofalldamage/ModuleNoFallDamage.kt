package cicada.client.feature.module.modules.player.nofalldamage

import cicada.client.event.Event
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.player.nofalldamage.mode.NoFallDamageIntave
import cicada.client.feature.module.modules.player.nofalldamage.mode.NoFallDamageSpoofGround

object ModuleNoFallDamage : ClientModule("NoFallDamage", ModuleCategory.PLAYER) {
    private val mode = choice("Mode").apply {
        choice(NoFallDamageIntave)
        choice(NoFallDamageSpoofGround)
    }

    override fun onDisable() {
        mode.inner?.onDisable()
    }

    override fun onEvent(event: Event) {
        mode.inner?.onEvent(event)
    }
}