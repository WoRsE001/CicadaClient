package cicada.client.render.gui.element

import net.minecraft.client.gui.GuiGraphicsExtractor

abstract class Element(
    protected val xProvider: () -> Float = { 0f },
    protected val yProvider: () -> Float = { 0f },
    protected val wProvider: () -> Float = { 0f },
    protected val hProvider: () -> Float = { 0f },
    protected val subElementsProvider: (() -> List<Element>) = { emptyList() }
) {
    var x = xProvider(); protected set
    var y = yProvider(); protected set
    var w = wProvider(); protected set
    var h = hProvider(); protected set

    protected var subElements = emptyList<Element>()

    open fun measure(offsetX: Float, offsetY: Float) {
        x = xProvider()
        y = yProvider()
        w = wProvider()
        h = hProvider()
        subElements = subElementsProvider()
        subElements.forEach { it.measure(x + offsetX, y + offsetY) }
    }

    open fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        subElements.forEach { it.draw(graphics, x + offsetX, y + offsetY) }
    }

    open fun handle(offsetX: Float, offsetY: Float): Boolean {
        subElements.reversed().forEach { if (it.handle(x + offsetX, y + offsetY)) return true }
        return false
    }

    protected fun isInside(mx: Float, my: Float, x: Float, y: Float, w: Float, h: Float): Boolean {
        return mx >= x && mx <= x + w && my >= y && my <= y + h
    }

    protected fun isInside(mx: Float, my: Float, offsetX: Float, offsetY: Float): Boolean {
        val ax = offsetX + x
        val ay = offsetY + y
        return isInside(mx, my, ax, ay, w, h)
    }
}