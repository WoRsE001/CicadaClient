package cicada.client.feature.hud

import cicada.client.gui.HUDEditor
import cicada.client.feature.hud.huds.HudDebug
import cicada.client.feature.hud.huds.HudKeystrokes
import cicada.client.feature.hud.huds.HudModuleList
import cicada.client.feature.hud.huds.HudTargetInfo
import cicada.client.utils.client.mc
import cicada.client.utils.render.Renderable
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 02.04.2026 11:22.
@Suppress("UNUSED_EXPRESSION")
object HUDManager : Renderable {
    private val _HUDs = mutableListOf<HUD>()
    val HUDs: List<HUD>
        get() = _HUDs

    init {
        HudDebug
        HudKeystrokes
        HudModuleList
        HudTargetInfo
    }

    override fun render(graphics: GuiGraphicsExtractor) {
        if (mc.screen != HUDEditor) {
            for (HUD in _HUDs)
                if (HUD.shouldRender())
                    HUD.render(graphics)
        }
    }

    operator fun plusAssign(HUD: HUD) {
        _HUDs += HUD
    }
}