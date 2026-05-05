package cicada.client.hud

import cicada.client.gui.HUDEditor
import cicada.client.hud.impl.HudDebug
import cicada.client.hud.impl.HudKeystrokes
import cicada.client.hud.impl.HudTargetInfo
import cicada.client.utils.mc
import cicada.client.utils.render.Renderable
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 02.04.2026 11:22.
@Suppress("UNUSED_EXPRESSION")
object HUDs : Renderable {
    private val _HUDs = mutableListOf<HUD>()

    val HUDs: List<HUD>
        get() = _HUDs

    init {
        HudDebug
        HudKeystrokes
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