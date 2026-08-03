package cicada.client.render.gui.element.elements

import cicada.client.font.FontData
import cicada.client.render.engine.height
import cicada.client.render.engine.text
import cicada.client.render.engine.width
import cicada.client.render.gui.element.Element
import net.minecraft.client.gui.GuiGraphicsExtractor

class ElementText(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    private val font: FontData,
    private val fontSize: Float = 9f,
    private val textProvider: () -> String,
    private val colorProvider: () -> Int = { 0xFFFFFFFF.toInt() },
    subElementsProvider: (() -> List<Element>) = { emptyList() }
) : Element(xProvider, yProvider, { font.width(textProvider(), fontSize) }, { font.height(fontSize) }, subElementsProvider) {
    var text = textProvider()
    var color = colorProvider()

    override fun measure(offsetX: Float, offsetY: Float) {
        super.measure(offsetX, offsetY)
        text = textProvider()
        color = colorProvider()
    }

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        graphics.text(font, text, offsetX + x, offsetY + y, fontSize, color)
        super.draw(graphics, offsetX, offsetY)
    }
}