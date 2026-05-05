package cicada.client.setting

import kotlinx.serialization.json.JsonObject

interface SaveLoadable {
    var json: JsonObject
}