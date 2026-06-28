package cicada.client.gui

import cicada.client.feature.module.ClientModule
import cicada.client.font.Fonts
import cicada.client.render.rect
import cicada.client.render.engine.text
import cicada.client.render.engine.width
import cicada.client.setting.BooleanValue
import cicada.client.setting.ChoiceValue
import cicada.client.setting.Value
import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.Rect
import net.minecraft.client.gui.GuiGraphicsExtractor

// SCWGxD regrets everything he did. 22.06.2026 16:10.
class ModuleSettingWindow(val clientModule: ClientModule, x: Float, y: Float) {
    private val font = Fonts["jetbrains-mono"]!!

    val rect = Rect(x - 40f, y - 40f, 80f, 80f)
    var xDiff = 0f
    var yDiff = 0f
    var dragging = false

    init {
        SettingScreen.moduleSettingWindows += this
    }

    fun draw(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        graphics.rect(rect.x, rect.y, rect.w, rect.h, 0xFF000000.toInt())
        graphics.rect(
            rect.x, rect.y, rect.w, 10f,
            0xFFFF416C.toInt(), 0xFFFF416C.toInt(), 0xFFFF4B2B.toInt(), 0xFFFF4B2B.toInt()
        )
        graphics.text(font, clientModule.name, rect.x, rect.y, 7f)
        graphics.rect(rect.x + rect.w - 9f, rect.y + 1, 8f, 8f)

        var yOffset = 10f
        for (value in clientModule.inner) {
            yOffset += drawSetting(graphics, value, rect.x, rect.y + yOffset)
        }
    }

    fun handle(): Boolean {
        if (FrameInput.clicked[0] && Rect(rect.x + rect.w - 9f, rect.y + 1, 8f, 8f).isCollide(FrameInput.MPos)) {
            SettingScreen.moduleSettingWindows -= this
            return true
        }

        if (FrameInput.clicked[0] && Rect(rect.x, rect.y, rect.w, 10f).isCollide(FrameInput.MPos)) {
            xDiff = rect.x - FrameInput.MPos[0]
            yDiff = rect.y - FrameInput.MPos[1]
            dragging = true
            SettingScreen.moduleSettingWindows.remove(this)
            SettingScreen.moduleSettingWindows.add(this)
            return true
        }

        if (FrameInput.released[0]) {
            dragging = false
        }

        if (dragging) {
            rect.x = xDiff + FrameInput.MPos[0]
            rect.y = yDiff + FrameInput.MPos[1]
        }

        for (value in clientModule.inner) {
            handleSetting(value)
        }

        if (FrameInput.clicked[0] && rect.isCollide(FrameInput.MPos)) {
            return true
        }

        return false
    }

    private fun handleSetting(value: Value<*>): Boolean {
        return false
    }

    private fun drawSetting(graphics: GuiGraphicsExtractor, value: Value<*>, x: Float, y: Float): Float {
        when (value) {
            is BooleanValue -> {
                graphics.rect(x, y, 6f, 6f)
                graphics.text(font, value.name, x + 7f, y, 5f)
                return 7f + font.width(value.name, 5f)
            }

            is ChoiceValue.Choice -> {
            }
        }

        return 0f
    }
}