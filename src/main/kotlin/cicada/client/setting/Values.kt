package cicada.client.setting

import cicada.client.CicadaClient
import cicada.client.event.Event
import cicada.client.setting.MultiChoiceValue.Choice
import cicada.client.utils.input.FrameInput
import cicada.client.utils.math.Color4f
import cicada.client.utils.math.Rect
import cicada.client.utils.math.map
import cicada.client.utils.mc
import cicada.client.utils.render.rect
import kotlinx.serialization.json.*
import net.minecraft.client.gui.GuiGraphicsExtractor
import kotlin.ranges.rangeTo

class BooleanValue(
    name: String,
    default: Boolean
) : Value<Boolean>(name, default) {
    override var json: JsonObject
        get() = buildJsonObject {
            put("toggled", inner)
        }
        set(value) {
            inner = value["toggled"]?.jsonPrimitive?.booleanOrNull ?: run {
                CicadaClient.warn("While loading \"$this\" parameter \"toggled\" wasn't found.")
                return
            }
        }

    override val rect = Rect(0f, 0f, 150f, 9f)

    fun toggle() {
        inner = !inner
    }

    override fun render(graphics: GuiGraphicsExtractor) {
        if (FrameInput.clicked[0] && isCollide(FrameInput.MPos, rect.x, rect.y, 9f, 9f))
            toggle()

        graphics.rect(rect.x, rect.y, 9f, 9f, if (inner) 0xFF3776FF.toInt() else 0xFF7F7F7F.toInt())
        //graphics.drawText(name, rect.x + 12f, rect.y + 1, -1)
    }
}

open class ChoiceValue(
    name: String
) : Value<ChoiceValue.Choice?>(name, null) {
    private val _choices = mutableListOf<Choice>()

    val choices: List<Choice>
        get() = _choices

    override var json: JsonObject
        get() = buildJsonObject {
            put("choice", inner?.name)
        }
        set(value) {
            val name = value["choice"]?.jsonPrimitive?.contentOrNull ?: run {
                CicadaClient.warn("While loading \"$this\" parameter \"string\" wasn't found.")
                return
            }

            inner = _choices.firstOrNull { it.name == name } ?: run {
                CicadaClient.warn("Choice \"$name\" wasn't found in choices \"${this.name}\".")
                return
            }
        }

    override val rect = Rect(0f, 0f, 150f, 9f)

    var isShowChoices = false

    fun choice(name: String) = Choice(name).apply {
        parent = this@ChoiceValue
        _choices += this
    }

    fun choice(choice: Choice) = choice.apply {
        parent = this@ChoiceValue
        _choices += this
    }

    override fun render(graphics: GuiGraphicsExtractor) {
        rect.h = 9f

        val isShowSettingsSuffix = if (isShowChoices) "-" else "+"
        val isShowSettingsSuffixWidth = mc.font.width(isShowSettingsSuffix).toFloat()

        //graphics.drawText("$name: ${inner?.name ?: ""}", rect.x, rect.y + 1, -1)
        //graphics.drawText(isShowSettingsSuffix, rect.x + rect.w - mc.font.width(isShowSettingsSuffix), rect.y + 1, -1)

        if (isShowChoices) {
            for (choice in choices) {
                choice.rect.x = rect.x + 5
                choice.rect.y = rect.y + rect.h

                if (
                    FrameInput.clicked[0] &&
                    isCollide(FrameInput.MPos, choice.rect.x, choice.rect.y, mc.font.width(choice.name).toFloat(), 10f)
                ) {
                    inner?.onDisable()
                    choice.select()
                }

                //graphics.drawText(choice.name, choice.rect.x, choice.rect.y, -1)

                rect.h += choice.rect.h
            }

            graphics.rect(rect.x, rect.y + 9, 2f, rect.h - 9, 0xFF3776FF.toInt())
        }

        if (FrameInput.clicked[0]) {
            isShowChoices = if (isCollide(FrameInput.MPos, rect.x + rect.w - isShowSettingsSuffixWidth, rect.y + 1, isShowSettingsSuffixWidth, 9f))
                !isShowChoices
            else
                false
        }
    }

    open class Choice internal constructor(
        name: String
    ) : Configureable(name) {
        lateinit var parent: ChoiceValue

        override val rect = Rect(0f, 0f, 150f, 9f)

        fun select() = apply {
            parent.set(this)
            onEnable()
        }

        fun selected() = parent.get() == this

        open fun onEnable() {}

        open fun onEvent(event: Event) {}

        open fun onDisable() {}

        override fun render(graphics: GuiGraphicsExtractor) {}
    }
}

class ColorValue(
    name: String,
    default: Color4f
) : Value<Color4f>(name, default) {
    override var json: JsonObject
        get() = buildJsonObject {
            put("color", inner.toInt())
        }
        set(value) {
            inner.setFromARGB(value["color"]?.jsonPrimitive?.intOrNull ?: run {
                CicadaClient.warn("While loading \"$this\" parameter \"int\" wasn't found.")
                return
            })
        }

    override val rect = Rect(0f, 0f, 150f, 9f)

    override fun render(graphics: GuiGraphicsExtractor) {}
}

open class Configureable(
    name: String,
    default: MutableCollection<Value<*>> = mutableListOf()
) : Value<MutableCollection<Value<*>>>(name, default) {
    var owner: Configureable? = null

    override var json: JsonObject
        get() = buildJsonObject {
            for (value in inner) {
                put(value.name, value.json)
            }
        }
        set(value) {
            for (item in inner) {
                item.json = value[item.name]?.jsonObject ?: run {
                    CicadaClient.warn("While loading \"$this\" object \"${item.name}\" wasn't found.")
                    continue
                }
            }
        }

    override val rect = Rect(0f, 0f, 150f, 9f)
    open val rect1 = Rect(0f, 0f, 150f, 9f)

    var isShowSettings = false

    override fun render(graphics: GuiGraphicsExtractor) {
        rect1.x = rect.x
        rect1.y = rect.y

        rect.h = 9f
        if (isShowSettings) {
            rect.h += 2
            for (setting in inner) {
                rect.h += setting.rect.h + 2
            }
        }

        val suffix = if (isShowSettings) "-" else "+"
        val suffixWidth = mc.font.width(suffix).toFloat()

        if (FrameInput.clicked[0] && isCollide(
                FrameInput.MPos,
                rect.x + rect.w - suffixWidth - 5,
                rect.y,
                suffixWidth,
                9f
            )
        )
            isShowSettings = !isShowSettings

        //graphics.drawText(name, rect.x, rect.y + 1, -1)
        //graphics.drawText(suffix, rect1.x + rect1.w - suffixWidth - 5, rect1.y, -1)

        if (isShowSettings) {
            var offsetY = 2f
            for (setting in inner) {
                setting.rect.x = rect.x + 5
                setting.rect.y = rect.y + rect1.h + offsetY
                setting.render(graphics)
                offsetY += setting.rect.h + 2
            }

            graphics.rect(rect.x, rect.y + rect1.h, 2f, rect.h - rect1.h, 0xFF3776FF.toInt())
        }
    }

    override fun resetToDefault() {
        for (value in inner) {
            value.resetToDefault()
        }
    }

    fun boolean(
        name: String,
        default: Boolean
    ) = BooleanValue(name, default).apply {
        this@Configureable.inner += this
    }

    fun choice(
        name: String
    ) = ChoiceValue(name).apply {
        this@Configureable.inner += this
    }

    fun color(
        name: String,
        default: Color4f
    ) = ColorValue(name, default).apply {
        this@Configureable.inner += this
    }

    fun float(
        name: String,
        default: Float,
        range: ClosedRange<Float>,
        suffix: String = ""
    ) = FloatValue(name, default, range, suffix).apply {
        this@Configureable.inner += this
    }

    fun floatRange(
        name: String,
        default: ClosedRange<Float>,
        range: ClosedRange<Float>,
        suffix: String = ""
    ) = FloatRangeValue(name, default, range, suffix).apply {
        this@Configureable.inner += this
    }

    fun multiChoice(
        name: String
    ) = MultiChoiceValue(name).apply {
        this@Configureable.inner += this
    }

    fun group(
        name: String
    ) = Configureable(name).apply {
        this@Configureable.inner += this
    }

    fun int(
        name: String,
        default: Int,
        range: IntRange,
        suffix: String = ""
    ) = IntValue(name, default, range, suffix).apply {
        this@Configureable.inner += this
    }

    fun intRange(
        name: String,
        default: IntRange,
        range: IntRange,
        suffix: String = ""
    ) = IntRangeValue(name, default, range, suffix).apply {
        this@Configureable.inner += this
    }

    fun string(
        name: String,
        default: String
    ) = StringValue(name, default).apply {
        this@Configureable.inner += this
    }

    fun toggleableGroup(
        name: String,
        default: Boolean
    ) = ToggleableConfigureable(name, defaultToggled = default).apply {
        this@Configureable.inner += this
    }

    fun <T : Configureable> tree(configurable: T) = configurable.apply {
        this.owner = this
        this@Configureable.inner += this
    }
}

/**
 * allows value to go out of range?
 * don't touch.
 */
class FloatRangeValue(
    name: String,
    default: ClosedRange<Float>,
    val range: ClosedRange<Float>,
    val suffix: String
) : Value<ClosedRange<Float>>(name, default) {
    override var json: JsonObject
        get() = buildJsonObject {
            put("floatRange.start", inner.start)
            put("floatRange.end", inner.endInclusive)
        }
        set(value) {
            val start = value["floatRange.start"]?.jsonPrimitive?.floatOrNull ?: run {
                CicadaClient.warn("While loading \"$this\" parameter \"floatRange.start\" wasn't found.")
                return
            }

            val end = value["floatRange.end"]?.jsonPrimitive?.floatOrNull ?: run {
                CicadaClient.warn("While loading \"$this\" parameter \"floatRange.end\" wasn't found.")
                return
            }

            inner = start..end
        }

    override val rect = Rect(0f, 0f, 150f, 14f)

    var isMinDragging = false
    var isMaxDragging = false

    override fun render(graphics: GuiGraphicsExtractor) {
        val innerCenter = (inner.start + inner.endInclusive) / 2
        val innerCenterPos = innerCenter.map(range.start, range.endInclusive, rect.x + rect.w - 100, rect.x + rect.w)

        if (
            FrameInput.clicked[0] &&
            isCollide(FrameInput.MPos, rect.x + rect.w - 100, rect.y, 100f, rect.h)
        ) {
            if (FrameInput.MPos.x < innerCenterPos) isMinDragging = true
            else isMaxDragging = true
        } else if (FrameInput.released[0]) {
            isMinDragging = false
            isMaxDragging = false
        }

        if (isMinDragging) {
            inner = FrameInput.MPos.x.map(rect.x + rect.w - 100, rect.x + rect.w, range.start, range.endInclusive).coerceIn(range.start, inner.endInclusive)..inner.endInclusive
        }

        if (isMaxDragging) {
            inner = inner.start..FrameInput.MPos.x.map(rect.x + rect.w - 100, rect.x + rect.w, range.start, range.endInclusive).coerceIn(inner.start, range.endInclusive)
        }

        //graphics.drawText(name, rect.x, rect.y + 1, -1)
        val innerStr = "${String.format("%.2f", inner.start)} - ${String.format("%.2f", inner.endInclusive)}"
        //graphics.drawText(innerStr, rect.x + rect.w - 100 - mc.font.width(innerStr), rect.y + 1, -1)
        graphics.rect(rect.x + rect.w - 100, rect.y, 100f, rect.h, 0xFF000000.toInt())

        val xPosByInnerStart = rect.x + rect.w - inner.start.map(range.start, range.endInclusive, 100f, 0f)
        val widthByInnerEnd = rect.x + rect.w - inner.endInclusive.map(range.start, range.endInclusive, 100f, 0f) - xPosByInnerStart
        graphics.rect(xPosByInnerStart, rect.y, widthByInnerEnd, rect.h, -1)
    }
}

/**
 * allows value to go out of range?
 * don't touch.
 */
class FloatValue(
    name: String,
    default: Float,
    val range: ClosedRange<Float>,
    val suffix: String = ""
) : Value<Float>(name, default) {
    override var json: JsonObject
        get() = buildJsonObject {
            put("float", inner)
        }
        set(value) {
            inner = value["float"]?.jsonPrimitive?.floatOrNull ?: run {
                CicadaClient.warn("While loading \"$this\" parameter \"float\" wasn't found.")
                return
            }
        }

    override val rect = Rect(0f, 0f, 150f, 14f)

    var isDragging = false

    override fun render(graphics: GuiGraphicsExtractor) {
        if (!isDragging && FrameInput.clicked[0] && isCollide(FrameInput.MPos, rect.x, rect.y + 9, rect.w, 5f)) {
            isDragging = true
        }

        if (isDragging && FrameInput.released[0]) {
            isDragging = false
        }

        if (isDragging) {
            inner = FrameInput.MPos.x.map(rect.x + 5, rect.x + rect.w, range.start, range.endInclusive).coerceIn(range)
        }

        //graphics.drawText(name, rect.x, rect.y, -1)
        val innerStr = String.format("%.2f", inner)
        //graphics.drawText(innerStr, rect.x + rect.w - mc.font.width(innerStr), rect.y, -1)
        graphics.rect(rect.x, rect.y + 9, rect.w, 5f, 0xFF7F7F7F.toInt(), 3f)
        val innerWidth = inner.map(range.start, range.endInclusive, 5f, rect.w)
        graphics.rect(rect.x, rect.y + 9, innerWidth, 5f, 0xFF3776FF.toInt(), 3f)
    }
}

open class MultiChoiceValue(
    name: String
) : Value<MutableList<Choice>>(name, mutableListOf()) {
    override var json: JsonObject
        get() = buildJsonObject {
            /*put("choice", inner?.name)*/
        }
        set(value) {
            /*val name = value["choice"]?.jsonPrimitive?.contentOrNull ?: run {
                HazeClient.warn("While loading \"$this\" parameter \"string\" wasn't found.")
                return
            }

            inner = _choices.firstOrNull { it.name == name } ?: run {
                HazeClient.warn("Choice \"$name\" wasn't found in choices \"${this.name}\".")
                return
            }*/
        }

    override val rect = Rect(0f, 0f, 150f, 9f)

    fun choice(name: String, defaultToggled: Boolean) = Choice(name, defaultToggled).apply {
        parent = this@MultiChoiceValue
        inner += this
    }

    fun choice(name: String) = choice(name, false)

    fun choice(choice: Choice) = choice.apply {
        parent = this@MultiChoiceValue
        inner += this
    }

    protected var isShowChoices = false

    override fun render(graphics: GuiGraphicsExtractor) {
        rect.h = 10f

        val isShowSettingsSuffix = if (isShowChoices) "-" else "+"
        val isShowSettingsSuffixWidth = mc.font.width(isShowSettingsSuffix).toFloat()

        //graphics.drawText(isShowSettingsSuffix, rect.x + rect.w - isShowSettingsSuffixWidth, rect.y + 1, -1)

        if (isShowChoices) {
            for (choice in inner) {
                choice.rect.x = rect.x + 5
                choice.rect.y = rect.y + rect.h

                if (
                    FrameInput.clicked[0] &&
                    isCollide(FrameInput.MPos, choice.rect.x, choice.rect.y, mc.font.width(choice.name).toFloat(), 10f)
                ) {
                    choice.toggle()
                }

                //graphics.drawText(choice.name, choice.rect.x, choice.rect.y, -1)

                rect.h += choice.rect.h
            }
        }

        if (FrameInput.clicked[0] && isCollide(
                FrameInput.MPos,
                rect.x + rect.w - isShowSettingsSuffixWidth,
                rect.y + 1,
                isShowSettingsSuffixWidth,
                9f
            )
        )
            isShowChoices = !isShowChoices
    }

    open class Choice internal constructor(
        name: String,
        defaultToggled: Boolean,
    ) : ToggleableConfigureable(name, defaultToggled) {
        lateinit var parent: MultiChoiceValue

        override val rect = Rect(0f, 0f, 270f, 10f)

        override fun render(graphics: GuiGraphicsExtractor) {}
    }
}

/**
 * allows value to go out of range?
 * don't touch.
 */
class IntRangeValue(
    name: String,
    default: IntRange,
    val range: IntRange,
    val suffix: String
) : Value<IntRange>(name, default) {
    override var json: JsonObject
        get() = buildJsonObject {
            put("intRange.first", inner.first)
            put("intRange.last", inner.last)
        }
        set(value) {
            val first = value["intRange.first"]?.jsonPrimitive?.intOrNull ?: run {
                CicadaClient.warn("While loading \"$this\" parameter \"intRange.first\" wasn't found.")
                return
            }

            val last = value["intRange.last"]?.jsonPrimitive?.intOrNull ?: run {
                CicadaClient.warn("While loading \"$this\" parameter \"intRange.last\" wasn't found.")
                return
            }

            inner = first..last
        }

    override val rect = Rect(0f, 0f, 150f, 14f)

    var isMinDragging = false
    var isMaxDragging = false

    override fun render(graphics: GuiGraphicsExtractor) {
        val innerCenter = (inner.first + inner.last) / 2
        val innerCenterPos = innerCenter.map(range.first, range.last, (rect.x + rect.w - 100).toInt(), (rect.x + rect.w).toInt())

        if (
            FrameInput.clicked[0] &&
            isCollide(FrameInput.MPos, rect.x + rect.w - 100, rect.y, 100f, rect.h)
        ) {
            if (FrameInput.MPos.x < innerCenterPos) isMinDragging = true
            else isMaxDragging = true
        } else if (FrameInput.released[0]) {
            isMinDragging = false
            isMaxDragging = false
        }

        if (isMinDragging) {
            inner = FrameInput.MPos.x.map(rect.x + rect.w - 100, rect.x + rect.w, range.first.toFloat(), range.last.toFloat()).toInt().coerceIn(range.first, inner.last)..inner.last
        }

        if (isMaxDragging) {
            inner = inner.first..FrameInput.MPos.x.map(rect.x + rect.w - 100, rect.x + rect.w, range.first.toFloat(), range.last.toFloat()).toInt().coerceIn(inner.first, range.last)
        }

        //graphics.drawText(name, rect.x, rect.y + 1, -1)
        val innerStr = "${String.format("%.2f", inner.first)} - ${String.format("%.2f", inner.last)}"
        //graphics.drawText(innerStr, rect.x + rect.w - 100 - mc.font.width(innerStr), rect.y + 1, -1)
        graphics.rect(rect.x + rect.w - 100, rect.y, 100f, rect.h, 0xFF000000.toInt())

        val xPosByInnerStart = rect.x + rect.w - inner.first.map(range.first, range.last, 100, 0)
        val widthByInnerEnd = rect.x + rect.w - inner.last.map(range.first, range.last, 100, 0) - xPosByInnerStart
        graphics.rect(xPosByInnerStart, rect.y, widthByInnerEnd, rect.h, -1)
    }
}

/**
 * allows value to go out of range?
 * don't touch.
 */
class IntValue(
    name: String,
    default: Int,
    val range: IntRange,
    val suffix: String
) : Value<Int>(name, default) {
    override var json: JsonObject
        get() = buildJsonObject {
            put("int", inner)
        }
        set(value) {
            inner = value["int"]?.jsonPrimitive?.intOrNull ?: run {
                CicadaClient.warn("While loading \"$this\" parameter \"int\" wasn't found.")
                return
            }
        }

    override val rect = Rect(0f, 0f, 150f, 14f)

    var isDragging = false

    override fun render(graphics: GuiGraphicsExtractor) {
        val floatInner = inner.toFloat()
        val floatRange = range.first.toFloat()..range.last.toFloat()

        if (!isDragging && FrameInput.clicked[0] && isCollide(FrameInput.MPos, rect.x, rect.y + 9, rect.w, 5f)) {
            isDragging = true
        }

        if (isDragging && FrameInput.released[0]) {
            isDragging = false
        }

        if (isDragging) {
            inner = FrameInput.MPos.x.map(rect.x + 5, rect.x + rect.w, floatRange.start, floatRange.endInclusive).coerceIn(floatRange).toInt()
        }

        //graphics.drawText(name, rect.x, rect.y, -1)
        //graphics.drawText(inner.toString(), rect.x + rect.w - mc.font.width(inner.toString()), rect.y, -1)
        graphics.rect(rect.x, rect.y + 9, rect.w, 5f, 0xFF7F7F7F.toInt(), 3f)
        val innerWidth = floatInner.map(floatRange.start, floatRange.endInclusive, 5f, rect.w)
        graphics.rect(rect.x, rect.y + 9, innerWidth, 5f, 0xFF3776FF.toInt(), 3f)
    }
}

class StringValue(
    name: String,
    default: String
) : Value<String>(name, default) {
    override var json: JsonObject
        get() = buildJsonObject {
            put("string", inner)
        }
        set(value) {
            inner = value["string"]?.jsonPrimitive?.contentOrNull ?: run {
                CicadaClient.warn("While loading \"$this\" parameter \"string\" wasn't found.")
                return
            }
        }

    override val rect = Rect(0f, 0f, 150f, 9f)

    override fun render(graphics: GuiGraphicsExtractor) {}
}

open class ToggleableConfigureable(name: String, defaultToggled: Boolean) : Configureable(name), Toggleable {
    override var json: JsonObject
        get() = buildJsonObject {
            put("info", buildJsonObject {
                put("toggled", toggled)
            })

            put("settings", super.json)
        }
        set(value) {
            value["info"]?.jsonObject?.let { info ->
                toggled = info["toggled"]?.jsonPrimitive?.booleanOrNull ?: run {
                    CicadaClient.warn("While loading \"$this\" parameter \"toggled\" wasn't found.")
                    false
                }
            } ?: CicadaClient.warn("While loading \"$this\" object \"info\" wasn't found.")

            super.json = value["settings"]?.jsonObject ?: run {
                CicadaClient.warn("While loading \"$this\" object \"settings\" wasn't found.")
                return
            }
        }

    override var toggled = defaultToggled
        set(value) {
            if (field != value) {
                field = value

                if (field)
                    onEnable()
                else
                    onDisable()
            }
        }

    override fun render(graphics: GuiGraphicsExtractor) {
        rect1.x = rect.x
        rect1.y = rect.y

        rect.h = 9f
        if (isShowSettings) {
            rect.h += 2
            for (setting in inner) {
                rect.h += setting.rect.h + 2
            }
        }

        val suffix = if (isShowSettings) "-" else "+"
        val suffixWidth = mc.font.width(suffix).toFloat()

        if (FrameInput.clicked[0]) {
            if (isCollide(FrameInput.MPos, rect.x, rect.y, 9f, 9f))
                toggle()

            if (isCollide(FrameInput.MPos, rect.x + rect.w - suffixWidth - 5, rect.y, suffixWidth, 9f))
                isShowSettings = !isShowSettings
        }

        graphics.rect(rect.x, rect.y, 9f, 9f, if (toggled) 0xFF3776FF.toInt() else 0xFF7F7F7F.toInt())
        //graphics.drawText(name, rect.x + 11, rect.y + 1, -1)
        //graphics.drawText(suffix, rect1.x + rect1.w - suffixWidth - 5, rect1.y, -1)

        if (isShowSettings) {
            var offsetY = 2f
            for (setting in inner) {
                setting.rect.x = rect.x + 5
                setting.rect.y = rect.y + rect1.h + offsetY
                setting.render(graphics)
                offsetY += setting.rect.h + 2
            }

            graphics.rect(rect.x, rect.y + rect1.h, 2f, rect.h - rect1.h, 0xFF3776FF.toInt())
        }
    }
}