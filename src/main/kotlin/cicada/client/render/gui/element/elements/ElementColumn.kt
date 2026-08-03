package cicada.client.render.gui.element.elements

import cicada.client.render.gui.element.Element
import net.minecraft.client.gui.GuiGraphicsExtractor

class ElementColumn(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    private val gap: Float = 5f,
    subElementsProvider: (() -> List<Element>) = { emptyList() }
) : Element(xProvider, yProvider, { 0f }, { 0f }, subElementsProvider) {

    override fun measure(offsetX: Float, offsetY: Float) {
        x = xProvider()
        y = yProvider()
        w = wProvider()
        h = hProvider()

        subElements = subElementsProvider()

        var cursor = 0f
        var maxW = 0f
        for (subElement in subElements) {
            subElement.measure(x + offsetX, y + offsetY + cursor)
            cursor += subElement.h + gap
            maxW = maxOf(maxW, subElement.w)
        }
        w = maxW
        h = if (subElements.isEmpty()) 0f else cursor - gap
    }

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val ax = offsetX + x
        val ay = offsetY + y

        var cursor = 0f
        for (subElement in subElements) {
            subElement.draw(graphics, ax, ay + cursor)
            cursor += subElement.h + gap
        }
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        val ax = offsetX + x
        val ay = offsetY + y

        var cursor = 0f
        for (subElement in subElements) {
            if (subElement.handle(ax, ay + cursor)) return true
            cursor += subElement.h + gap
        }

        return false
    }
}