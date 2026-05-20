package cicada.client.feature.hud.huds

import cicada.client.feature.hud.HUD
import cicada.client.font.Fonts
import cicada.client.render.text
import cicada.client.utils.client.mc
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 02.04.2026 11:32.
object HudDebug : HUD(0f, 0f, 50f, 12f, "Debug", false) {
    override fun render(graphics: GuiGraphicsExtractor) {
        graphics.text(Fonts["inter-bold"], mc.fps.toString(), x, y, 12f)
    }
}