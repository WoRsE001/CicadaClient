package cicada.client.module.impl.visual

import cicada.client.CicadaClient
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.utils.render.image
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 01.05.2026 8:38.
object ModuleOverlay : Module("Overlay", Category.VISUAL) {
    fun renderOverlay(graphics: GuiGraphicsExtractor) {
        graphics.image(0f, 0f, graphics.guiWidth().toFloat(), graphics.guiHeight().toFloat(), CicadaClient.of("images/overlays/overlay_0.png"))
    }
}