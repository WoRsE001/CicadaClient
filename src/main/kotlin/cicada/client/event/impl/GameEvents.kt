package cicada.client.event.impl

import cicada.client.event.CancelableEvent
import cicada.client.event.Event

interface TickEvent {
    object Pre : CancelableEvent()
    object Post : Event
}

interface GameLoopEvent {
    object Pre : Event
    object Post : Event
}

object WorldChangeEvent : Event