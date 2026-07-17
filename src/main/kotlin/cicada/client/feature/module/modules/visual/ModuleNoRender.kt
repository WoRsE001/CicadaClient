package cicada.client.feature.module.modules.visual

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory

// SCWGxD regrets everything he did. 01.04.2026 12:04.
object ModuleNoRender : ClientModule("NoRender", ModuleCategory.VISUAL) {
    val achievementsOverlay by boolean("Achievements overlay", false) // TODO: MAKE
    val blindnessFog by boolean("Blindness fog", false)
    val darknessFog by boolean("Darkness fog", false)
    val fireOverlay by boolean("Fire overlay", false)
    val screenBobbing by boolean("Screen bobbing", false) // TODO: FIX
    val powderSnowOverlay by boolean("Powder snow overlay", false)
    val lavaFog by boolean("Lava fog", false) // TODO: FIX
    val waterFog by boolean("Water fog", false) // TODO: FIX

    override fun listenEvents() = false
}