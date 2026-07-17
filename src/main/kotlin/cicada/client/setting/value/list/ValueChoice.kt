package cicada.client.setting.value.list

import cicada.client.event.Event
import cicada.client.setting.value.Configurable
import cicada.client.setting.value.Value
import kotlinx.serialization.json.*

// SCWGxD regrets everything he did. 06.07.2026 9:44.
class ValueChoice<T : ValueChoice.Choice>(name: String, defaultValue: T? = null) : Value<T?>(name, defaultValue) {
    private val choices = mutableListOf<T>()

    override fun asJson(): JsonObject = buildJsonObject {
        put("choice", inner?.name)
    }

    override fun fromJson(jsonObject: JsonObject) {
        val name = jsonObject["choice"]?.jsonPrimitive?.contentOrNull ?: run { return }
        inner = choices.firstOrNull { it.name == name } ?: run { return }
    }

    open class Choice(
        name: String
    ) : Configurable(name) {
        open fun onEnable() {}

        open fun onEvent(event: Event) {}

        open fun onDisable() {}
    }
}