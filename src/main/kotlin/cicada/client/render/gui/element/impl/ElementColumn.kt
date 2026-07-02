package cicada.client.render.gui.element.impl

import cicada.client.render.gui.element.Element
import net.minecraft.client.gui.GuiGraphicsExtractor

/**
 * ElementColumn - контейнер для вертикального размещения элементов.
 * Раскладка считается в [measure] после замера детей; собственные w/h выводятся из содержимого.
 */
class ElementColumn(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    private val gap: Float = 5f
) : Element(xProvider, yProvider, { 0f }, { 0f }) {

    private val offsetsY = mutableListOf<Float>()

    override fun measure() {
        super.measure()
        offsetsY.clear()
        var cursor = 0f
        var maxW = 0f
        for (child in subElements) {
            offsetsY.add(cursor)
            cursor += child.h + gap
            maxW = maxOf(maxW, child.w)
        }
        w = maxW
        h = if (subElements.isEmpty()) 0f else cursor - gap
    }

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val baseX = offsetX + x
        val baseY = offsetY + y
        subElements.forEachIndexed { i, child ->
            child.draw(graphics, baseX, baseY + offsetsY[i])
        }
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        val baseX = offsetX + x
        val baseY = offsetY + y
        subElements.forEachIndexed { i, child ->
            if (child.handle(baseX, baseY + offsetsY[i])) return true
        }
        return false
    }
}