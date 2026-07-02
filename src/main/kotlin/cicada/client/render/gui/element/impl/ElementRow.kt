package cicada.client.render.gui.element.impl

import cicada.client.render.gui.element.Element
import net.minecraft.client.gui.GuiGraphicsExtractor

/**
 * ElementRow - контейнер для горизонтального размещения элементов.
 * Раскладка считается в [measure] после замера детей; собственные w/h выводятся из содержимого.
 */
class ElementRow(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    private val gap: Float = 5f
) : Element(xProvider, yProvider, { 0f }, { 0f }) {

    private val offsetsX = mutableListOf<Float>()

    override fun measure() {
        super.measure()
        offsetsX.clear()
        var cursor = 0f
        var maxH = 0f
        for (child in subElements) {
            offsetsX.add(cursor)
            cursor += child.w + gap
            maxH = maxOf(maxH, child.h)
        }
        w = if (subElements.isEmpty()) 0f else cursor - gap
        h = maxH
    }

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val baseX = offsetX + x
        val baseY = offsetY + y
        subElements.forEachIndexed { i, child ->
            child.draw(graphics, baseX + offsetsX[i], baseY)
        }
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        val baseX = offsetX + x
        val baseY = offsetY + y
        subElements.forEachIndexed { i, child ->
            if (child.handle(baseX + offsetsX[i], baseY)) return true
        }
        return false
    }
}