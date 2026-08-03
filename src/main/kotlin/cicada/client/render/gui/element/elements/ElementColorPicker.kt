package cicada.client.render.gui.element.elements

import cicada.client.render.gui.element.Element
import cicada.client.render.rect
import cicada.client.utils.input.FrameInput
import net.minecraft.client.gui.GuiGraphicsExtractor
import kotlin.math.abs

/**
 * ElementColorPicker - выбор цвета
 */
class ElementColorPicker(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    wProvider: () -> Float = { 200f },
    hProvider: () -> Float = { 200f },
    private var color: Int = 0xFFFF0000.toInt(),
    private val onChange: (Int) -> Unit = {},
    subElementsProvider: (() -> List<Element>) = { emptyList() }
) : Element(xProvider, yProvider, wProvider, hProvider, subElementsProvider) {

    private var isDragging = false

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val ax = offsetX + x
        val ay = offsetY + y

        // Рисуем цветовое поле (упрощённая версия)
        val steps = 20
        val stepW = w / steps
        val stepH = h / steps

        for (i in 0 until steps) {
            for (j in 0 until steps) {
                val hue = (i.toFloat() / steps) * 360f
                val sat = j.toFloat() / steps
                val rgb = hsvToRgb(hue, sat, 1f)

                val px = ax + i * stepW
                val py = ay + j * stepH
                graphics.rect(px, py, stepW, stepH, rgb, rgb, rgb, rgb, 0f, 0f, 0f, 0f)
            }
        }

        // Показываем текущий выбранный цвет
        graphics.rect(ax, ay + h + 10f, w, 30f, color, color, color, color, 4f, 4f, 4f, 4f)

        super.draw(graphics, offsetX, offsetY)
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        if (super.handle(offsetX, offsetY)) return true

        val mx = FrameInput.MPos[0]
        val my = FrameInput.MPos[1]

        val isHovered = isInside(mx, my, offsetX, offsetY)

        if (FrameInput.pressed[0] && isHovered) {
            isDragging = true
            return true
        }

        if (FrameInput.released[0]) {
            val wasDragging = isDragging
            isDragging = false
            return wasDragging
        }

        if (isDragging) {
            val ax = offsetX + x
            val ay = offsetY + y
            val tx = ((mx - ax) / w).coerceIn(0f, 1f)
            val ty = ((my - ay) / h).coerceIn(0f, 1f)

            val hue = tx * 360f
            val sat = ty
            color = hsvToRgb(hue, sat, 1f)
            onChange(color)
            return true
        }

        return false
    }

    private fun hsvToRgb(h: Float, s: Float, v: Float): Int {
        val c = v * s
        val x = c * (1 - abs((h / 60f) % 2 - 1))
        val m = v - c

        val (r, g, b) = when {
            h < 60 -> Triple(c, x, 0f)
            h < 120 -> Triple(x, c, 0f)
            h < 180 -> Triple(0f, c, x)
            h < 240 -> Triple(0f, x, c)
            h < 300 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        val red = ((r + m) * 255).toInt().coerceIn(0, 255)
        val green = ((g + m) * 255).toInt().coerceIn(0, 255)
        val blue = ((b + m) * 255).toInt().coerceIn(0, 255)

        return (0xFF shl 24) or (red shl 16) or (green shl 8) or blue
    }
}