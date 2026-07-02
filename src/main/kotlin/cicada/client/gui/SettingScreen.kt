package cicada.client.gui

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.font.Fonts
import cicada.client.render.engine.height
import cicada.client.render.engine.width
import cicada.client.render.gui.Screen
import cicada.client.render.gui.element.Element
import cicada.client.render.gui.element.impl.ElementButton
import cicada.client.render.gui.element.impl.ElementChoice
import cicada.client.render.gui.element.impl.ElementColorPicker
import cicada.client.render.gui.element.impl.ElementColumn
import cicada.client.render.gui.element.impl.ElementMultiChoice
import cicada.client.render.gui.element.impl.ElementRect
import cicada.client.render.gui.element.impl.ElementRow
import cicada.client.render.gui.element.impl.ElementSlider
import cicada.client.render.gui.element.impl.ElementText
import cicada.client.setting.BooleanValue
import cicada.client.setting.ChoiceValue
import cicada.client.setting.ColorValue
import cicada.client.setting.Configurable
import cicada.client.setting.FloatRangeValue
import cicada.client.setting.FloatValue
import cicada.client.setting.IntRangeValue
import cicada.client.setting.IntValue
import cicada.client.setting.MultiChoiceValue
import cicada.client.setting.StringValue
import cicada.client.setting.ToggleableConfigurable
import cicada.client.setting.Value
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component

// SCWGxD regrets everything he did. 21.06.2026 20:40.
object SettingScreen : net.minecraft.client.gui.screens.Screen(Component.literal("")) {
    private val screen = Screen()
    private val font = Fonts["jetbrains-mono"]!!

    private var selectedCategory: ModuleCategory? = null
    private var selectedModule: ClientModule? = null

    // Дерево строится один раз; состав детей пересчитывается каждый кадр через .children {},
    // поэтому смена выбора/раскрытие просто меняют модель — пересобирать экран вручную не нужно.
    init {
        buildScreen()
    }

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        screen.measure()
        screen.handle()
        screen.draw(graphics)
    }

    private fun buildScreen() {
        screen.add(
            ElementRect(
                { width / 2 - 200f },
                { height / 2 - 100f },
                { 400f }, { 200f },
                COLOR_PANEL
            ).add(
                ElementRow({ 8f }, { 8f }).children {
                    buildList {
                        // Колонка категорий — всегда
                        add(categoryColumn())
                        // Колонка модулей выбранной категории
                        selectedCategory?.let { add(moduleColumn(it)) }
                        // Колонка настроек выбранного модуля
                        selectedModule?.let { add(settingsColumn(it)) }
                    }
                }
            )
        )
    }

    private fun categoryColumn() = ElementColumn().key("categories").children {
        ModuleCategory.entries.map { categoryButton(it) }
    }

    private fun moduleColumn(category: ModuleCategory) = ElementColumn().key(category).children {
        category.modules.map { moduleButton(it) }
    }

    private fun settingsColumn(module: ClientModule) = ElementColumn().key(module).children {
        buildList {
            add(ElementText(font = font, fontSize = 10f, textProvider = { module.name }).key("title"))
            for (value in module.inner) {
                if (value.visible()) add(settingButton(value))
            }
        }
    }

    private fun categoryButton(category: ModuleCategory) = ElementButton(
        wProvider = { 100f },
        hProvider = { 20f },
        font = font,
        fontSize = 6f,
        textProvider = { category.name },
        colorNormal = { if (selectedCategory == category) COLOR_ACTIVE else COLOR_IDLE },
        colorHover = { COLOR_HOVER },
        colorPressed = { COLOR_PRESSED },
        textColor = COLOR_TEXT,
        radius = 4f,
        onClick = {
            selectedCategory = category
            selectedModule = null
        }
    ).key(category)

    private fun moduleButton(module: ClientModule) = ElementButton(
        wProvider = { 100f },
        hProvider = { 18f },
        font = font,
        fontSize = 6f,
        textProvider = { module.name },
        colorNormal = { if (module.toggled) COLOR_ACTIVE else COLOR_IDLE },
        colorHover = { COLOR_HOVER },
        colorPressed = { COLOR_PRESSED },
        textColor = COLOR_TEXT,
        radius = 4f,
        // ЛКМ — включить/выключить, ПКМ — выбрать модуль и показать настройки
        onClick = {
            module.toggle()
        },
        onRightClick = {
            selectedModule = if (selectedModule == module) null else module
        }
    ).key(module)

    private fun settingButton(value: Value<*>): Element {
        return when (value) {
            is BooleanValue -> ElementRow(gap = 2f).key(value).apply {
                add(
                    ElementButton(
                        wProvider = { 10f },
                        hProvider = { 10f },
                        colorNormal = { if (value.inner) COLOR_ACTIVE else COLOR_IDLE },
                        colorHover = { COLOR_HOVER },
                        colorPressed = { COLOR_PRESSED },
                        textColor = COLOR_TEXT,
                        radius = 4f,
                        onClick = { value.toggle() }
                    )
                )

                add(label(value.name))
            }

            // ToggleableConfigurable должен идти раньше Configurable — он его наследник.
            is ToggleableConfigurable -> ElementColumn().key(value).children {
                buildList {
                    add(
                        ElementRow(gap = 2f).key("header").apply {
                            add(
                                ElementButton(
                                    wProvider = { 10f },
                                    hProvider = { 10f },
                                    colorNormal = { if (value.toggled) COLOR_ACTIVE else COLOR_IDLE },
                                    colorHover = { COLOR_HOVER },
                                    colorPressed = { COLOR_PRESSED },
                                    radius = 4f,
                                    onClick = { value.toggle() }
                                )
                            )

                            add(label(value.name))

                            add(expandButton({ value.isOpen }) { value.isOpen = !value.isOpen })
                        }
                    )

                    if (value.isOpen) add(childColumn(value.inner).key("children"))
                }
            }

            is Configurable -> ElementColumn().key(value).children {
                buildList {
                    add(
                        ElementRow(gap = 2f).key("header").apply {
                            add(label(value.name))

                            add(expandButton({ value.isOpen }) { value.isOpen = !value.isOpen })
                        }
                    )

                    if (value.isOpen) add(childColumn(value.inner).key("children"))
                }
            }

            is FloatValue -> ElementRow(gap = 2f).key(value).apply {
                add(label(value.name))

                add(
                    ElementSlider(
                        wProvider = { 100f },
                        hProvider = { 10f },
                        min = value.range.start,
                        max = value.range.endInclusive,
                        value = value.inner,
                        onChange = { changeValue -> value.inner = changeValue }
                    )
                )

                add(label { "%.2f%s".format(value.inner, value.suffix) })
            }

            is IntValue -> ElementRow(gap = 2f).key(value).apply {
                add(label(value.name))

                add(
                    ElementSlider(
                        wProvider = { 100f },
                        hProvider = { 10f },
                        min = value.range.first.toFloat(),
                        max = value.range.last.toFloat(),
                        value = value.inner.toFloat(),
                        onChange = { changeValue -> value.inner = changeValue.toInt() }
                    )
                )

                add(label { "${value.inner}${value.suffix}" })
            }

            is FloatRangeValue -> ElementColumn().key(value).apply {
                add(label(value.name))

                add(
                    ElementRow(gap = 2f).apply {
                        add(
                            ElementSlider(
                                wProvider = { 100f },
                                hProvider = { 10f },
                                min = value.range.start,
                                max = value.range.endInclusive,
                                value = value.inner.start,
                                onChange = { changeValue ->
                                    value.inner = changeValue..value.inner.endInclusive
                                }
                            )
                        )

                        add(
                            ElementSlider(
                                wProvider = { 100f },
                                hProvider = { 10f },
                                min = value.range.start,
                                max = value.range.endInclusive,
                                value = value.inner.endInclusive,
                                onChange = { changeValue ->
                                    value.inner = value.inner.start..changeValue
                                }
                            )
                        )
                    }
                )

                add(label { "%.2f - %.2f%s".format(value.inner.start, value.inner.endInclusive, value.suffix) })
            }

            is IntRangeValue -> ElementColumn().key(value).apply {
                add(label(value.name))

                add(
                    ElementRow(gap = 2f).apply {
                        add(
                            ElementSlider(
                                wProvider = { 100f },
                                hProvider = { 10f },
                                min = value.range.first.toFloat(),
                                max = value.range.last.toFloat(),
                                value = value.inner.first.toFloat(),
                                onChange = { changeValue ->
                                    value.inner = changeValue.toInt()..value.inner.last
                                }
                            )
                        )

                        add(
                            ElementSlider(
                                wProvider = { 100f },
                                hProvider = { 10f },
                                min = value.range.first.toFloat(),
                                max = value.range.last.toFloat(),
                                value = value.inner.last.toFloat(),
                                onChange = { changeValue ->
                                    value.inner = value.inner.first..changeValue.toInt()
                                }
                            )
                        )
                    }
                )

                add(label { "${value.inner.first} - ${value.inner.last}${value.suffix}" })
            }

            is ChoiceValue -> ElementColumn().key(value).children {
                buildList {
                    val options = value.choices.map { it.name }
                    val selectedIndex = value.choices.indexOf(value.inner).coerceAtLeast(0)

                    add(
                        ElementRow(gap = 2f).key("header").apply {
                            add(label(value.name))

                            add(
                                ElementChoice(
                                    wProvider = { 50f },
                                    itemHeightProvider = { 10f },
                                    options = options,
                                    selected = selectedIndex,
                                    font = font,
                                    fontSize = 7f,
                                    onSelect = { index -> value.choices.getOrNull(index)?.select() }
                                )
                            )
                        }
                    )

                    // Настройки выбранного варианта (если есть)
                    value.inner?.inner?.takeIf { it.isNotEmpty() }?.let { add(childColumn(it).key("children")) }
                }
            }

            is MultiChoiceValue -> ElementColumn().key(value).apply {
                add(label(value.name))

                val options = value.inner.map { it.name }
                val selected = value.inner
                    .mapIndexedNotNull { index, choice -> if (choice.toggled) index else null }
                    .toMutableSet()

                add(
                    ElementMultiChoice(
                        wProvider = { 100f },
                        options = options,
                        selected = selected,
                        font = font,
                        onToggle = { picked ->
                            value.inner.forEachIndexed { index, choice -> choice.toggled = index in picked }
                        }
                    )
                )
            }

            is ColorValue -> ElementColumn().key(value).children {
                buildList {
                    add(
                        ElementRow(gap = 2f).key("header").apply {
                            add(label(value.name))

                            // Превью текущего цвета — клик раскрывает/сворачивает пикер
                            add(
                                ElementButton(
                                    wProvider = { 14f },
                                    hProvider = { 10f },
                                    colorNormal = { value.inner.toInt() },
                                    colorHover = { value.inner.toInt() },
                                    colorPressed = { value.inner.toInt() },
                                    radius = 3f,
                                    onClick = { value.isOpen = !value.isOpen }
                                )
                            )
                        }
                    )

                    if (value.isOpen) {
                        add(
                            ElementColorPicker(
                                wProvider = { 100f },
                                hProvider = { 60f },
                                color = value.inner.toInt(),
                                onChange = { argb -> value.inner.setFromARGB(argb) }
                            ).key("picker")
                        )
                        // Пикер рисует полоску-превью ниже своей области — резервируем место, чтобы не было наложения.
                        add(spacer(45f).key("spacer"))
                    }
                }
            }

            is StringValue -> ElementRow(gap = 2f).key(value).apply {
                // Поля ввода текста пока нет — показываем значение только для чтения.
                add(label(value.name))
                add(label { ": ${value.inner}" })
            }

            else -> label { "Unknown: ${value.name}" }.key(value)
        }
    }

    private fun label(text: String) = label { text }

    private fun label(textProvider: () -> String) =
        ElementText(font = font, fontSize = 7f, textProvider = textProvider)

    private fun expandButton(isOpen: () -> Boolean, onClick: () -> Unit) = ElementButton(
        wProvider = { 10f },
        hProvider = { 10f },
        font = font,
        fontSize = 7f,
        textProvider = { if (isOpen()) "-" else "+" },
        colorNormal = { 0 },
        colorHover = { 0 },
        colorPressed = { 0 },
        onClick = onClick
    )

    /** Вертикальная колонка дочерних настроек с отступом; состав пересчитывается каждый кадр по видимости. */
    private fun childColumn(values: Collection<Value<*>>) = ElementColumn(xProvider = { 5f }).children {
        values.filter { it.visible() }.map { settingButton(it) }
    }

    /** Невидимый прямоугольник заданной высоты — резервирует место в раскладке. */
    private fun spacer(height: Float) = ElementButton(
        wProvider = { 1f },
        hProvider = { height },
        colorNormal = { 0 },
        colorHover = { 0 },
        colorPressed = { 0 }
    )

    // Тёмно-фиолетовая пастельная тема
    private const val COLOR_PANEL = 0xFF1C1726.toInt()    // фон панели — почти чёрный с фиолетом
    private const val COLOR_IDLE = 0xFF2B2540.toInt()     // кнопка в покое
    private const val COLOR_HOVER = 0xFF39315A.toInt()    // наведение
    private const val COLOR_PRESSED = 0xFF221D33.toInt()  // нажатие
    private const val COLOR_ACTIVE = 0xFF7E6CB8.toInt()   // активный/включённый — лавандовый акцент
    private const val COLOR_SELECTED = 0xFF9B86D9.toInt() // выбранный модуль — яркий лавандовый
    private const val COLOR_TEXT = 0xFFD8CEF0.toInt()     // светлый лавандовый текст
}