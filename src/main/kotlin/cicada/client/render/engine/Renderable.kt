package cicada.client.render.engine

import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 31.03.2026 19:49.
interface Renderable {
    fun render(graphics: GuiGraphicsExtractor)
}