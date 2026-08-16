package cicada.client.feature.module

import cicada.client.event.Event
import cicada.client.event.EventListener
import cicada.client.key.KeyListener
import cicada.client.key.Keybind
import cicada.client.setting.value.ToggleableConfigurable
import cicada.client.utils.client.nullCheck
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

// SCWGxD regrets everything he did. 30.03.2026 11:30.
abstract class ClientModule(
    name: String,
    val category: ModuleCategory,
    override var keybind: Keybind = Keybind(),
    defaultToggled: Boolean = false,
    description: String = ""
) : ToggleableConfigurable(name, defaultToggled, description), EventListener, KeyListener {
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

    init {
        registerToEvents()
        registerToKeybinds()
        Modules += this
        category += this
    }

    override fun asJson(): JsonObject = buildJsonObject {
        put("keybind", buildJsonObject {
            put("key", keybind.key)
            put("hold", keybind.hold)
        })
        for ((key, value) in super.asJson()) {
            put(key, value)
        }
    }

    override fun fromJson(jsonObject: JsonObject) {
        super.fromJson(jsonObject)
        jsonObject["keybind"]?.jsonObject?.let { bind ->
            keybind.key = bind["key"]?.jsonPrimitive?.intOrNull ?: keybind.key
            keybind.hold = bind["hold"]?.jsonPrimitive?.booleanOrNull ?: keybind.hold
        }
    }

    override fun shouldListenEvents() = toggled && nullCheck()

    override fun onEvent(event: Event) {}

    override fun onKey(action: Int) {
        if (action == 2)
            return

        val pressed = action == 1

        if (keybind.hold) {
            toggled = pressed
        } else if (!pressed) {
            toggle()
        }
    }

    override fun listenKeybinds() = nullCheck()
}