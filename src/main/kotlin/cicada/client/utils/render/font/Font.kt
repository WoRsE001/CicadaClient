package cicada.client.utils.render.font

import cicada.client.utils.render.MSDF_PIPELINE
import cicada.client.utils.render.TexturedElementToDraw
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 26.04.2026 0:21.
class Font(
    val name: String
) {
    private val font = MsdfFont.create(name)

    fun draw(graphics: GuiGraphicsExtractor, text: String, x: Float, y: Float, size: Float, color: Int) {
        graphics.guiRenderState.addGuiElement(
            TexturedElementToDraw(
                graphics.pose(),
                MSDF_PIPELINE,
                x, y, width(text, size), font.height(size),
                { consumer ->
                    font.applyGlyphs(text, graphics.pose(), consumer, x, y, size, color)
                },
                graphics.scissorStack.peek(),
                font.texture
            )
        )
    }

    fun draw(graphics: GuiGraphicsExtractor, text: String, x: Float, y: Float, size: Float) =
        draw(graphics, text, x, y, size, -1)

    fun centeredDraw(graphics: GuiGraphicsExtractor, text: String, x: Float, y: Float, size: Float, color: Int) =
        draw(graphics, text, x - width(text, size) / 2, y - font.height(size) / 2, size, color)

    fun centeredDraw(graphics: GuiGraphicsExtractor, text: String, x: Float, y: Float, size: Float) =
        draw(graphics, text, x - width(text, size) / 2, y - font.height(size) / 2, size)

    fun width(text: String, size: Float): Float {
        return font.width(text, size)
    }

    fun height(size: Float): Float {
        return font.height(size)
    }
}