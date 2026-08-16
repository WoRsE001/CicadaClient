package cicada.client.feature.module.modules.visual

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory

// SCWGxD regrets everything he did. 01.04.2026 12:04.
object ModuleNoRender : ClientModule("NoRender", ModuleCategory.VISUAL) {
    val achievementsOverlay by boolean("AchievementsOverlay", false) // TODO: MAKE
    val blindnessFog by boolean("BlindnessFog", false)
    val darknessFog by boolean("DarknessFog", false)
    val fireOverlay by boolean("FireOverlay", true)
    val screenBobbing by boolean("ScreenBobbing", false) // TODO: FIX
    val powderSnowOverlay by boolean("PowderSnowOverlay", false)
    val lavaFog by boolean("LavaFog", false) // TODO: FIX
    val waterFog by boolean("WaterFog", false) // TODO: FIX

    override fun shouldListenEvents() = false
}