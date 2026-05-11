package cicada.client.utils.render

import net.minecraft.client.gui.GuiGraphicsExtractor
import org.joml.Vector2f

// SCWGxD regrets everything he did. 31.03.2026 19:49.
interface Renderable {
    fun render(graphics: GuiGraphicsExtractor)
}