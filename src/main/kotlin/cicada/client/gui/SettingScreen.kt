package cicada.client.gui

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.font.Fonts
import cicada.client.render.engine.centeredText
import cicada.client.render.cut
import cicada.client.render.rect
import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.Rect
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

// SCWGxD regrets everything he did. 21.06.2026 20:40.
object SettingScreen : Screen(Component.literal("")) {
    private val font = Fonts["jetbrains-mono"]!!

    val moduleSettingWindows = mutableSetOf<ModuleSettingWindow>()

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        handle()

        var offsetX = 2.5f
        for (moduleCategory in ModuleCategory.entries) {
            drawModuleCategory(graphics, moduleCategory, offsetX, 60f)
            offsetX += 80f
        }

        for (window in moduleSettingWindows) {
            window.draw(graphics, mouseX, mouseY, a)
        }
    }

    private fun handle() {
        for (window in moduleSettingWindows.reversed()) {
            if (window.handle()) return
        }

        var offsetX = 2.5f
        for (moduleCategory in ModuleCategory.entries) {
            if (handleModuleCategory(moduleCategory, offsetX, 60f)) return
            offsetX += 80f
        }
    }

    private fun handleModuleCategory(moduleCategory: ModuleCategory, x: Float, y: Float): Boolean {
        val rect = Rect(x, y + 20f, 75f, 130f)

        if (rect.isCollide(FrameInput.MPos)) {
            var offsetY = 0f
            for (clientModule in moduleCategory.modules) {
                if (handleClientModule(clientModule, rect.x, rect.y + offsetY)) return true
                offsetY += 20f
            }

            return true
        }

        return false
    }

    private fun handleClientModule(clientModule: ClientModule, x: Float, y: Float): Boolean {
        val rect = Rect(x, y, 75f, 20f)

        if (rect.isCollide(FrameInput.MPos)) {
            if (FrameInput.clicked[0]) {
                clientModule.toggle()
            }

            if (FrameInput.clicked[1]) {
                ModuleSettingWindow(clientModule, width / 2f, height / 2f)
            }

            return true
        }

        return false
    }

    private fun drawModuleCategory(graphics: GuiGraphicsExtractor, moduleCategory: ModuleCategory, x: Float, y: Float) {
        val titleRect = Rect(0f, 0f, 75f, 20f)
        val rect = Rect(x, y, 75f, 150f)

        graphics.rect(rect.x, rect.y, rect.w, rect.h, 0xFF000000.toInt())
        graphics.rect(
            rect.x + titleRect.x, rect.y + titleRect.y, titleRect.w, titleRect.h,
            0xFFFF416C.toInt(), 0xFFFF416C.toInt(), 0xFFFF4B2B.toInt(), 0xFFFF4B2B.toInt()
        )
        graphics.centeredText(
            font, moduleCategory.name,
            rect.x + titleRect.x + titleRect.w / 2,
            rect.y + titleRect.y + titleRect.h / 2,
            9f
        )

        graphics.cut(rect.x, rect.y, rect.w, rect.h) {
            var offsetY = titleRect.h
            for (clientModule in moduleCategory.modules) {
                drawClientModule(graphics, clientModule, rect.x, rect.y + offsetY)
                offsetY += 20f
            }
        }
    }

    private fun drawClientModule(graphics: GuiGraphicsExtractor, clientModule: ClientModule, x: Float, y: Float) {
        val rect = Rect(x, y, 75f, 20f)

        if (clientModule.toggled) {
            graphics.rect(
                rect.x + 2, rect.y + 2, rect.w - 4, rect.h - 4,
                0xFFFF416C.toInt(), 0xFFFF416C.toInt(), 0xFFFF4B2B.toInt(), 0xFFFF4B2B.toInt()
            )
        } else {
            graphics.rect(rect.x + 2, rect.y + 2, rect.w - 4, rect.h - 4, 0xFF000000.toInt())
        }

        graphics.centeredText(
            font, clientModule.name,
            rect.x + rect.w / 2,
            rect.y + rect.h / 2,
            5f
        )
    }
}