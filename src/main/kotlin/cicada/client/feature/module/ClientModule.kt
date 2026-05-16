package cicada.client.feature.module

import cicada.client.event.Event
import cicada.client.event.EventListener
import cicada.client.key.KeyListener
import cicada.client.key.Keybind
import cicada.client.config.types.ToggleableConfigurable
import cicada.client.utils.nullCheck
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

// SCWGxD regrets everything he did. 30.03.2026 11:30.
abstract class ClientModule(
    name: String,
    val category: ModuleCategory,
    override var keybind: Keybind = Keybind(),
    defaultToggled: Boolean = false
) : ToggleableConfigurable(name, defaultToggled), EventListener, KeyListener {
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
        ModuleManager += this
        category += this
    }

    override fun onEvent(event: Event) {}
    
    override fun listenEvents() = toggled && nullCheck()

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