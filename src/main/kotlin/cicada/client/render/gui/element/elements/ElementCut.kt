package cicada.client.render.gui.element.elements

import cicada.client.render.gui.element.Element
import cicada.client.utils.input.FrameInput
import net.minecraft.client.gui.GuiGraphicsExtractor

class ElementCut(
    xProvider: () -> Float = { 0f },
    yProvider: () -> Float = { 0f },
    wProvider: () -> Float = { 100f },
    hProvider: () -> Float = { 100f },
    subElementsProvider: (() -> List<Element>) = { emptyList() }
) : Element(xProvider, yProvider, wProvider, hProvider, subElementsProvider) {
    override fun draw(graphics: GuiGraphicsExtractor, offsetX: Float, offsetY: Float) {
        val ax = x + offsetX
        val ay = y + offsetY

        graphics.enableScissor(ax.toInt(), ay.toInt(), (ax + w).toInt(), (ay + h).toInt())
        super.draw(graphics, offsetX, offsetY)
        graphics.disableScissor()
    }

    override fun handle(offsetX: Float, offsetY: Float): Boolean {
        if (isInside(FrameInput.MPos.x, FrameInput.MPos.y, offsetX, offsetY))
            return super.handle(offsetX, offsetY)

        return false
    }
}