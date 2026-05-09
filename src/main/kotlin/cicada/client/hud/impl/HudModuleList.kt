package cicada.client.hud.impl

import cicada.client.hud.HUD
import cicada.client.module.Modules
import cicada.client.utils.math.Color4f
import cicada.client.utils.math.gazLarpit
import cicada.client.utils.render.font.ROBOTO_BOLD_FONT
import cicada.client.utils.render.rect
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 25.04.2026 6:54.
object HudModuleList : HUD(0f, 0f, 100f, 100f, "ModuleList") {
    private val width by float("Width", 0f, 0f..10f)
    private val height by float("Height", 0f, 0f..10f)
    private val textOffset by float("Text offset", 0f, -1f..1f)
    private val textSize by float("Text size", 12f, 1f..20f)
    private val backgroundColor = color("Background color", Color4f(0f))
    private val textColor = color("Text color", Color4f(1f))

    private val font = ROBOTO_BOLD_FONT

    override fun render(graphics: GuiGraphicsExtractor) {
        val toggledModules = Modules.list.filter { it.toggled }.sortedByDescending { font.width(it.name, 9f) }
        var offsetY = y

        for (module in toggledModules) {
            val textWidth = font.width(module.name, textSize)
            val textHeight = font.height(textSize)
            val lineWidth = textWidth + width
            val lineHeight = textHeight + height
            val textX = lineWidth / 2 - textWidth / 2
            val textY = gazLarpit((textOffset + 1f) / 2f, 0f, lineHeight - textHeight)

            graphics.rect(x,  offsetY, lineWidth, lineHeight, backgroundColor.inner.toInt())
            font.draw(graphics, module.name, x + textX, offsetY + textY, textSize, textColor.inner.toInt())

            offsetY += lineHeight
        }
    }
}