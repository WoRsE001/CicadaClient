package cicada.client.event.events

import cicada.client.event.CancelableEvent
import cicada.client.event.Event

interface EventTick {
    object Pre : CancelableEvent()
    object Post : Event
}

interface EventGameLoop {
    object Pre : Event
    object Post : Event
}

object EventWorldChange : Event