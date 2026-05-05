package cicada.client.utils.render

import cicada.client.utils.math.Rect
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 04.04.2026 17:28.
interface RenderableObject : Renderable {
    val rect: Rect

    override fun render(graphics: GuiGraphicsExtractor)
}