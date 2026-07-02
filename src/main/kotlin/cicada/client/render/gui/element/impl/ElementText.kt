package cicada.client.render.gui.element.impl

import cicada.client.font.FontData
import cicada.client.render.engine.height
import cicada.client.render.engine.text
import cicada.client.render.engine.width
import cicada.client.render.gui.element.Element
import net.minecraft.client.gui.GuiGraphicsExtractor

/**
 * ElementText - рисует текст
 */
class ElementText(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    private val font: FontData,
    private val fontSize: Float = 9f,
    private val textProvider: () -> String,
    private val color: Int = 0xFFFFFFFF.toInt()
) : Element(xProvider, yProvider, { font.width(textProvider(), fontSize) }, { font.height(fontSize) }) {
    var text = textProvider()

    override fun measure() {
        text = textProvider()
        super.measure()
    }

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        graphics.text(font, text, offsetX + x, offsetY + y, fontSize, color)

        super.draw(graphics, offsetX, offsetY)
    }
}