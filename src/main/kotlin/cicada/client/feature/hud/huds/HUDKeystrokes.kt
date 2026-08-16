package cicada.client.feature.hud.huds

import cicada.client.feature.hud.HUD
import cicada.client.render.rect
import cicada.client.utils.client.mc
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 03.04.2026 2:11.
object HUDKeystrokes : HUD(0f, 0f, 150f, 150f, "Keystrokes", false) {
    override fun render(graphics: GuiGraphicsExtractor) {
        renderElement(graphics, x + 50, y, 50f, 50f, mc.options.keyUp.isDown, "W")
        renderElement(graphics, x + 100, y + 50, 50f, 50f, mc.options.keyRight.isDown, "D")
        renderElement(graphics, x + 50, y + 50, 50f, 50f, mc.options.keyDown.isDown, "S")
        renderElement(graphics, x, y + 50, 50f, 50f, mc.options.keyLeft.isDown, "A")
        renderElement(graphics, x, y + 100, 75f, 50f, mc.options.keyAttack.isDown, "LMB")
        renderElement(graphics, x + 75, y + 100, 75f, 50f, mc.options.keyUse.isDown, "RMB")
    }

    override fun renderInHUDEditor(graphics: GuiGraphicsExtractor) {
        render(graphics)
    }

    private fun renderElement(graphics: GuiGraphicsExtractor, x: Float, y: Float, w: Float, h: Float, toggled: Boolean, text: String) {
        graphics.rect(x, y, w, h, if (toggled) 0x80FFFFFF.toInt() else 0x80000000.toInt())
    }
}