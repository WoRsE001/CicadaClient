package cicada.client.feature.hud.huds

import cicada.client.feature.hud.HUD
import cicada.client.feature.module.ModuleManager
import cicada.client.font.Fonts
import cicada.client.render.engine.height
import cicada.client.render.rect
import cicada.client.render.engine.text
import cicada.client.render.engine.width
import cicada.client.utils.math.Color4f
import cicada.client.utils.math.gazLarpit
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 25.04.2026 6:54.
object HudModuleList : HUD(0f, 0f, 100f, 100f, "ModuleList", true) {
    private val width by float("Width", 0f, 0f..10f)
    private val height by float("Height", 0f, 0f..10f)
    private val textOffset by float("Text offset", 0f, -1f..1f)
    private val textSize by float("Text size", 12f, 1f..20f)
    private val backgroundColor = color("Background color", Color4f(0f))
    private val textColor = color("Text color", Color4f(1f))

    private val font = Fonts["roboto-bold"]!!

    override fun render(graphics: GuiGraphicsExtractor) {
        val toggledModules = ModuleManager.filter { it.toggled }.sortedByDescending { font.width(it.name, 9f) }
        var offsetY = y

        for (module in toggledModules) {
            val textWidth = font.width(module.name, textSize)
            val textHeight = font.height(textSize)
            val lineWidth = textWidth + width
            val lineHeight = textHeight + height
            val textX = lineWidth / 2 - textWidth / 2
            val textY = gazLarpit((textOffset + 1f) / 2f, 0f, lineHeight - textHeight)

            graphics.rect(x, offsetY, lineWidth, lineHeight, backgroundColor.inner.toInt())
            graphics.text(font, module.name, x + textX, offsetY + textY, textSize, textColor.inner.toInt())

            offsetY += lineHeight
        }
    }
}