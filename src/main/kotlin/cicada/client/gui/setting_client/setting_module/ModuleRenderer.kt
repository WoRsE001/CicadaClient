package cicada.client.gui.setting_client.setting_module

import cicada.client.feature.module.ClientModule
import cicada.client.render.height
import cicada.client.render.text
import cicada.client.render.rect
import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.Rect
import cicada.client.utils.render.RenderableObject
import net.minecraft.client.gui.GuiGraphicsExtractor

class ModuleRenderer(val module: ClientModule) : RenderableObject {
    override val rect = Rect(0f, 0f, 200f, 50f)
    val font = SettingModulesScreen.font

    override fun render(graphics: GuiGraphicsExtractor) {
        rect.w = 200f
        rect.h = 50f
        if (rect.isCollide(FrameInput.MPos)) {
            if (FrameInput.clicked[0]) module.toggle()
            if (FrameInput.clicked[1]) SettingModulesScreen.selectedModule = this
        }

        val secondColor = if (module.toggled) 0xFF2E4A3F.toInt() else 0xFF802B2B.toInt()
        graphics.rect(rect.x, rect.y, rect.w, rect.h, 0xFF3A3A3A.toInt(), 0xFF3A3A3A.toInt(), secondColor, secondColor, 5f, 5f, 5f, 5f)
        graphics.text(font, module.name, rect.x + 10f, rect.y + rect.h / 2 - font.height(9f) / 2, 9f)
    }
}