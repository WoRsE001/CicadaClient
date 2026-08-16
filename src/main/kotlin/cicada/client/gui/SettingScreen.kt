package cicada.client.gui

import cicada.client.config.ConfigManager
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.font.Fonts
import cicada.client.render.engine.height
import cicada.client.render.engine.width
import cicada.client.render.gui.Screen
import cicada.client.render.gui.element.Element
import cicada.client.render.gui.element.elements.ElementCut
import cicada.client.render.gui.element.elements.*
import cicada.client.setting.value.BooleanValue
import cicada.client.setting.value.ChoiceValue
import cicada.client.setting.value.Configurable
import cicada.client.setting.value.FloatValue
import cicada.client.setting.value.IntValue
import cicada.client.setting.value.MultiChoiceValue
import cicada.client.setting.value.ToggleableConfigurable
import cicada.client.setting.value.Value
import cicada.client.utils.math.roundTo
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component
import org.apache.commons.lang3.StringUtils
import kotlin.math.roundToInt

// SCWGxD regrets everything he did. 21.06.2026 20:40.
object SettingScreen : net.minecraft.client.gui.screens.Screen(Component.literal("")) {
    private val screen = Screen()
    private val windowWidth = 400f
    private val windowHeight = 225f
    private val font = Fonts["jetbrains-mono"]!!

    private var selectedClientCategory: String? = null
    private var selectedModuleCategory: ModuleCategory? = null
    private var selectedModule: ClientModule? = null

    init {
        buildScreen()
    }

    override fun onClose() {
        ConfigManager.saveDefault()
        super.onClose()
    }

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        screen.measure()
        screen.handle()
        screen.draw(graphics)
    }

    private fun buildScreen() {
        screen.add(window())
    }

    private fun window() = ElementRect(
        { width / 2f - windowWidth / 2 }, { height / 2f - windowHeight / 2 },
        { windowWidth }, { windowHeight },
        COLOR_BACKGROUND,
        r0 = 10f
    ) {
        listOf(windowContent())
    }

    private fun windowContent() = ElementRow({ 5f }, { 5f }, 5f) {
        val content = mutableListOf(sidebar())

        if (selectedClientCategory == "Modules") {
            content += modulePage()
        }

        content
    }

    private fun sidebar() = ElementColumn(gap = 5f) {
        listOf(
            logo(),
            sidebarTab("Modules"),
            sidebarTab("Kaka")
        )
    }

    private fun logo() = ElementRect(
        wProvider = { 75f },
        hProvider = { 25f },
        c0 = -1,
        r0 = 5f
    )

    private fun sidebarTab(name: String): ElementButton {
        val tabColor = { IntArray(4) { if (selectedClientCategory == name) COLOR_ACTIVE else 0 } }

        return ElementButton(
            wProvider = { 75f },
            hProvider = { 10f },
            colorNormal = tabColor,
            colorHover = tabColor,
            colorPressed = tabColor,
            radiiProvider = { FloatArray(4) { 5f } },
            onClick = {
                selectedClientCategory = name
            }
        ) {
            listOf(
                ElementText(
                    xProvider = { 5f },
                    font = font,
                    fontSize = 6f,
                    textProvider = { name },
                    colorProvider = { COLOR_TEXT }
                )
            )
        }
    }

    private fun modulePage(): ElementColumn {
        fun searchBar() = ElementRect(
            wProvider = { 310f },
            hProvider = { 25f },
            c0 = -1,
            r0 = 5f
        )

        fun categoryTabs(): ElementRow {
            fun categoryTab(moduleCategory: ModuleCategory): ElementButton {
                val buttonColor = {
                    IntArray(4) { if (selectedModuleCategory == moduleCategory) COLOR_ACTIVE else COLOR_ELEMENT }
                }

                val name = StringUtils.capitalize(moduleCategory.name.lowercase())

                return ElementButton(
                    wProvider = { font.width(name, 6f) + 10f },
                    hProvider = { 10f },
                    colorNormal = buttonColor,
                    colorHover = buttonColor,
                    colorPressed = buttonColor,
                    radiiProvider = { FloatArray(4) { 5f } },
                    fontProvider = { font },
                    fontSizeProvider = { 6f },
                    textProvider = { name },
                    textColorProvider = { COLOR_TEXT },
                    onClick = {
                        selectedModuleCategory = moduleCategory
                    }
                )
            }

            return ElementRow(gap = 5f) {
                ModuleCategory.entries.map { categoryTab(it) }
            }
        }

        fun modulesList(category: ModuleCategory): ElementCut {
            fun moduleButton(clientModule: ClientModule): ElementButton {
                val buttonColor = {
                    IntArray(4) { if (clientModule.toggled) COLOR_ACTIVE else COLOR_ELEMENT }
                }

                return ElementButton(
                    wProvider = { 310f },
                    hProvider = { 25f },
                    colorNormal = buttonColor,
                    colorHover = buttonColor,
                    colorPressed = buttonColor,
                    radiiProvider = { FloatArray(4) { 5f } },
                    onClick = { button ->
                        if (button == 0)
                            clientModule.toggle()
                        else if (button == 1)
                            selectedModule = clientModule
                    }
                ) {
                    listOf(
                        ElementText(
                            xProvider = { 5f },
                            yProvider = { 5f },
                            font = font,
                            fontSize = 8f,
                            textProvider = { clientModule.name },
                            colorProvider = { COLOR_TEXT }
                        )
                    )
                }
            }

            return ElementCut(
                wProvider = { 310f },
                hProvider = { 170f }
            ) {
                listOf(
                    ElementColumn(gap = 5f) {
                        category.modules.map { moduleButton(it) }
                    }
                )
            }
        }

        fun moduleSettings(selectedModule: ClientModule): ElementRect {
            fun values(xProvider: () -> Float, values: List<Value<*>>): ElementColumn {
                fun value(value: Value<*>): Element {
                    when (value) {
                        is BooleanValue -> {
                            return ElementRow(gap = 3f) {
                                listOf(
                                    ElementText(
                                        font = font,
                                        fontSize = 6f,
                                        textProvider = { "${value.name}:" }
                                    ),
                                    ElementButton(
                                        wProvider = { font.height(6f) },
                                        hProvider = { font.height(6f) },
                                        colorNormal = {
                                            IntArray(4) {
                                                if (value.inner) COLOR_ACTIVE
                                                else COLOR_BACKGROUND
                                            }
                                        },
                                        radiiProvider = { FloatArray(4) { 5f } },
                                        onClick = { value.toggle() }
                                    )
                                )
                            }
                        }

                        is MultiChoiceValue -> {
                            val suffix = if (value.isOpen) "-" else "+"

                            return ElementColumn(gap = 3f) {
                                val content = mutableListOf<Element>(
                                    ElementRow(gap = 3f) {
                                        listOf(
                                            ElementText(
                                                font = font,
                                                fontSize = 6f,
                                                textProvider = { "${value.name}:" }
                                            ),
                                            ElementRect(
                                                wProvider = { 20f + value.inner.maxOf { font.width(it.name, 6f) } },
                                                hProvider = { font.height(6f) },
                                                c0 = COLOR_BACKGROUND
                                            ) {
                                                listOf(
                                                    ElementButton(
                                                        wProvider = { 10f },
                                                        hProvider = { font.height(6f) },
                                                        colorNormal = { IntArray(4) { COLOR_ELEMENT } },
                                                        fontProvider = { font },
                                                        fontSizeProvider = { 6f },
                                                        textProvider = { "<" },
                                                        textColorProvider = { COLOR_TEXT },
                                                        onClick = { value.selected = ((--value.selected % value.inner.size) + value.inner.size) % value.inner.size }
                                                    ),
                                                    ElementButton(
                                                        xProvider = { 10f },
                                                        wProvider = { value.inner.maxOf { font.width(it.name, 6f) } },
                                                        hProvider = { font.height(6f) },
                                                        colorNormal = { IntArray(4) { COLOR_BACKGROUND } },
                                                        fontProvider = { font },
                                                        fontSizeProvider = { 6f },
                                                        textProvider = { value.inner[value.selected].name },
                                                        textColorProvider = { if (value.inner[value.selected].toggled) COLOR_ACTIVE else COLOR_ELEMENT },
                                                        onClick = { value.inner[value.selected].toggle() }
                                                    ),
                                                    ElementButton(
                                                        xProvider = {
                                                            10f + value.inner.maxOf { font.width(it.name, 6f) }
                                                        },
                                                        wProvider = { 10f },
                                                        hProvider = { font.height(6f) },
                                                        colorNormal = { IntArray(4) { COLOR_ELEMENT } },
                                                        fontProvider = { font },
                                                        fontSizeProvider = { 6f },
                                                        textProvider = { ">" },
                                                        textColorProvider = { COLOR_TEXT },
                                                        onClick = { value.selected = ++value.selected % value.inner.size }
                                                    ),
                                                )
                                            },
                                            ElementButton(
                                                xProvider = { 0f },
                                                wProvider = { font.width(suffix, 6f) },
                                                hProvider = { font.height(6f) },
                                                colorNormal = { IntArray(4) { COLOR_ELEMENT } },
                                                fontProvider = { font },
                                                fontSizeProvider = { 6f },
                                                textProvider = { suffix },
                                                textColorProvider = { COLOR_TEXT },
                                                onClick = { value.isOpen = !value.isOpen }
                                            )
                                        )
                                    }
                                )

                                if (value.isOpen) {
                                    content += values({ 3f }, value.inner[value.selected].inner.toList())
                                }

                                content
                            }
                        }

                        is ChoiceValue -> {
                            val suffix = if (value.isOpen) "-" else "+"

                            return ElementColumn(gap = 3f) {
                                val content = mutableListOf<Element>(
                                    ElementRow(gap = 3f) {
                                        listOf(
                                            ElementText(
                                                font = font,
                                                fontSize = 6f,
                                                textProvider = { "${value.name}:" }
                                            ),
                                            ElementRect(
                                                wProvider = { 20f + value.choices.maxOf { font.width(it.name, 6f) } },
                                                hProvider = { font.height(6f) },
                                                c0 = COLOR_BACKGROUND
                                            ) {
                                                listOf(
                                                    ElementButton(
                                                        wProvider = { 10f },
                                                        hProvider = { font.height(6f) },
                                                        colorNormal = { IntArray(4) { COLOR_ELEMENT } },
                                                        fontProvider = { font },
                                                        fontSizeProvider = { 6f },
                                                        textProvider = { "<" },
                                                        textColorProvider = { COLOR_TEXT },
                                                        onClick = { value.previous() }
                                                    ),
                                                    ElementText(
                                                        xProvider = {
                                                            10f + value.choices.maxOf {
                                                                font.width(it.name, 6f)
                                                            } / 2 - font.width(value.inner?.name ?: "None", 6f) / 2
                                                        },
                                                        font = font,
                                                        fontSize = 6f,
                                                        textProvider = { value.inner?.name ?: "None" },
                                                        colorProvider = { COLOR_TEXT }
                                                    ),
                                                    ElementButton(
                                                        xProvider = {
                                                            10f + value.choices.maxOf {
                                                                font.width(
                                                                    it.name,
                                                                    6f
                                                                )
                                                            }
                                                        },
                                                        wProvider = { 10f },
                                                        hProvider = { font.height(6f) },
                                                        colorNormal = { IntArray(4) { COLOR_ELEMENT } },
                                                        fontProvider = { font },
                                                        fontSizeProvider = { 6f },
                                                        textProvider = { ">" },
                                                        textColorProvider = { COLOR_TEXT },
                                                        onClick = { value.next() }
                                                    ),
                                                )
                                            },
                                            ElementButton(
                                                xProvider = { 0f },
                                                wProvider = { font.width(suffix, 6f) },
                                                hProvider = { font.height(6f) },
                                                colorNormal = { IntArray(4) { COLOR_ELEMENT } },
                                                fontProvider = { font },
                                                fontSizeProvider = { 6f },
                                                textProvider = { suffix },
                                                textColorProvider = { COLOR_TEXT },
                                                onClick = { value.isOpen = !value.isOpen }
                                            )
                                        )
                                    }
                                )

                                val choice = value.inner
                                if (choice != null && value.isOpen) {
                                    content += values({ 3f }, choice.inner.toList())
                                }

                                content
                            }
                        }

                        is IntValue -> {
                            return ElementRow(gap = 3f) {
                                listOf(
                                    ElementText(
                                        font = font,
                                        fontSize = 6f,
                                        textProvider = { "${value.name}:" }
                                    ),
                                    ElementSlider(
                                        wProvider = { 100f },
                                        hProvider = { font.height(6f) },
                                        minProvider = { value.range.first.toFloat() },
                                        maxProvider = { value.range.last.toFloat() },
                                        valueProvider = { value.inner.toFloat() },
                                        colorBg = COLOR_BACKGROUND,
                                        colorFill = COLOR_ACTIVE,
                                        onChange = { changedValue -> value.inner = changedValue.roundToInt() }
                                    ),
                                    ElementText(
                                        font = font,
                                        fontSize = 6f,
                                        textProvider = { value.inner.toString() }
                                    )
                                )
                            }
                        }

                        is FloatValue -> {
                            return ElementRow(gap = 3f) {
                                listOf(
                                    ElementText(
                                        font = font,
                                        fontSize = 6f,
                                        textProvider = { "${value.name}:" }
                                    ),
                                    ElementSlider(
                                        wProvider = { 100f },
                                        hProvider = { font.height(6f) },
                                        minProvider = { value.range.start },
                                        maxProvider = { value.range.endInclusive },
                                        valueProvider = { value.inner },
                                        colorBg = COLOR_BACKGROUND,
                                        colorFill = COLOR_ACTIVE,
                                        onChange = { changedValue -> value.inner = changedValue.roundTo(0.01f) }
                                    ),
                                    ElementText(
                                        font = font,
                                        fontSize = 6f,
                                        textProvider = { "%.01f".format(value.inner) }
                                    )
                                )
                            }
                        }

                        is ToggleableConfigurable -> {
                            val suffix = if (value.isOpen) "-" else "+"

                            return ElementColumn(gap = 3f) {
                                val content = mutableListOf<Element>(
                                    ElementRow(gap = 3f) {
                                        listOf(
                                            ElementButton(
                                                wProvider = { font.height(6f) },
                                                hProvider = { font.height(6f) },
                                                colorNormal = {
                                                    IntArray(4) {
                                                        if (value.toggled) COLOR_ACTIVE
                                                        else COLOR_BACKGROUND
                                                    }
                                                },
                                                radiiProvider = { FloatArray(4) { 5f } },
                                                onClick = { value.toggle() }
                                            ),
                                            ElementText(
                                                font = font,
                                                fontSize = 6f,
                                                textProvider = { "${value.name}:" }
                                            ),
                                            ElementButton(
                                                xProvider = { 0f },
                                                wProvider = { font.width(suffix, 6f) },
                                                hProvider = { font.height(6f) },
                                                colorNormal = { IntArray(4) { COLOR_ELEMENT } },
                                                fontProvider = { font },
                                                fontSizeProvider = { 6f },
                                                textProvider = { suffix },
                                                textColorProvider = { COLOR_TEXT },
                                                onClick = { value.isOpen = !value.isOpen }
                                            )
                                        )
                                    }
                                )

                                if (value.isOpen) {
                                    content += values({ 3f }, value.inner.toList())
                                }

                                content
                            }
                        }

                        is Configurable -> {
                            val suffix = if (value.isOpen) "-" else "+"

                            return ElementColumn(gap = 3f) {
                                val content = mutableListOf<Element>(
                                    ElementRow(gap = 3f) {
                                        listOf(
                                            ElementText(
                                                font = font,
                                                fontSize = 6f,
                                                textProvider = { "${value.name}:" }
                                            ),
                                            ElementButton(
                                                xProvider = { 0f },
                                                wProvider = { font.width(suffix, 6f) },
                                                hProvider = { font.height(6f) },
                                                colorNormal = { IntArray(4) { COLOR_ELEMENT } },
                                                fontProvider = { font },
                                                fontSizeProvider = { 6f },
                                                textProvider = { suffix },
                                                textColorProvider = { COLOR_TEXT },
                                                onClick = { value.isOpen = !value.isOpen }
                                            )
                                        )
                                    }
                                )

                                if (value.isOpen) {
                                    content += values({ 3f }, value.inner.toList())
                                }

                                content
                            }
                        }

                        else -> {
                            return ElementText(
                                font = font,
                                fontSize = 6f,
                                textProvider = { "${value.name}: Unknown" }
                            )
                        }
                    }
                }

                return ElementColumn(xProvider = xProvider, gap = 5f) { values.filter { it.visible() }.map { value(it) } }
            }

            return ElementRect(
                wProvider = { 310f },
                hProvider = { 170f },
                c0 = COLOR_ELEMENT,
                r0 = 5f
            ) {
                listOf(
                    ElementText(
                        xProvider = { 5f },
                        yProvider = { 5f },
                        font = font,
                        fontSize = 8f,
                        textProvider = { selectedModule.name },
                    ),
                    ElementButton(
                        xProvider = { 295f },
                        yProvider = { 5f },
                        wProvider = { 10f },
                        hProvider = { 10f },
                        radiiProvider = { FloatArray(4) { 5f } },
                        onClick = {
                            this.selectedModule = null
                        }
                    ),
                    ElementCut(
                        { 5f },
                        { font.height(8f) + 10f },
                        { 305f },
                        { 170f - font.height(8f) - 10f }
                    ) { listOf(values({ 0f }, selectedModule.inner.toList())) }
                )
            }
        }


        return ElementColumn(gap = 5f) {
            val content = mutableListOf(searchBar(), categoryTabs())

            selectedModuleCategory?.let {
                val selected = selectedModule

                content += if (selected == null)
                    modulesList(selectedModuleCategory!!)
                else
                    moduleSettings(selected)
            }

            content
        }
    }

    private const val COLOR_BACKGROUND = 0xFF000000.toInt()
    private const val COLOR_ACTIVE = 0xFF7E6CB8.toInt()
    private const val COLOR_TEXT = -1
    private const val COLOR_ELEMENT = 0xFFACACAC.toInt()
}