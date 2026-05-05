package cicada.client.gui

import cicada.client.hud.HUDs
import cicada.client.utils.input.FrameInput
import cicada.client.utils.mc
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

// SCWGxD regrets everything he did. 03.04.2026 8:51.
object HUDEditor : Screen(Component.empty()) {
    var offsetX = 0f
    var offsetY = 0f

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        for (HUD in HUDs.HUDs.reversed()) {
            if (
                FrameInput.clicked[0] &&
                FrameInput.MPos.x in HUD.x..(HUD.x + HUD.w) &&
                FrameInput.MPos.y in HUD.y..(HUD.y + HUD.h)
            ) {
                HUD.isDragging = true
                offsetX = HUD.x - FrameInput.MPos.x
                offsetY = HUD.y - FrameInput.MPos.y
                break
            }

            if (HUD.isDragging && FrameInput.released[0])
                HUD.isDragging = false

            if (HUD.isDragging) {
                HUD.x = (FrameInput.MPos.x + offsetX).coerceIn(0f, mc.window.width - HUD.w)
                HUD.y = (FrameInput.MPos.y + offsetY).coerceIn(0f, mc.window.height - HUD.h)
            }
        }

        for (HUD in HUDs.HUDs) {
            HUD.render(graphics)
        }
    }
}