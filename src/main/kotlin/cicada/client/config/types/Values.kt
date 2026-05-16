package cicada.client.config.types

import cicada.client.CicadaClient
import cicada.client.event.Event
import cicada.client.config.types.MultiChoiceValue.Choice
import cicada.client.utils.math.Color4f
import kotlinx.serialization.json.*
import kotlin.ranges.rangeTo

class BooleanValue(
    name: String,
    default: Boolean
) : Value<Boolean>(name, default) {
    override fun serializeTo(): JsonObject = buildJsonObject {
        put("toggled", inner)
    }

    override fun deserializeFrom(jsonObject: JsonObject) {
        inner = jsonObject["toggled"]?.jsonPrimitive?.booleanOrNull ?: run { return }
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

    override fun serializeTo(): JsonObject = buildJsonObject {
        put("choice", inner?.name)
    }

    override fun deserializeFrom(jsonObject: JsonObject) {
        val name = jsonObject["choice"]?.jsonPrimitive?.contentOrNull ?: run { return }
        inner = _choices.firstOrNull { it.name == name } ?: run { return }
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
    override fun serializeTo(): JsonObject = buildJsonObject {
        put("color", inner.toInt())
    }

    override fun deserializeFrom(jsonObject: JsonObject) {
        inner.setFromARGB(jsonObject["color"]?.jsonPrimitive?.intOrNull ?: run { return })
    }
}

open class Configurable(
    name: String,
    default: MutableCollection<Value<*>> = mutableListOf()
) : Value<MutableCollection<Value<*>>>(name, default) {
    var owner: Configurable? = null

    override fun serializeTo(): JsonObject = buildJsonObject {
        for (value in inner) {
            put(value.name, value.serializeTo())
        }
    }

    override fun deserializeFrom(jsonObject: JsonObject) {
        for (item in inner) {
            item.deserializeFrom(jsonObject[item.name]?.jsonObject ?: run { continue })
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
    override fun serializeTo(): JsonObject = buildJsonObject {
        put("floatRange.start", inner.start)
        put("floatRange.end", inner.endInclusive)
    }

    override fun deserializeFrom(jsonObject: JsonObject) {
        val start = jsonObject["floatRange.start"]?.jsonPrimitive?.floatOrNull ?: run { return }
        val end = jsonObject["floatRange.end"]?.jsonPrimitive?.floatOrNull ?: run { return }
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
    override fun serializeTo(): JsonObject = buildJsonObject {
        put("float", inner)
    }

    override fun deserializeFrom(jsonObject: JsonObject) {
        inner = jsonObject["float"]?.jsonPrimitive?.floatOrNull ?: run { return }
    }
}

open class MultiChoiceValue(
    name: String
) : Value<MutableList<Choice>>(name, mutableListOf()) {
    override fun serializeTo(): JsonObject = buildJsonObject {
        for (choice in inner) {
            put(choice.name, choice.toggled)
        }
    }

    override fun deserializeFrom(jsonObject: JsonObject) {
        for (choice in inner) {
            choice.toggled = jsonObject[choice.name]?.jsonPrimitive?.booleanOrNull ?: run { return }
        }
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
    override fun serializeTo(): JsonObject = buildJsonObject {
        put("intRange.first", inner.first)
        put("intRange.last", inner.last)
    }

    override fun deserializeFrom(jsonObject: JsonObject) {
        val first = jsonObject["intRange.first"]?.jsonPrimitive?.intOrNull ?: run { return }
        val last = jsonObject["intRange.last"]?.jsonPrimitive?.intOrNull ?: run { return }
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
    override fun serializeTo(): JsonObject = buildJsonObject {
        put("int", inner)
    }

    override fun deserializeFrom(jsonObject: JsonObject) {
        inner = jsonObject["int"]?.jsonPrimitive?.intOrNull ?: run { return }
    }
}

class StringValue(
    name: String,
    default: String
) : Value<String>(name, default) {
    override fun serializeTo(): JsonObject = buildJsonObject {
        put("string", inner)
    }

    override fun deserializeFrom(jsonObject: JsonObject) {
        inner = jsonObject["string"]?.jsonPrimitive?.contentOrNull ?: run { return }
    }
}

    open class ToggleableConfigurable(name: String, defaultToggled: Boolean) : Configurable(name), Toggleable {
        override fun serializeTo(): JsonObject = buildJsonObject {
            put("toggled", toggled)
            put("settings", super.serializeTo())
        }

        override fun deserializeFrom(jsonObject: JsonObject) {
            toggled = jsonObject["toggled"]?.jsonPrimitive?.booleanOrNull ?: false
            super.deserializeFrom(jsonObject)
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
