package cicada.client.render.gui.element.impl

import cicada.client.font.FontData
import cicada.client.render.engine.text
import cicada.client.render.gui.element.Element
import cicada.client.render.rect
import cicada.client.utils.input.FrameInput
import net.minecraft.client.gui.GuiGraphicsExtractor

/**
 * ElementButton - кнопка с обработкой нажатий
 */
class ElementButton(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    wProvider: () -> Float = { 100f },
    hProvider: () -> Float = { 30f },
    private val font: FontData? = null,
    private val fontSize: Float = 9f,
    // Реактивные провайдеры: инстанс переиспользуется между кадрами, а цвет/текст пересчитываются из текущего состояния.
    private val textProvider: () -> String = { "" },
    private val colorNormal: () -> Int = { 0xFF3C3F41.toInt() },
    private val colorHover: () -> Int = { 0xFF4A4D50.toInt() },
    private val colorPressed: () -> Int = { 0xFF2B2D30.toInt() },
    private val textColor: Int = 0xFFFFFFFF.toInt(),
    private val radius: Float = 0f,
    private val onClick: () -> Unit = {},
    private val onRightClick: () -> Unit = {}
) : Element(xProvider, yProvider, wProvider, hProvider) {

    private var isHovered = false
    private var wasPressed = false
    private var wasRightPressed = false

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val ax = offsetX + x
        val ay = offsetY + y

        // Определяем цвет
        val color = when {
            FrameInput.pressed[0] && isHovered -> colorPressed()
            isHovered -> colorHover()
            else -> colorNormal()
        }

        // Рисуем фон кнопки
        graphics.rect(ax, ay, w, h, color, color, color, color, radius, radius, radius, radius)

        // Рисуем текст (если есть шрифт)
        val text = textProvider()
        if (font != null && text.isNotEmpty()) {
            val textX = ax + w / 2f - text.length * fontSize * 0.3f
            val textY = ay + h / 2f - fontSize / 2f
            graphics.text(font, text, textX, textY, fontSize, textColor)
        }

        super.draw(graphics, offsetX, offsetY)
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        super.handle(offsetX, offsetY)

        val mx = FrameInput.MPos[0]
        val my = FrameInput.MPos[1]

        isHovered = isInside(mx, my, offsetX, offsetY)

        // Обработка клика
        if (isHovered && FrameInput.pressed[0]) {
            wasPressed = true
            return true
        }

        if (wasPressed && FrameInput.released[0]) {
            if (isHovered) {
                onClick()
            }
            wasPressed = false
            return true
        }

        // Обработка правого клика
        if (isHovered && FrameInput.pressed[1]) {
            wasRightPressed = true
            return true
        }

        if (wasRightPressed && FrameInput.released[1]) {
            if (isHovered) {
                onRightClick()
            }
            wasRightPressed = false
            return true
        }

        return isHovered && (FrameInput.clicked[0] || FrameInput.clicked[1])
    }
}