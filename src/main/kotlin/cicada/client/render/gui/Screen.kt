package cicada.client.render.gui

import cicada.client.render.gui.element.Element
import net.minecraft.client.gui.GuiGraphicsExtractor

class Screen {
    val elements = mutableListOf<Element>()

    fun measure() {
        elements.forEach { it.measure(0f, 0f) }
    }

    fun handle() {
        elements.forEach { if (it.handle(0f, 0f)) return }
    }

    fun draw(graphics: GuiGraphicsExtractor) {
        elements.forEach { it.draw(graphics, 0f, 0f) }
    }

    fun add(element: Element) {
        elements.add(element)
    }
}