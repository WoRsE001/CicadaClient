package cicada.client.hud.impl

import cicada.client.hud.HUD
import cicada.client.module.Modules
import cicada.client.utils.render.font.ROBOTO_BOLD_FONT
import cicada.client.utils.render.rect
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 25.04.2026 6:54.
object HudModuleList : HUD(0f, 0f, 100f, 100f, "ModuleList") {
    val font = ROBOTO_BOLD_FONT

    override fun render(graphics: GuiGraphicsExtractor) {
        var offsetY = 0f
        for (module in Modules.list.filter { it.toggled }.sortedByDescending { font.width(it.name, 9f) }) {
            graphics.rect(x, y + offsetY, font.width(module.name, 9f), font.height(9f), 0xFF000000.toInt())
            font.draw(graphics, module.name, x, y + offsetY, 9f, -1)
            offsetY += font.height(9f)
        }
    }
}