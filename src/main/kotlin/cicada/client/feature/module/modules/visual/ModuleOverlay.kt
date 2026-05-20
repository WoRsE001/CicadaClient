package cicada.client.feature.module.modules.visual

import cicada.client.CicadaClient
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ClientModule
import cicada.client.render.image
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 01.05.2026 8:38.
object ModuleOverlay : ClientModule("Overlay", ModuleCategory.VISUAL) {
    fun renderOverlay(graphics: GuiGraphicsExtractor) {
        graphics.image(CicadaClient.of("images/overlays/overlay_0.png"), 0f, 0f, graphics.guiWidth().toFloat(), graphics.guiHeight().toFloat())
    }
}