package cicada.client.setting.value.list

import cicada.client.setting.value.Value
import kotlinx.serialization.json.*

// SCWGxD regrets everything he did. 06.07.2026 9:44.
open class ValueChoice<T>(name: String, defaultValue: T? = null) : Value<T?>(name, defaultValue) {
    private val choices = mutableListOf<T>()

    override fun asJson(): JsonObject = buildJsonObject {
        put("choice", choices.indexOf(inner))
    }

    override fun fromJson(jsonObject: JsonObject) {
        val index = jsonObject["choice"]?.jsonPrimitive?.intOrNull ?: run { return }
        if (index == -1) return
        inner = choices[index] ?: run { return }
    }
}