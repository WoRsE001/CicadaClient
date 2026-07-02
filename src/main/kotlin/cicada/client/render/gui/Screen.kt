package cicada.client.render.gui

import cicada.client.render.gui.element.Element
import net.minecraft.client.gui.GuiGraphicsExtractor

/**
 * Screen - контейнер для UI элементов.
 * Можно "собрать" один раз, затем многократно рисовать и обрабатывать.
 */
class Screen {
    val elements = mutableListOf<Element>()

    fun measure() {
        elements.forEach { it.measure() }
    }

    /** Обрабатывает нажатия на элементы. Возвращает true, если хотя бы один элемент был взаимодействован */
    fun handle() {
        elements.forEach { if (it.handle(0f, 0f)) return }
    }

    /** Рисует все элементы */
    fun draw(graphics: GuiGraphicsExtractor) {
        elements.forEach { it.draw(graphics, 0f, 0f) }
    }

    /** Добавить элемент */
    fun add(element: Element) {
        elements.add(element)
    }
}