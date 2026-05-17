package cicada.client.gui.setting_client.setting_module

import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.ModuleManager
import cicada.client.utils.math.Rect
import cicada.client.utils.mc
import cicada.client.utils.render.RenderableObject
import cicada.client.utils.render.rect
import net.minecraft.client.gui.GuiGraphicsExtractor

object SettingModulesScreen : RenderableObject {
    override val rect = Rect(0f, 0f, 800f, 450f)
    val categoriesRenderers = ModuleCategory.entries.map { CategoryRenderer(it) }
    val categoryPanelRect = Rect(5f, 5f, 40f, 440f)
    val mainPanelRect = Rect(categoryPanelRect.x + categoryPanelRect.w + 5f, 40f, 790f - categoryPanelRect.x - categoryPanelRect.w, 405f)
    var selectedCategory: CategoryRenderer? = null
    var selectedModule: ModuleRenderer? = null

    init {
        rect.x = mc.window.guiScaledWidth / 2 - rect.w / 2
        rect.y = mc.window.guiScaledHeight / 2 - rect.h / 2
    }

    override fun render(graphics: GuiGraphicsExtractor) {
        var xOffset = 0f
        var yOffset = 0f

        graphics.rect(rect, 0x501C1C1C, 20f)

        graphics.rect(rect.x + categoryPanelRect.x, rect.y + categoryPanelRect.y, categoryPanelRect.w, categoryPanelRect.h,
            0xFF1F1F1F.toInt(), 0xFF1F1F1F.toInt(), 0xFF1F1F1F.toInt(), 0xFF1F1F1F.toInt(),
            15f, 15f, 0f, 0f
        )

        yOffset = 50f
        for (categoryRenderer in categoriesRenderers) {
            categoryRenderer.rect.x = rect.x + categoryPanelRect.x + 10f
            categoryRenderer.rect.y = rect.y + categoryPanelRect.y + yOffset
            categoryRenderer.render(graphics)
            yOffset += categoryRenderer.rect.h + 10f
        }

        graphics.rect(rect.x + mainPanelRect.x, rect.y + mainPanelRect.y, mainPanelRect.w, mainPanelRect.h,
            0xFF1F1F1F.toInt(), 0xFF1F1F1F.toInt(), 0xFF1F1F1F.toInt(), 0xFF1F1F1F.toInt(),
            0f, 0f, 15f, 0f
        )

        if (selectedCategory != null) {
            graphics.enableScissor(
                (rect.x + mainPanelRect.x).toInt(),
                (rect.y + mainPanelRect.y).toInt(),
                (rect.x + mainPanelRect.x + mainPanelRect.w).toInt(),
                (rect.y + mainPanelRect.y + mainPanelRect.h).toInt(),
            )

            yOffset = 10f
            for (moduleRenderer in selectedCategory!!.modulesRenderers) {
                moduleRenderer.rect.x = rect.x + mainPanelRect.x + 10f
                moduleRenderer.rect.y = rect.y + mainPanelRect.y + yOffset
                moduleRenderer.render(graphics)
                yOffset += moduleRenderer.rect.h + 10f
            }

            graphics.disableScissor()
        }
    }
}