package cicada.client.config

import kotlinx.serialization.json.JsonObject

// SCWGxD regrets everything he did. 21.06.2026 12:06.
interface AsJson {
    fun asJson(): JsonObject

    fun fromJson(jsonObject: JsonObject)
}