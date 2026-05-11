package cicada.client.gui.setting_client.setting_module

import cicada.client.module.Category
import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.Rect
import cicada.client.utils.render.RenderableObject
import cicada.client.utils.render.rect
import net.minecraft.client.gui.GuiGraphicsExtractor

class CategoryRenderer(val category: Category) : RenderableObject {
    override val rect = Rect(0f, 0f, 20f, 20f)
    val modulesRenderers = category.modules.map { ModuleRenderer(it) }

    override fun render(graphics: GuiGraphicsExtractor) {
        if (FrameInput.clicked[0] && rect.isCollide(FrameInput.MPos)) {
            SettingModulesScreen.selectedCategory = this
        }

        val backgroundColor = if (SettingModulesScreen.selectedCategory == this) 0xFFA478E8.toInt() else 0xFFFFD700.toInt()
        graphics.rect(rect, backgroundColor, 0f)
    }
}