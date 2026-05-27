package cicada.client.gui.setting.modules

import cicada.client.feature.module.ModuleCategory
import cicada.client.font.Fonts
import cicada.client.render.height
import cicada.client.render.rect
import cicada.client.render.text
import cicada.client.utils.math.Rect
import cicada.client.utils.client.mc
import cicada.client.utils.render.RenderableObject
import net.minecraft.client.gui.GuiGraphicsExtractor

object SettingModulesScreen : RenderableObject {
    val font = Fonts["roboto-bold"]!!
    override val rect = Rect(0f, 0f, 800f, 450f)
    val categoriesRenderers = ModuleCategory.entries.map { CategoryRenderer(it) }
    val categoryPanelRect = Rect(5f, 5f, 40f, 440f)
    val mainPanelRect = Rect(categoryPanelRect.x + categoryPanelRect.w + 5f, 50f, 790f - categoryPanelRect.x - categoryPanelRect.w, 395f)
    val infoPanelRect = Rect(categoryPanelRect.x + categoryPanelRect.w + 5f, 5f, (790f - categoryPanelRect.x - categoryPanelRect.w) / 2f, 40f)
    var selectedCategory: CategoryRenderer? = null
    var selectedModule: ModuleRenderer? = null

    init {
        rect.x = mc.window.guiScaledWidth / 2 - rect.w / 2
        rect.y = mc.window.guiScaledHeight / 2 - rect.h / 2
    }

    override fun render(graphics: GuiGraphicsExtractor) {
        mainPanelRect.h = 395f
        var xOffset = 0f
        var yOffset = 0f

        graphics.rect(rect.x, rect.y, rect.w, rect.h, 0x501C1C1C, 20f)

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

        graphics.rect(
            rect.x + infoPanelRect.x, rect.y + infoPanelRect.y, infoPanelRect.w, infoPanelRect.h,
            0xFF1F1F1F.toInt()
        )

        var infoText = ""

        if (selectedModule != null) {
            infoText += selectedModule!!.module.name

            if (selectedModule!!.module.description != "")
                infoText += " > " + selectedModule!!.module.description

            graphics.enableScissor(
                (rect.x + mainPanelRect.x).toInt(),
                (rect.y + mainPanelRect.y).toInt(),
                (rect.x + mainPanelRect.x + mainPanelRect.w).toInt(),
                (rect.y + mainPanelRect.y + mainPanelRect.h).toInt(),
            )

            graphics.disableScissor()
        }

        graphics.text(font, infoText, rect.x + infoPanelRect.x + 5f, rect.y + infoPanelRect.y + infoPanelRect.h / 2 - font.height(12f) / 2, 12f)
    }
}