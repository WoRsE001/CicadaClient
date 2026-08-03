package cicada.client.render.gui.element.elements

import cicada.client.font.FontData
import cicada.client.render.engine.centeredText
import cicada.client.render.gui.element.Element
import cicada.client.render.rect
import cicada.client.utils.input.FrameInput
import net.minecraft.client.gui.GuiGraphicsExtractor

class ElementButton(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    wProvider: () -> Float = { 100f },
    hProvider: () -> Float = { 30f },
    private val colorNormal: () -> IntArray = { IntArray(4) { 0xFF000000.toInt() } },
    private val colorHover: () -> IntArray = colorNormal,
    private val colorPressed: () -> IntArray = colorHover,
    private val radiiProvider: () -> FloatArray = { FloatArray(4) { 0f } },
    private val fontProvider: () -> FontData? = { null },
    private val fontSizeProvider: () -> Float = { 9f },
    private val textProvider: () -> String = { "" },
    private val textColorProvider: () -> Int = { 0xFFFFFFFF.toInt() },
    private val onClick: (Int) -> Unit = {},
    subElementsProvider: (() -> List<Element>) = { emptyList() }
) : Element(xProvider, yProvider, wProvider, hProvider, subElementsProvider) {
    private var color = colorNormal()
    private var radii = radiiProvider()
    private var font = fontProvider()
    private var fontSize = fontSizeProvider()
    private var text = textProvider()
    private var textColor = textColorProvider()
    private var isHovered = false

    override fun measure(offsetX: Float, offsetY: Float) {
        super.measure(offsetX, offsetY)

        isHovered = isInside(FrameInput.MPos.x, FrameInput.MPos.y, offsetX, offsetY)

        color = when {
            FrameInput.isPressed() && isHovered -> colorPressed()
            isHovered -> colorHover()
            else -> colorNormal()
        }

        radii = radiiProvider()
        font = fontProvider()
        fontSize = fontSizeProvider()
        text = textProvider()
        textColor = textColorProvider()
    }

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val ax = offsetX + x
        val ay = offsetY + y

        graphics.rect(
            ax, ay, w, h,
            color[0], color[1], color[2], color[3],
            radii[0], radii[1], radii[2], radii[3]
        )

        if (font != null && text.isNotEmpty()) {
            graphics.centeredText(font, text, ax + w / 2, ay + h / 2, fontSize, textColor)
        }

        super.draw(graphics, offsetX, offsetY)
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        if (super.handle(offsetX, offsetY)) return true

        if (isHovered && FrameInput.isClicked()) {
            var mouseButton = 0

            for (i in 0..2) {
                if (FrameInput.clicked[i])
                    mouseButton = i
            }

            onClick(mouseButton)
            return true
        }

        return false
    }
}