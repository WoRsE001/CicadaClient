package cicada.client.gui.setting.modules

import cicada.client.CicadaClient
import cicada.client.feature.module.ModuleCategory
import cicada.client.render.rect
import cicada.client.render.sprite
import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.Rect
import cicada.client.utils.render.RenderableObject
import net.minecraft.client.gui.GuiGraphicsExtractor

class CategoryRenderer(val category: ModuleCategory) : RenderableObject {
    override val rect = Rect(0f, 0f, 20f, 20f)
    val modulesRenderers = category.modules.map { ModuleRenderer(it) }

    override fun render(graphics: GuiGraphicsExtractor) {
        if (FrameInput.clicked[0] && rect.isCollide(FrameInput.MPos)) {
            SettingModulesScreen.selectedCategory = this
        }

        graphics.rect(rect.x, rect.y, rect.w, rect.h, 0xFF3A3A3A.toInt(), 5f)
        val iconColor = if (SettingModulesScreen.selectedCategory == this) -1 else 0xFF000000.toInt()
        graphics.sprite(
            CicadaClient.of("images/gui/icons.png"),
            category.ordinal * 24f, 0f, 24f, 24f,
            rect.x, rect.y, rect.w, rect.h, iconColor
        )
    }
}