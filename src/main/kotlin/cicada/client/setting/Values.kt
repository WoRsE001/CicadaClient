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

    fun toggle() {
        inner = !inner
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

    fun choice(name: String) = Choice(name).apply {
        parent = this@ChoiceValue
        _choices += this
    }

    fun choice(choice: Choice) = choice.apply {
        parent = this@ChoiceValue
        _choices += this
    }

    open class Choice internal constructor(
        name: String
    ) : Configurable(name) {
        lateinit var parent: ChoiceValue

        fun select() = apply {
            parent.set(this)
            onEnable()
        }

        fun selected() = parent.get() == this

        open fun onEnable() {}

        open fun onEvent(event: Event) {}

        open fun onDisable() {}
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
}

open class Configurable(
    name: String,
    default: MutableCollection<Value<*>> = mutableListOf()
) : Value<MutableCollection<Value<*>>>(name, default) {
    var owner: Configurable? = null

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

    override fun resetToDefault() {
        for (value in inner) {
            value.resetToDefault()
        }
    }

    fun boolean(
        name: String,
        default: Boolean
    ) = BooleanValue(name, default).apply {
        this@Configurable.inner += this
    }

    fun choice(
        name: String
    ) = ChoiceValue(name).apply {
        this@Configurable.inner += this
    }

    fun color(
        name: String,
        default: Color4f
    ) = ColorValue(name, default).apply {
        this@Configurable.inner += this
    }

    fun float(
        name: String,
        default: Float,
        range: ClosedRange<Float>,
        suffix: String = ""
    ) = FloatValue(name, default, range, suffix).apply {
        this@Configurable.inner += this
    }

    fun floatRange(
        name: String,
        default: ClosedRange<Float>,
        range: ClosedRange<Float>,
        suffix: String = ""
    ) = FloatRangeValue(name, default, range, suffix).apply {
        this@Configurable.inner += this
    }

    fun multiChoice(
        name: String
    ) = MultiChoiceValue(name).apply {
        this@Configurable.inner += this
    }

    fun group(
        name: String
    ) = Configurable(name).apply {
        this@Configurable.inner += this
    }

    fun int(
        name: String,
        default: Int,
        range: IntRange,
        suffix: String = ""
    ) = IntValue(name, default, range, suffix).apply {
        this@Configurable.inner += this
    }

    fun intRange(
        name: String,
        default: IntRange,
        range: IntRange,
        suffix: String = ""
    ) = IntRangeValue(name, default, range, suffix).apply {
        this@Configurable.inner += this
    }

    fun string(
        name: String,
        default: String
    ) = StringValue(name, default).apply {
        this@Configurable.inner += this
    }

    fun toggleableGroup(
        name: String,
        default: Boolean
    ) = ToggleableConfigurable(name, defaultToggled = default).apply {
        this@Configurable.inner += this
    }

    fun <T : Configurable> tree(configurable: T) = configurable.apply {
        this.owner = this
        this@Configurable.inner += this
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

    fun choice(name: String, defaultToggled: Boolean) = Choice(name, defaultToggled).apply {
        parent = this@MultiChoiceValue
        inner += this
    }

    fun choice(name: String) = choice(name, false)

    fun choice(choice: Choice) = choice.apply {
        parent = this@MultiChoiceValue
        inner += this
    }

    open class Choice internal constructor(
        name: String,
        defaultToggled: Boolean,
    ) : ToggleableConfigurable(name, defaultToggled) {
        lateinit var parent: MultiChoiceValue
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
}

open class ToggleableConfigurable(name: String, defaultToggled: Boolean) : Configurable(name), Toggleable {
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
}