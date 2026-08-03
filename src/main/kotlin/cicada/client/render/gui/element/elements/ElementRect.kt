package cicada.client.render.gui.element.elements

import cicada.client.render.gui.element.Element
import cicada.client.render.rect
import net.minecraft.client.gui.GuiGraphicsExtractor

class ElementRect(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    wProvider: () -> Float = { 0f },
    hProvider: () -> Float = { 0f },
    private val c0: Int = 0xFFFFFFFF.toInt(),
    private val c1: Int = c0,
    private val c2: Int = c0,
    private val c3: Int = c0,
    private val r0: Float = 0f,
    private val r1: Float = r0,
    private val r2: Float = r0,
    private val r3: Float = r0,
    subElementsProvider: (() -> List<Element>) = { emptyList() }
) : Element(xProvider, yProvider, wProvider, hProvider, subElementsProvider) {

    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        graphics.rect(
            offsetX + x, offsetY + y, w, h,
            c0, c1, c2, c3,
            r0, r1, r2, r3
        )

        super.draw(graphics, offsetX, offsetY)
    }
}
