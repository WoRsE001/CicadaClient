package cicada.client.key

import cicada.client.setting.SaveLoadable
import kotlinx.serialization.json.*
import org.lwjgl.glfw.GLFW

// created by dicves_recode on 22.02.2026
data class Keybind(
    var key: Int = GLFW.GLFW_KEY_UNKNOWN,
    var hold: Boolean = false,
    var mouse: Boolean = false
) : SaveLoadable {
    override var json: JsonObject
        get() = buildJsonObject {
            put("key", key)
            put("hold", hold)
            put("mouse", mouse)
        }
        set(value) {
            key = value["key"]?.jsonPrimitive?.intOrNull ?: return
            hold = value["hold"]?.jsonPrimitive?.booleanOrNull ?: return
            mouse = value["mouse"]?.jsonPrimitive?.booleanOrNull ?: return
        }

    companion object {
        val NONE = Keybind()
    }
}
