package cicada.client.feature.hud.huds

import cicada.client.feature.hud.HUD
import cicada.client.font.Fonts
import cicada.client.render.engine.height
import cicada.client.render.engine.text
import cicada.client.render.engine.width
import cicada.client.utils.client.mc
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 02.04.2026 11:32.
object HUDDebug : HUD(0f, 0f, 0f, 0f, "Debug", false) {
    private val fontSize by float("TextSize", 12f, 1f..20f)

    private val font = Fonts["inter-bold"]!!

    override fun render(graphics: GuiGraphicsExtractor) {
        val FPSText = mc.fps.toString()
        graphics.text(font, FPSText, x, y, fontSize)
        w = font.width(FPSText, fontSize)
        h = font.height(fontSize)
    }

    override fun renderInHUDEditor(graphics: GuiGraphicsExtractor) {
        render(graphics)
    }
}