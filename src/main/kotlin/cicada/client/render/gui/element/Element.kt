package cicada.client.render.gui.element

import net.minecraft.client.gui.GuiGraphicsExtractor

/**
 * Element - базовый класс для всех UI элементов
 */
abstract class Element(
    private val xProvider: () -> Float = { 0f },
    private val yProvider: () -> Float = { 0f },
    private val wProvider: () -> Float = { 0f },
    private val hProvider: () -> Float = { 0f }
) {
    var x = 0f; protected set
    var y = 0f; protected set
    var w = 0f; protected set
    var h = 0f; protected set

    var parent: Element? = null
        private set

    /**
     * Ключ для сопоставления при пересборке динамических детей.
     * Инстансы с одинаковым ключом переиспользуются между кадрами — так сохраняется
     * состояние интерактивных элементов (перетаскивание слайдера, раскрытие дропдауна, зажатие кнопки).
     */
    var key: Any? = null
        private set

    protected val subElements = mutableListOf<Element>()

    private var childrenProvider: (() -> List<Element>)? = null

    /** Задаёт ключ для реконсиляции. Возвращает этот же элемент для чейнинга. */
    fun key(key: Any?): Element {
        this.key = key
        return this
    }

    /** Добавляет статического дочернего элемента. Возвращает этого же родителя для чейнинга. */
    fun add(child: Element): Element {
        child.parent = this
        subElements.add(child)
        return this
    }

    /**
     * Задаёт динамический список детей: [provider] вызывается каждый кадр в [measure],
     * поэтому состав дерева может меняться в зависимости от состояния без внешней пересборки.
     * Дети с совпадающим [key] переиспользуются (сохраняют состояние), остальные создаются заново.
     * Возвращает этого же родителя для чейнинга.
     */
    fun children(provider: () -> List<Element>): Element {
        childrenProvider = provider
        return this
    }

    /** Пересобирает [subElements] из [childrenProvider], переиспользуя старые инстансы по ключу. */
    private fun rebuildChildren() {
        val provider = childrenProvider ?: return
        val reusable = subElements.filter { it.key != null }.associateBy { it.key }
        subElements.clear()
        for (built in provider()) {
            val child = built.key?.let { reusable[it] } ?: built
            child.parent = this
            subElements.add(child)
        }
    }

    open fun measure() {
        rebuildChildren()
        x = xProvider()
        y = yProvider()
        w = wProvider()
        h = hProvider()
        subElements.forEach { it.measure() }
    }

    /** Рисует элемент с учётом смещения родителя */
    open fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        subElements.forEach { it.draw(graphics, x + offsetX, y + offsetY) }
    }

    /** Обрабатывает ввод с учётом смещения родителя. Возвращает true, если элемент был взаимодействован */
    open fun handle(offsetX: Float, offsetY: Float): Boolean {
        subElements.reversed().forEach { if (it.handle(x + offsetX, y + offsetY)) return true }
        return false
    }

    /** Проверяет, находится ли точка внутри элемента */
    protected fun isInside(mx: Float, my: Float, offsetX: Float, offsetY: Float): Boolean {
        val ax = offsetX + x
        val ay = offsetY + y
        return mx >= ax && mx <= ax + w && my >= ay && my <= ay + h
    }
}