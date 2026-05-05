package cicada.client.hud.impl

import cicada.client.hud.HUD
import cicada.client.utils.mc
import cicada.client.utils.render.font.INTER_BOLD_FONT
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 02.04.2026 11:32.
object HudDebug : HUD(0f, 0f, 50f, 12f, "Debug", false) {
    override fun render(graphics: GuiGraphicsExtractor) {
        INTER_BOLD_FONT.draw(graphics, mc.fps.toString(), x, y, 12f, -1)
    }
}