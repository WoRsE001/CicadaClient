package cicada.client.event

import cicada.client.utils.client.nullCheck

interface EventListener {
    fun onEvent(event: Event)
    fun listenEvents() = nullCheck()

    fun registerToEvents() {
        EventCaller += this
    }
}