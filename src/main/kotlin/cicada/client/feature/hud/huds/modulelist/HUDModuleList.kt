package cicada.client.feature.hud.huds.modulelist

import cicada.client.feature.hud.HUD
import cicada.client.feature.hud.huds.modulelist.fakemodules.ModuleTest
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.Modules
import cicada.client.font.Fonts
import cicada.client.render.engine.height
import cicada.client.render.engine.text
import cicada.client.render.engine.width
import cicada.client.render.rect
import cicada.client.utils.math.Color4f
import net.minecraft.client.gui.GuiGraphicsExtractor
import kotlin.math.max

// SCWGxD regrets everything he did. 25.04.2026 6:54.
object HUDModuleList : HUD(
    name = "ModuleList",
    defaultToggled = true
) {
    private val moduleSort = ModuleSort.WIDTH_DESCENDING
    private val fontSize by float("TextSize", 12f, 1f..20f)
    private val xGaps by float("XGaps", 0f, 0f..10f)
    private val yGaps by float("YGaps", 0f, 0f..10f)
    private val backgroundColor = color("BackgroundColor", Color4f(0f))
    private val textColor = color("TextColor", Color4f(1f))

    private val font = Fonts["inter-bold"]!!
    private val modulesForRender = mutableListOf<ClientModule>()

    private fun updateModulesForRender() {
        modulesForRender.clear()
        modulesForRender.addAll(moduleSort.sort(Modules.filter { it.toggled }))
    }

    private fun drawModules(graphics: GuiGraphicsExtractor) {
        var maxW = 0f
        var offsetY = y
        for (module in modulesForRender) {
            val textWidth = font.width(module.name, fontSize)
            val textHeight = font.height(fontSize)
            val lineWidth = textWidth + xGaps
            val lineHeight = textHeight + yGaps
            val textX = lineWidth / 2 - textWidth / 2
            val textY = lineHeight / 2 - textHeight / 2

            graphics.rect(x, offsetY, lineWidth, lineHeight, backgroundColor.inner.toInt())
            graphics.text(font, module.name, x + textX, offsetY + textY, fontSize, textColor.inner.toInt())

            maxW = max(lineWidth, maxW)
            offsetY += lineHeight
        }

        w = maxW
        h = offsetY
    }

    override fun render(graphics: GuiGraphicsExtractor) {
        updateModulesForRender()
        drawModules(graphics)
    }

    override fun renderInHUDEditor(graphics: GuiGraphicsExtractor) {
        updateModulesForRender()
        if (modulesForRender.isEmpty()) {
            modulesForRender.add(ModuleTest)
        }
        drawModules(graphics)
    }

    private enum class ModuleSort(
        val sort: (List<ClientModule>) -> List<ClientModule>
    ) {
        ALPHABET_ASCENDING({ modules ->
            modules.sortedBy { it.name }
        }),

        ALPHABET_DESCENDING({ modules ->
            modules.sortedByDescending { it.name }
        }),

        WIDTH_ASCENDING({ modules ->
            modules.sortedBy { font.width(it.name, fontSize) }
        }),

        WIDTH_DESCENDING({ modules ->
            modules.sortedByDescending { font.width(it.name, fontSize) }
        })
    }
}