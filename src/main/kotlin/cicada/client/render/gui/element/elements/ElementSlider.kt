package cicada.client.render.gui.element.elements

import cicada.client.render.gui.element.Element
import cicada.client.render.rect
import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.map
import net.minecraft.client.gui.GuiGraphicsExtractor

class ElementSlider(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    wProvider: () -> Float = { 200f },
    hProvider: () -> Float = { 8f },
    private val radiusProvider: () -> Float = { 14f },
    private val minProvider: () -> Float = { 0f },
    private val maxProvider: () -> Float = { 100f },
    private var valueProvider: () -> Float = { 50f },
    private val colorBg: Int = 0xFF4C7EFF.toInt(),
    private val colorFill: Int = 0xFF4C7EFF.toInt(),
    private val colorSlider: Int = 0xFF3C3F41.toInt(),
    private val onChange: (Float) -> Unit = {}
) : Element(xProvider, yProvider, wProvider, hProvider) {

    var radius = radiusProvider()
    var min = minProvider()
    var max = maxProvider()
    var value = valueProvider()
    private var isHovered = false
    private var isDragging = false

    override fun measure(offsetX: Float, offsetY: Float) {
        super.measure(offsetX, offsetY)

        isHovered = isInside(FrameInput.MPos[0], FrameInput.MPos[1], offsetX, offsetY)
    }

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val ax = offsetX + x
        val ay = offsetY + y

        graphics.rect(ax, ay, w, h, colorBg, r0 = h / 2)

        val fillWidth = value.map(min, max, w / 20, w)
        graphics.rect(ax, ay, fillWidth, h, colorFill, r0 = h / 2)

        super.draw(graphics, offsetX, offsetY)
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        if (super.handle(offsetX, offsetY)) return true

        val ax = x + offsetX

        if (FrameInput.pressed[0] && isHovered) {
            isDragging = true
        }

        if (FrameInput.released[0]) {
            isDragging = false
        }

        if (isDragging) {
            onChange(
                FrameInput.MPos.x.map(
                    ax + 10f, ax + w, min, max
                ).coerceIn(min, max)
            )

            if (FrameInput.clicked[0]) {
                return true
            }
        }

        return false
    }
}