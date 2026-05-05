package cicada.client.gui.clientsettings

import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.setting.*
import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.Rect
import cicada.client.utils.math.map
import cicada.client.utils.render.font.INTER_BOLD_FONT
import cicada.client.utils.render.isCollide
import cicada.client.utils.render.rect
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

// SCWGxD regrets everything he did. 30.03.2026 16:14.
object ClientSettingsScreen : Screen(Component.empty()) {
    val font = INTER_BOLD_FONT
    val rect = Rect(0f, 0f, 384f, 216f)
    var categoriesWidth = 0f
    var categoriesHeight = 0f
    var modulesWidth = 0f
    var modulesHeight = 0f
    var selectedCategory: Category? = null
    var selectedModule: Module? = null
    var modulesScroll = 0f
    var settingsScroll = 0f
    var totalSettingHeight = 0f

    override fun init() {
        rect.x = width / 2 - rect.w / 2
        rect.y = height / 2 - rect.h / 2

        categoriesWidth = rect.w / Category.entries.size
        categoriesHeight = rect.h / 10f

        modulesWidth = rect.w / 3f
        modulesHeight = (rect.h - categoriesHeight) / 5.5f
    }

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        var xOffset = 0f
        var yOffset = 0f

        graphics.rect(rect.x, rect.y, rect.w, rect.h, 0xFF1C1C1C.toInt())

        xOffset = 0f
        for (category in Category.entries) {
            if (isSosi() && FrameInput.clicked[0] && graphics.isCollide(FrameInput.MPos, rect.x + xOffset, rect.y, categoriesWidth, categoriesHeight)) {
                selectedCategory = category
            }

            graphics.rect(rect.x + xOffset, rect.y, categoriesWidth, categoriesHeight, 0xFFF1A0C0.toInt(), 0xFFF1A0C0.toInt(), 0xFFAAF0D1.toInt(), 0xFFAAF0D1.toInt())
            font.centeredDraw(graphics, category.name, rect.x + categoriesWidth / 2 + xOffset, rect.y + categoriesHeight / 2, 9f, -1)
            xOffset += categoriesWidth
        }

        graphics.enableScissor(rect.x.toInt(), (rect.y + categoriesHeight).toInt(), (rect.x + modulesWidth).toInt(), (rect.y + rect.h).toInt())

        yOffset = modulesScroll
        if (selectedCategory != null) {
            for (module in selectedCategory!!.modules) {
                if (isSosi() && graphics.isCollide(FrameInput.MPos, rect.x, rect.y + categoriesHeight + yOffset, modulesWidth, modulesHeight)) {
                    if (FrameInput.clicked[0])
                        module.toggle()
                    if (FrameInput.clicked[1])
                        selectedModule = module
                }

                if (module.toggled) {
                    graphics.rect(
                        rect.x, rect.y + categoriesHeight + yOffset, modulesWidth, modulesHeight,
                        0xFFF1A0C0.toInt(), 0xFFF1A0C0.toInt(), 0xFFAAF0D1.toInt(), 0xFFAAF0D1.toInt()
                    )
                }

                font.centeredDraw(graphics, module.name, rect.x + modulesWidth / 2, rect.y + categoriesHeight + modulesHeight / 2 + yOffset, 9f, -1)
                yOffset += modulesHeight
            }
        }

        graphics.disableScissor()

        totalSettingHeight = 0f
        if (selectedModule != null) {
            font.draw(graphics, selectedModule!!.name, rect.x + modulesWidth, rect.y + categoriesHeight, 12f)

            graphics.enableScissor((rect.x + modulesWidth).toInt(), (rect.y + categoriesHeight + font.height(12f)).toInt(), (rect.x + rect.w).toInt(), (rect.y + rect.h).toInt())

            for (setting in selectedModule!!.inner) {
                totalSettingHeight += drawSetting(setting, graphics, rect.x + modulesWidth, rect.y + categoriesHeight + totalSettingHeight + settingsScroll + font.height(12f))
            }

            graphics.disableScissor()
        }
    }

    fun isSosi() = rect.isCollide(FrameInput.MPos)

    fun drawSetting(setting: Value<*>, graphics: GuiGraphicsExtractor, x: Float, y: Float): Float {
        when (setting) {
            is BooleanValue -> {
                if (
                    FrameInput.clicked[0] &&
                    graphics.isCollide(FrameInput.MPos, x + font.width(setting.name, 9f), y, font.height(9f) * 2, font.height(9f))
                    ) {
                    setting.toggle()
                }

                font.draw(graphics, setting.name, x, y, 9f)
                graphics.rect(x + font.width(setting.name, 9f), y, font.height(9f) * 2, font.height(9f), 0xFF000000.toInt())
                graphics.rect(x + font.width(setting.name, 9f) + if (setting.inner) font.height(9f) else 0f, y, font.height(9f), font.height(9f), -1)
                return font.height(9f)
            }

            is ChoiceValue -> {
                font.draw(graphics, setting.name, x, y, 9f)
                val maxChoiceWidth = setting.choices.maxOfOrNull { font.width(it.name, 9f) } ?: 20f
                graphics.rect(x + font.width(setting.name, 9f), y, maxChoiceWidth, font.height(9f), 0xFFF1A0C0.toInt(), 0xFFF1A0C0.toInt(), 0xFFAAF0D1.toInt(), 0xFFAAF0D1.toInt())
                if (setting.inner != null) font.draw(graphics, setting.inner!!.name, x + font.width(setting.name, 9f), y, 9f)

                if (FrameInput.clicked[0] && graphics.isCollide(FrameInput.MPos, x + font.width(setting.name, 9f), y, maxChoiceWidth, font.height(9f))) {
                    setting.isShowChoices = !setting.isShowChoices
                }

                var offsetY = 0f
                if (setting.isShowChoices) {
                    for (choice in setting.choices) {
                        if (choice.selected()) continue
                        if (FrameInput.clicked[0] && graphics.isCollide(FrameInput.MPos, x + font.width(setting.name, 9f), y + font.height(9f) + offsetY, maxChoiceWidth, font.height(9f))) {
                            choice.select()
                            setting.isShowChoices = false
                        }
                        graphics.rect(x + font.width(setting.name, 9f), y + font.height(9f) + offsetY, maxChoiceWidth, font.height(9f), 0xFFF1A0C0.toInt(), 0xFFF1A0C0.toInt(), 0xFFAAF0D1.toInt(), 0xFFAAF0D1.toInt())
                        font.draw(graphics, choice.name, x + font.width(setting.name, 9f), y + offsetY + font.height(9f), 9f)
                        offsetY += font.height(9f)
                    }
                }

                if (setting.inner != null) {
                    var suffix = if (setting.inner!!.isShowSettings) "-" else "+"

                    if (FrameInput.clicked[0] && graphics.isCollide(FrameInput.MPos, x + font.width(setting.name, 9f) + maxChoiceWidth, y, font.width(suffix, 9f), font.height(9f))) {
                        setting.inner!!.isShowSettings = !setting.inner!!.isShowSettings
                    }

                    suffix = if (setting.inner!!.isShowSettings) "-" else "+"

                    font.draw(graphics, suffix, x + font.width(setting.name, 9f) + maxChoiceWidth, y, 9f)

                    if (setting.inner!!.isShowSettings) {
                        for (setting1 in setting.inner!!.inner) {
                            offsetY += drawSetting(setting1, graphics, x + 5, y + offsetY + font.height(9f))
                        }
                    }
                }

                return font.height(9f) + offsetY
            }

            is ToggleableConfigureable -> {
                var suffix = if (setting.isShowSettings) " -" else " +"

                if (FrameInput.clicked[0]) {
                    if (graphics.isCollide(FrameInput.MPos, x, y, font.height(9f) * 2, font.height(9f)))
                        setting.toggle()
                    if (graphics.isCollide(FrameInput.MPos, x + font.height(9f) * 2, y, font.width(setting.name + suffix, 9f), font.height(9f)))
                        setting.isShowSettings = !setting.isShowSettings
                }

                suffix = if (setting.isShowSettings) " -" else " +"

                graphics.rect(x, y, font.height(9f) * 2, font.height(9f), 0xFF000000.toInt())
                graphics.rect(x + if (setting.toggled) font.height(9f) else 0f, y, font.height(9f), font.height(9f), -1)
                font.draw(graphics, setting.name + suffix, x + font.height(9f) * 2, y, 9f)

                var offsetY = 0f
                if (setting.isShowSettings) {
                    for (setting1 in setting.inner) {
                        offsetY += drawSetting(setting1, graphics, x + 5, y + offsetY + font.height(9f))
                    }
                }

                return offsetY + font.height(9f)
            }

            is Configureable -> {
                var suffix = if (setting.isShowSettings) " -" else " +"

                if (
                    FrameInput.clicked[0] &&
                    graphics.isCollide(FrameInput.MPos, x, y, font.width(setting.name + suffix, 9f), font.height(9f))
                ) {
                    setting.isShowSettings = !setting.isShowSettings
                }

                suffix = if (setting.isShowSettings) " -" else " +"

                font.draw(graphics, setting.name + suffix, x, y, 9f)

                var offsetY = 0f
                if (setting.isShowSettings) {
                    for (setting1 in setting.inner) {
                        offsetY += drawSetting(setting1, graphics, x + 5, y + offsetY + font.height(9f))
                    }
                }

                return offsetY + font.height(9f)
            }

            is FloatRangeValue -> {
                if (
                    !setting.isMinDragging &&
                    !setting.isMaxDragging &&
                    FrameInput.clicked[0] &&
                    graphics.isCollide(FrameInput.MPos, x, y + font.height(9f), 150f, 5f)
                ) {
                    val valueFromPos = FrameInput.MPos.x.map(x, x + 150f, setting.range.start, setting.range.endInclusive)
                    val innerCenter = (setting.inner.start + setting.inner.endInclusive) / 2
                    if (valueFromPos <= innerCenter) {
                        setting.isMinDragging = true
                    } else {
                        setting.isMaxDragging = true
                    }
                }

                if ((setting.isMinDragging || setting.isMaxDragging) && FrameInput.released[0]) {
                    setting.isMinDragging = false
                    setting.isMaxDragging = false
                }

                if (setting.isMinDragging) {
                    setting.inner = FrameInput.MPos.x.map(x, x + 150f, setting.range.start, setting.range.endInclusive).coerceIn(setting.range.start, setting.inner.endInclusive)..setting.inner.endInclusive
                }

                if (setting.isMaxDragging) {
                    setting.inner = setting.inner.start..FrameInput.MPos.x.map(x, x + 150f, setting.range.start, setting.range.endInclusive).coerceIn(setting.inner.start, setting.range.endInclusive)
                }

                font.draw(graphics, setting.name, x, y, 9f)
                val innerStr = String.format("%.2f", setting.inner.start) + "-" + String.format("%.2f", setting.inner.endInclusive) + if (setting.suffix != "") " " else "" + setting.suffix
                font.draw(graphics, innerStr, x + 150f - font.width(innerStr, 9f), y, 9f)
                graphics.rect(x, y + font.height(9f), 150f, 5f, 0xFF000000.toInt())
                val minInnerPos = setting.inner.start.map(setting.range.start, setting.range.endInclusive, x, x + 150f)
                val maxInnerPos = setting.inner.endInclusive.map(setting.range.start, setting.range.endInclusive, x, x + 150f)
                graphics.rect(minInnerPos, y + font.height(9f), maxInnerPos - minInnerPos, 5f, -1)
                return font.height(9f) + 5f
            }

            is FloatValue -> {
                if (
                    !setting.isDragging &&
                    FrameInput.clicked[0] &&
                    graphics.isCollide(FrameInput.MPos, x, y + font.height(9f), 150f, 5f)
                    ) {
                    setting.isDragging = true
                }

                if (setting.isDragging && FrameInput.released[0]) {
                    setting.isDragging = false
                }

                if (setting.isDragging) {
                    setting.inner = FrameInput.MPos.x.coerceIn(x, x + 150f).map(x, x + 150f, setting.range.start, setting.range.endInclusive)
                }

                font.draw(graphics, setting.name, x, y, 9f)
                val innerStr = String.format("%.2f", setting.inner) + if (setting.suffix != "") " " else "" + setting.suffix
                font.draw(graphics, innerStr, x + 150f - font.width(innerStr, 9f), y, 9f)
                graphics.rect(x, y + font.height(9f), 150f, 5f, 0xFF000000.toInt())
                val innerWidth = setting.inner.map(setting.range.start, setting.range.endInclusive, 0f, 150f)
                graphics.rect(x, y + font.height(9f), innerWidth, 5f, -1)
                return font.height(9f) + 5f
            }

            is IntRangeValue -> {
                if (
                    !setting.isMinDragging &&
                    !setting.isMaxDragging &&
                    FrameInput.clicked[0] &&
                    graphics.isCollide(FrameInput.MPos, x, y + font.height(9f), 150f, 5f)
                ) {
                    val valueFromPos = FrameInput.MPos.x.map(x, x + 150f, setting.range.start.toFloat(), setting.range.endInclusive.toFloat())
                    val innerCenter = (setting.inner.start + setting.inner.endInclusive) / 2
                    if (valueFromPos <= innerCenter) {
                        setting.isMinDragging = true
                    } else {
                        setting.isMaxDragging = true
                    }
                }

                if ((setting.isMinDragging || setting.isMaxDragging) && FrameInput.released[0]) {
                    setting.isMinDragging = false
                    setting.isMaxDragging = false
                }

                if (setting.isMinDragging) {
                    setting.inner = FrameInput.MPos.x.map(x, x + 150f, setting.range.start.toFloat(), setting.range.endInclusive.toFloat()).coerceIn(setting.range.start.toFloat(), setting.inner.endInclusive.toFloat()).toInt()..setting.inner.endInclusive
                }

                if (setting.isMaxDragging) {
                    setting.inner = setting.inner.start..FrameInput.MPos.x.map(x, x + 150f, setting.range.start.toFloat(), setting.range.endInclusive.toFloat()).coerceIn(setting.inner.start.toFloat(), setting.range.endInclusive.toFloat()).toInt()
                }

                font.draw(graphics, setting.name, x, y, 9f)
                val innerStr = setting.inner.start.toString() + "-" + setting.inner.endInclusive.toString() + if (setting.suffix != "") " " else "" + setting.suffix
                font.draw(graphics, innerStr, x + 150f - font.width(innerStr, 9f), y, 9f)
                graphics.rect(x, y + font.height(9f), 150f, 5f, 0xFF000000.toInt())
                val minInnerPos = setting.inner.start.toFloat().map(setting.range.start.toFloat(), setting.range.endInclusive.toFloat(), x, x + 150f)
                val maxInnerPos = setting.inner.endInclusive.toFloat().map(setting.range.start.toFloat(), setting.range.endInclusive.toFloat(), x, x + 150f)
                graphics.rect(minInnerPos, y + font.height(9f), maxInnerPos - minInnerPos, 5f, -1)
                return font.height(9f) + 5f
            }

            is IntValue -> {
                if (
                    !setting.isDragging &&
                    FrameInput.clicked[0] &&
                    graphics.isCollide(FrameInput.MPos, x, y + font.height(9f), 150f, 5f)
                ) {
                    setting.isDragging = true
                }

                if (setting.isDragging && FrameInput.released[0]) {
                    setting.isDragging = false
                }

                if (setting.isDragging) {
                    setting.inner = FrameInput.MPos.x.coerceIn(x, x + 150f).map(x, x + 150f, setting.range.start.toFloat(), setting.range.endInclusive.toFloat()).toInt()
                }

                font.draw(graphics, setting.name, x, y, 9f)
                val innerStr = setting.inner.toString() + if (setting.suffix != "") " " else "" + setting.suffix
                font.draw(graphics, innerStr, x + 150f - font.width(innerStr, 9f), y, 9f)
                graphics.rect(x, y + font.height(9f), 150f, 5f, 0xFF000000.toInt())
                val innerWidth = setting.inner.toFloat().map(setting.range.start.toFloat(), setting.range.endInclusive.toFloat(), 0f, 150f)
                graphics.rect(x, y + font.height(9f), innerWidth, 5f, -1)

                return font.height(9f) + 5f
            }

            else -> {
                return font.height(9f)
            }
        }
    }

    override fun mouseScrolled(x: Double, y: Double, scrollX: Double, scrollY: Double): Boolean {
        if (selectedCategory != null && Rect(rect.x, rect.y + categoriesHeight, modulesWidth, rect.h - categoriesHeight).isCollide(x.toFloat(), y.toFloat())) {
            val viewHeight = rect.h - categoriesHeight
            val listHeight = selectedCategory!!.modules.size * modulesHeight
            val minScroll = minOf(0f, viewHeight - listHeight)
            val maxScroll = 0f

            modulesScroll += scrollY.toFloat() * 12
            modulesScroll = modulesScroll.coerceIn(minScroll, maxScroll)
        }

        if (selectedModule != null && Rect(rect.x + modulesWidth, rect.y + categoriesHeight, rect.w - modulesWidth, rect.h - categoriesHeight).isCollide(x.toFloat(), y.toFloat())) {
            val viewHeight = rect.h - categoriesHeight - font.height(12f)
            val minScroll = minOf(0f, viewHeight - totalSettingHeight)
            val maxScroll = 0f

            settingsScroll += scrollY.toFloat() * 12
            settingsScroll = settingsScroll.coerceIn(minScroll, maxScroll)
        }

        return super.mouseScrolled(x, y, scrollX, scrollY)
    }
}