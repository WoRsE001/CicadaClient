package cicada.client.setting.value.list

import cicada.client.event.Event
import cicada.client.setting.value.Configurable

class ValueMode(name: String, defaultValue: Choice? = null) : ValueChoice<ValueMode.Choice>(name, defaultValue) {
    open class Choice(
        name: String,
        val parent: ValueMode
    ) : Configurable(name) {
        fun select() = apply {
            parent.set(this)
        }

        fun selected() = parent.get() == this

        open fun onEnable() {}

        open fun onEvent(event: Event) {}

        open fun onDisable() {}
    }
}