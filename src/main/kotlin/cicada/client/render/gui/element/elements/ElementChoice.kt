package cicada.client.render.gui.element.elements

import cicada.client.font.FontData
import cicada.client.render.engine.height
import cicada.client.render.engine.text
import cicada.client.render.gui.element.Element
import cicada.client.render.rect
import cicada.client.utils.input.FrameInput
import net.minecraft.client.gui.GuiGraphicsExtractor

class ElementChoice(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    wProvider: () -> Float = { 200f },
    hProvider: () -> Float = { 50f },
    val itemHeightProvider: () -> Float = { 50f },
    private val optionsProvider: () -> List<String>,
    private var selectedProvider: () -> Int = { 0 },
    private val colorBgProvider: () -> IntArray = { IntArray(4) { 0xFF000000.toInt() } },
    private val colorHoverProvider: () -> IntArray = colorBgProvider,
    private val colorSelectedProvider: () -> IntArray = colorHoverProvider,
    private val radiiProvider: () -> FloatArray = { FloatArray(4) { 4f } },
    private val fontProvider: () -> FontData,
    private val fontSizeProvider: () -> Float = { 9f },
    private val textColorProvider: () -> Int = { 0xFFFFFFFF.toInt() },
    private val onSelect: (Int) -> Unit = {},
    subElementsProvider: (() -> List<Element>) = { emptyList() }
) : Element(xProvider, yProvider, wProvider, hProvider, subElementsProvider) {

    private var itemHeight = itemHeightProvider()
    private var options = optionsProvider()
    private var selected = selectedProvider()
    private var colorBg = colorBgProvider()
    private var colorHover = colorHoverProvider()
    private var colorSelected = colorSelectedProvider()
    private var radii = radiiProvider()
    private var font = fontProvider()
    private var fontSize = fontSizeProvider()
    private var textColor = textColorProvider()
    private var expanded = false
    private var isHovered = false

    override fun measure(offsetX: Float, offsetY: Float) {
        super.measure(offsetX, offsetY)

        itemHeight = itemHeightProvider()
        options = optionsProvider()
        selected = selectedProvider()
        colorBg = colorBgProvider()
        colorHover = colorHoverProvider()
        colorSelected = colorSelectedProvider()
        radii = radiiProvider()
        font = fontProvider()
        fontSize = fontSizeProvider()
        textColor = textColorProvider()
        if (expanded) h += itemHeight * (options.size - 1)

        isHovered = isInside(FrameInput.MPos.x, FrameInput.MPos.y, offsetX, offsetY)
    }

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val ax = offsetX + x
        val ay = offsetY + y

        val mainColor = if (isHovered && !expanded) colorHover else colorBg
        graphics.rect(
            ax, ay, w, h,
            mainColor[0], mainColor[1], mainColor[2], mainColor[3],
            radii[0], radii[1], radii[2], radii[3]
        )

        val height = hProvider()
        graphics.text(font, options[selected], ax + 3, ay + height / 2 - font.height(fontSize) / 2, fontSize, textColor)

        var offset = height
        for (option in options) {
            if (options[selected] == option) continue

            graphics.text(font, option, ax + 3, ay + offset + itemHeight / 2 - font.height(fontSize) / 2, fontSize, textColor)

            offset += itemHeight
        }

        super.draw(graphics, offsetX, offsetY)
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        if (super.handle(offsetX, offsetY)) return true

        val mx = FrameInput.MPos[0]
        val my = FrameInput.MPos[1]

        val ax = offsetX + x
        val ay = offsetY + y

        val height = hProvider()
        if (isInside(mx, my, ax + 3, ay, w, height)) {
            expanded = !expanded
            return true
        }

        var offset = height
        for (i in 0 ..<options.size) {
            if (selected == i) continue

            if (isInside(mx, my, ax + 3, ay + height + offset, w, itemHeight)) {
                expanded = !expanded
                onSelect(selected)
                return true
            }

            offset += itemHeight
        }

        return false
    }
}