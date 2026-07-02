package cicada.client.render.gui.element.impl

import cicada.client.font.FontData
import cicada.client.render.engine.text
import cicada.client.render.gui.element.Element
import cicada.client.render.rect
import cicada.client.utils.input.FrameInput
import net.minecraft.client.gui.GuiGraphicsExtractor

/**
 * ElementMultiChoice - выбор нескольких вариантов из списка (чекбоксы)
 */
class ElementMultiChoice(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    wProvider: () -> Float = { 200f },
    private val options: List<String>,
    private val selected: MutableSet<Int> = mutableSetOf(),
    private val font: FontData? = null,
    private val colorBg: Int = 0xFF3C3F41.toInt(),
    private val colorHover: Int = 0xFF4A4D50.toInt(),
    private val colorChecked: Int = 0xFF4C7EFF.toInt(),
    private val textColor: Int = 0xFFFFFFFF.toInt(),
    private val radius: Float = 4f,
    private val onToggle: (Set<Int>) -> Unit = {}
) : Element(xProvider, yProvider, wProvider, { options.size * ITEM_HEIGHT }) {

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val ax = offsetX + x
        val ay = offsetY + y

        options.forEachIndexed { index, option ->
            val itemY = ay + index * ITEM_HEIGHT
            val mx = FrameInput.MPos[0]
            val my = FrameInput.MPos[1]
            val isHovered = mx >= ax && mx <= ax + w && my >= itemY && my <= itemY + ITEM_HEIGHT

            val itemColor = when {
                index in selected -> colorChecked
                isHovered -> colorHover
                else -> colorBg
            }

            graphics.rect(ax, itemY, w, ITEM_HEIGHT, itemColor, itemColor, itemColor, itemColor, radius, radius, radius, radius)

            if (font != null) {
                val textSize = 14f
                val textX = ax + 8f
                val textY = itemY + ITEM_HEIGHT / 2f - textSize / 2f
                graphics.text(font, option, textX, textY, textSize, textColor)
            }
        }
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        val mx = FrameInput.MPos[0]
        val my = FrameInput.MPos[1]
        val ax = offsetX + x
        val ay = offsetY + y

        options.forEachIndexed { index, _ ->
            val itemY = ay + index * ITEM_HEIGHT
            val itemHovered = mx >= ax && mx <= ax + w && my >= itemY && my <= itemY + ITEM_HEIGHT

            if (itemHovered && FrameInput.clicked[0]) {
                if (index in selected) {
                    selected.remove(index)
                } else {
                    selected.add(index)
                }
                onToggle(selected)
                return true
            }
        }

        return false
    }

    companion object {
        private const val ITEM_HEIGHT = 25f
    }
}