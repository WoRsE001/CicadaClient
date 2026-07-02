package cicada.client.render.gui.element.impl

import cicada.client.render.gui.element.Element
import cicada.client.render.rect
import cicada.client.utils.input.FrameInput
import net.minecraft.client.gui.GuiGraphicsExtractor

/**
 * ElementSlider - слайдер для выбора числового значения
 */
class ElementSlider(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    wProvider: () -> Float = { 200f },
    hProvider: () -> Float = { 20f },
    private val min: Float = 0f,
    private val max: Float = 100f,
    private var value: Float = 50f,
    private val colorTrack: Int = 0xFF3C3F41.toInt(),
    private val colorFill: Int = 0xFF4C7EFF.toInt(),
    //private val colorThumb: Int = 0xFFFFFFFF.toInt(),
    private val radius: Float = 4f,
    private val onChange: (Float) -> Unit = {}
) : Element(xProvider, yProvider, wProvider, hProvider) {

    private var isDragging = false

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val ax = offsetX + x
        val ay = offsetY + y

        // Трек слайдера
        graphics.rect(ax, ay, w, h, colorTrack, colorTrack, colorTrack, colorTrack, radius, radius, radius, radius)

        // Заполненная часть
        val fillWidth = ((value - min) / (max - min)) * w
        graphics.rect(ax, ay, fillWidth, h, colorFill, colorFill, colorFill, colorFill, radius, radius, radius, radius)

        super.draw(graphics, offsetX, offsetY)
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        super.handle(offsetX, offsetY)

        val isHovered = isInside(FrameInput.MPos[0], FrameInput.MPos[1], offsetX, offsetY)

        if (FrameInput.clicked[0] && isHovered) {
            isDragging = true
        }

        if (FrameInput.released[0]) {
            val wasDragging = isDragging
            isDragging = false
            return wasDragging
        }

        if (isDragging) {
            val ax = offsetX + x
            val t = ((FrameInput.MPos[0] - ax) / w).coerceIn(0f, 1f)
            value = min + t * (max - min)
            onChange(value)
        }

        return false
    }
}