package cicada.client.event

import cicada.client.utils.client.nullCheck

interface EventListener {
    fun registerToEvents() {
        EventCaller += this
    }

    fun shouldListenEvents() = nullCheck()

    fun onEvent(event: Event)
}