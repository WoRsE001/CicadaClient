package cicada.client.event.impl

import cicada.client.event.CancelableEvent
import cicada.client.event.Event
import net.minecraft.client.input.KeyEvent

// SCWGxD regrets everything he did. 30.03.2026 15:56.
interface ChatMessageEvent {
    object Send : CancelableEvent() {
        lateinit var content: String
    }

    object Receive : CancelableEvent() {
        lateinit var content: String
    }
}

object KeyEvent : Event {
    var action = 0
    lateinit var input: KeyEvent
}

object LegitClickTimingEvent : Event

object MovementInputEvent : Event {
    var forward = false
    var back = false
    var left = false
    var right = false
    var jump = false
    var sneak = false
    var sprint = false
}