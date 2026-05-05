package cicada.client.hud.impl

import cicada.client.hud.HUD
import cicada.client.utils.mc
import cicada.client.utils.render.rect
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 03.04.2026 2:11.
object HudKeystrokes : HUD(0f, 0f, 150f, 150f, "Keystrokes", false) {
    override fun render(graphics: GuiGraphicsExtractor) {
        renderElement(graphics, x + 50, y, 50f, 50f, mc.options.keyUp.isDown, "W")
        renderElement(graphics, x + 100, y + 50, 50f, 50f, mc.options.keyRight.isDown, "D")
        renderElement(graphics, x + 50, y + 50, 50f, 50f, mc.options.keyDown.isDown, "S")
        renderElement(graphics, x, y + 50, 50f, 50f, mc.options.keyLeft.isDown, "A")
        renderElement(graphics, x, y + 100, 75f, 50f, mc.options.keyAttack.isDown, "LMB")
        renderElement(graphics, x + 75, y + 100, 75f, 50f, mc.options.keyUse.isDown, "RMB")
    }

    private fun renderElement(graphics: GuiGraphicsExtractor, x: Float, y: Float, w: Float, h: Float, toggled: Boolean, text: String) {
        graphics.rect(x, y, w, h, if (toggled) 0x80FFFFFF.toInt() else 0x80000000.toInt())
        //graphics.drawText(text, x + w / 2 - mc.font.width(text) / 2, y + h / 2 - 5, -1)
    }
}