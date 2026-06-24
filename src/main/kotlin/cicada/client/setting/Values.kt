package cicada.client.setting

import cicada.client.event.Event
import cicada.client.setting.MultiChoiceValue.Choice
import cicada.client.utils.math.Color4f
import kotlinx.serialization.json.*
import kotlinx.serialization.json.put
import kotlin.ranges.rangeTo

class BooleanValue(
    name: String,
    default: Boolean,
    description: String = ""
) : Value<Boolean>(name, default, description) {
    override fun asJson(): JsonObject = buildJsonObject {
        put("toggled", inner)
    }

    override fun fromJson(jsonObject: JsonObject) {
        inner = jsonObject["toggled"]?.jsonPrimitive?.booleanOrNull ?: run { return }
    }

    fun toggle() {
        inner = !inner
    }
}

open class ChoiceValue(
    name: String,
    description: String = ""
) : Value<ChoiceValue.Choice?>(name, null, description) {
    private val _choices = mutableListOf<Choice>()
    val choices: List<Choice>
        get() = _choices

    override fun asJson(): JsonObject = buildJsonObject {
        put("choice", inner?.name)
    }

    override fun fromJson(jsonObject: JsonObject) {
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
    default: Color4f,
    description: String = ""
) : Value<Color4f>(name, default, description) {
    override fun asJson(): JsonObject = buildJsonObject {
        put("color", inner.toInt())
    }

    override fun fromJson(jsonObject: JsonObject) {
        inner.setFromARGB(jsonObject["color"]?.jsonPrimitive?.intOrNull ?: run { return })
    }
}

open class Configurable(
    name: String,
    description: String = "",
    default: MutableCollection<Value<*>> = mutableListOf()
) : Value<MutableCollection<Value<*>>>(name, default, description) {
    var owner: Configurable? = null

    override fun asJson(): JsonObject = buildJsonObject {
        for (value in inner) {
            put(value.name, value.asJson())
        }
    }

    override fun fromJson(jsonObject: JsonObject) {
        for (item in inner) {
            item.fromJson(jsonObject[item.name]?.jsonObject ?: run { continue })
        }
    }

    override fun resetToDefault() {
        for (value in inner) {
            value.resetToDefault()
        }
    }

    fun boolean(
        name: String,
        default: Boolean,
        description: String = ""
    ) = BooleanValue(name, default, description).apply {
        this@Configurable.inner += this
    }

    fun choice(
        name: String,
        description: String = ""
    ) = ChoiceValue(name, description).apply {
        this@Configurable.inner += this
    }

    fun color(
        name: String,
        default: Color4f,
        description: String = ""
    ) = ColorValue(name, default, description).apply {
        this@Configurable.inner += this
    }

    fun float(
        name: String,
        default: Float,
        range: ClosedRange<Float>,
        suffix: String = "",
        description: String = ""
    ) = FloatValue(name, default, range, suffix, description).apply {
        this@Configurable.inner += this
    }

    fun floatRange(
        name: String,
        default: ClosedRange<Float>,
        range: ClosedRange<Float>,
        suffix: String = "",
        description: String = ""
    ) = FloatRangeValue(name, default, range, suffix, description).apply {
        this@Configurable.inner += this
    }

    fun multiChoice(
        name: String,
        description: String = ""
    ) = MultiChoiceValue(name, description).apply {
        this@Configurable.inner += this
    }

    fun group(
        name: String,
        description: String = ""
    ) = Configurable(name, description).apply {
        this@Configurable.inner += this
    }

    fun int(
        name: String,
        default: Int,
        range: IntRange,
        suffix: String = "",
        description: String = ""
    ) = IntValue(name, default, range, suffix, description).apply {
        this@Configurable.inner += this
    }

    fun intRange(
        name: String,
        default: IntRange,
        range: IntRange,
        suffix: String = "",
        description: String = ""
    ) = IntRangeValue(name, default, range, suffix, description).apply {
        this@Configurable.inner += this
    }

    fun string(
        name: String,
        default: String,
        description: String = ""
    ) = StringValue(name, default, description).apply {
        this@Configurable.inner += this
    }

    fun toggleableGroup(
        name: String,
        default: Boolean,
        description: String = ""
    ) = ToggleableConfigurable(name, default, description).apply {
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
    val suffix: String,
    description: String = ""
) : Value<ClosedRange<Float>>(name, default, description) {
    override fun asJson(): JsonObject = buildJsonObject {
        put("floatRange.start", inner.start)
        put("floatRange.end", inner.endInclusive)
    }

    override fun fromJson(jsonObject: JsonObject) {
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
    val suffix: String = "",
    description: String = ""
) : Value<Float>(name, default, description) {
    override fun asJson(): JsonObject = buildJsonObject {
        put("float", inner)
    }

    override fun fromJson(jsonObject: JsonObject) {
        inner = jsonObject["float"]?.jsonPrimitive?.floatOrNull ?: run { return }
    }
}

open class MultiChoiceValue(
    name: String,
    description: String = ""
) : Value<MutableList<Choice>>(name, mutableListOf(), description) {
    override fun asJson(): JsonObject = buildJsonObject {
        for (choice in inner) {
            put(choice.name, choice.toggled)
        }
    }

    override fun fromJson(jsonObject: JsonObject) {
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
    val suffix: String,
    description: String = ""
) : Value<IntRange>(name, default, description) {
    override fun asJson(): JsonObject = buildJsonObject {
        put("intRange.first", inner.first)
        put("intRange.last", inner.last)
    }

    override fun fromJson(jsonObject: JsonObject) {
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
    val suffix: String,
    description: String = ""
) : Value<Int>(name, default, description) {
    override fun asJson(): JsonObject = buildJsonObject {
        put("int", inner)
    }

    override fun fromJson(jsonObject: JsonObject) {
        inner = jsonObject["int"]?.jsonPrimitive?.intOrNull ?: run { return }
    }
}

class StringValue(
    name: String,
    default: String,
    description: String = ""
) : Value<String>(name, default, description) {
    override fun asJson(): JsonObject = buildJsonObject {
        put("string", inner)
    }

    override fun fromJson(jsonObject: JsonObject) {
        inner = jsonObject["string"]?.jsonPrimitive?.contentOrNull ?: run { return }
    }
}

open class ToggleableConfigurable(
    name: String,
    defaultToggled: Boolean,
    description: String = ""
) : Configurable(name, description), Toggleable {
    override fun asJson(): JsonObject = buildJsonObject {
        put("toggled", toggled)
        put("settings", super.asJson())
    }

    override fun fromJson(jsonObject: JsonObject) {
        toggled = jsonObject["toggled"]?.jsonPrimitive?.booleanOrNull ?: false
        super.fromJson(jsonObject)
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
