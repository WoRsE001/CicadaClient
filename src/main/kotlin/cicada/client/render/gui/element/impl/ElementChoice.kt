package cicada.client.render.gui.element.impl

import cicada.client.font.FontData
import cicada.client.render.engine.text
import cicada.client.render.gui.element.Element
import cicada.client.render.rect
import cicada.client.utils.input.FrameInput
import net.minecraft.client.gui.GuiGraphicsExtractor

/**
 * ElementChoice - выбор одного варианта из списка
 */
class ElementChoice(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    wProvider: () -> Float = { 200f },
    val itemHeightProvider: () -> Float = { 24f },
    private val options: List<String>,
    private var selected: Int = 0,
    private val font: FontData? = null,
    private val fontSize: Float = 9f,
    private val colorBg: Int = 0xFF3C3F41.toInt(),
    private val colorHover: Int = 0xFF4A4D50.toInt(),
    private val colorSelected: Int = 0xFF4C7EFF.toInt(),
    private val textColor: Int = 0xFFFFFFFF.toInt(),
    private val radius: Float = 4f,
    private val onSelect: (Int) -> Unit = {}
) : Element(xProvider, yProvider, wProvider, { 0f }) {

    private var itemHeight = 0f
    private var isHovered = false
    private var expanded = false

    /** Высота зависит от состояния: свёрнут — одна строка, раскрыт — строка + список опций */
    override fun measure() {
        super.measure()
        itemHeight = itemHeightProvider()
        h = if (expanded) itemHeight + options.size * itemHeight else itemHeight
    }

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val ax = offsetX + x
        val ay = offsetY + y

        // Главный прямоугольник
        val mainColor = if (isHovered && !expanded) colorHover else colorBg
        graphics.rect(ax, ay, w, itemHeight, mainColor, mainColor, mainColor, mainColor, radius, radius, radius, radius)

        // Текст выбранного элемента
        if (font != null && selected in options.indices) {
            val textX = ax + 2f
            val textY = ay + itemHeight / 2f - fontSize / 2f - 1
            graphics.text(font, options[selected], textX, textY, fontSize, textColor)
        }

        // Если раскрыт - показываем опции
        if (expanded) {
            options.forEachIndexed { index, option ->
                val itemY = ay + itemHeight + index * itemHeight
                val itemColor = if (index == selected) colorSelected else colorBg
                graphics.rect(ax, itemY, w, itemHeight, itemColor, itemColor, itemColor, itemColor, radius, radius, radius, radius)

                if (font != null) {
                    val textX = ax + 2f
                    val textY = itemY + itemHeight / 2f - fontSize / 2f - 1
                    graphics.text(font, option, textX, textY, fontSize, textColor)
                }
            }
        }
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        val mx = FrameInput.MPos[0]
        val my = FrameInput.MPos[1]

        val ax = offsetX + x
        val ay = offsetY + y

        // Проверка hover на главной кнопке
        isHovered = mx >= ax && mx <= ax + w && my >= ay && my <= ay + itemHeight

        // Клик по главной кнопке
        if (isHovered && FrameInput.clicked[0]) {
            expanded = !expanded
            return true
        }

        // Клик по опции
        if (expanded) {
            options.forEachIndexed { index, _ ->
                val itemY = ay + itemHeight + index * itemHeight
                val itemHovered = mx >= ax && mx <= ax + w && my >= itemY && my <= itemY + itemHeight

                if (itemHovered && FrameInput.clicked[0]) {
                    selected = index
                    expanded = false
                    onSelect(index)
                    return true
                }
            }
        }

        // Закрыть при клике вне
        if (expanded && FrameInput.clicked[0] && !isHovered) {
            val inExpanded = mx >= ax && mx <= ax + w && my >= ay && my <= ay + h
            if (!inExpanded) {
                expanded = false
                return true
            }
        }

        return expanded || isHovered
    }
}