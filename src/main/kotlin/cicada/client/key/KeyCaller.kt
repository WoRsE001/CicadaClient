package cicada.client.key

import cicada.client.event.Event
import cicada.client.event.EventListener
import cicada.client.event.impl.KeyEvent
import cicada.client.utils.nullCheck

// created by dicves_recode on 22.02.2026
object KeyCaller : EventListener {
    private val listeners = mutableSetOf<KeyListener>()

    init {
        registerToEvents()
    }

    internal operator fun plusAssign(listener: KeyListener) {
        if (listener !in listeners)
            listeners += listener
    }

    override fun onEvent(event: Event) {
        if (event is KeyEvent) {
            for (listener in listeners) {
                if (!listener.listenKeybinds())
                    continue

                val keybind = listener.keybind
                if (keybind == Keybind.NONE || keybind.key != event.input.key)
                    continue

                listener.onKey(event.action)
            }
        }
    }

    override fun listenEvents() = nullCheck()
}
