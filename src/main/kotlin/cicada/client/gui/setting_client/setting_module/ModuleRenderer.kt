package cicada.client.gui.setting_client.setting_module

import cicada.client.gui.setting_client.values_renderers.convertValue
import cicada.client.module.Module
import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.Rect
import cicada.client.utils.render.RenderableObject
import cicada.client.utils.render.font.INTER_BOLD_FONT
import cicada.client.utils.render.rect
import net.minecraft.client.gui.GuiGraphicsExtractor

class ModuleRenderer(val module: Module) : RenderableObject {
    override val rect = Rect(0f, 0f, 120f, 40f)
    val settingsRenderers = module.inner.map { convertValue(it) }

    override fun render(graphics: GuiGraphicsExtractor) {
        if (rect.isCollide(FrameInput.MPos)) {
            if (FrameInput.clicked[0]) module.toggle()
            if (FrameInput.clicked[1]) SettingModulesScreen.selectedModule = this
        }

        val backgroundColor = if (module.toggled) 0xFFA478E8.toInt() else 0xFFFFD700.toInt()
        graphics.rect(rect, backgroundColor, 0f)
        INTER_BOLD_FONT.centeredDraw(graphics, module.name, rect.x + rect.w / 2, rect.y + rect.h / 2, 9f)
    }
}