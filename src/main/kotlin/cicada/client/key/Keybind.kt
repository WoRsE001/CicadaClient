package cicada.client.key

import org.lwjgl.glfw.GLFW

// created by dicves_recode on 22.02.2026
data class Keybind(
    var key: Int = GLFW.GLFW_KEY_UNKNOWN,
    var hold: Boolean = false
) {
    /*override var json: JsonObject
        get() = buildJsonObject {
            put("key", key)
            put("hold", hold)
            put("mouse", mouse)
        }
        set(value) {
            key = value["key"]?.jsonPrimitive?.intOrNull ?: return
            hold = value["hold"]?.jsonPrimitive?.booleanOrNull ?: return
            mouse = value["mouse"]?.jsonPrimitive?.booleanOrNull ?: return
        }*/

    companion object {
        val NONE = Keybind()
    }
}
