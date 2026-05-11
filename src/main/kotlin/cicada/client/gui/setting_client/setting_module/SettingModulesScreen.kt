package cicada.client.gui.setting_client.setting_module

import cicada.client.module.Category
import cicada.client.setting.Value
import cicada.client.utils.math.Rect
import cicada.client.utils.render.RenderableObject
import cicada.client.utils.render.font.INTER_BOLD_FONT
import cicada.client.utils.render.rect
import net.minecraft.client.gui.GuiGraphicsExtractor

object SettingModulesScreen : RenderableObject {
    override val rect = Rect(0f, 0f, 400f, 225f)
    val categoriesRenderers = Category.entries.map { CategoryRenderer(it) }
    var selectedCategory: CategoryRenderer? = null
    var selectedModule: ModuleRenderer? = null

    override fun render(graphics: GuiGraphicsExtractor) {
        var xOffset: Float
        var yOffset: Float

        graphics.rect(rect, 0xFF2E1A47.toInt(), 0f)

        yOffset = 0f
        for (categoryRenderer in categoriesRenderers) {
            categoryRenderer.rect.x = rect.x
            categoryRenderer.rect.y = rect.y + yOffset
            categoryRenderer.render(graphics)
            yOffset += categoryRenderer.rect.h
        }

        if (selectedCategory != null) {
            yOffset = 0f
            for (moduleRenderer in selectedCategory!!.modulesRenderers) {
                moduleRenderer.rect.x = rect.x + selectedCategory!!.rect.w
                moduleRenderer.rect.y = rect.y + yOffset
                moduleRenderer.render(graphics)
                yOffset += moduleRenderer.rect.h
            }
        }

        if (selectedModule != null) {
            INTER_BOLD_FONT.draw(graphics, selectedModule!!.module.name, selectedModule!!.rect.x + selectedModule!!.rect.w, 0f, 12f)

            yOffset = 0f
            for (settingRenderer in selectedModule!!.settingsRenderers) {
                settingRenderer.rect.x = selectedModule!!.rect.x + selectedModule!!.rect.w
                settingRenderer.rect.y = INTER_BOLD_FONT.height(12f)
                settingRenderer.render(graphics)
                yOffset += settingRenderer.rect.h
            }
        }
    }
}