package cicada.client.utils.render

import net.minecraft.client.gui.GuiGraphicsExtractor
import org.joml.Vector2f

// SCWGxD regrets everything he did. 31.03.2026 19:49.
interface Renderable {
    fun render(graphics: GuiGraphicsExtractor)

    fun isCollide(px: Float, py: Float, x: Float, y: Float, w: Float, h: Float) = px in x..x + w && py in y..y + h

    fun isCollide(p: Vector2f, x: Float, y: Float, w: Float, h: Float) = isCollide(p.x, p.y, x, y, w, h)
}