package cicada.client.event

@Suppress("UNUSED_EXPRESSION")
object EventCaller {
    private val listeners = mutableSetOf<EventListener>()

    internal operator fun plusAssign(listener: EventListener) {
        if (listener !in listeners)
            listeners += listener
    }

    internal fun call(event: Event) {
        if (event is CancelableEvent)
            event.reset()

        for (listener in listeners) {
            if (!listener.shouldListenEvents())
                continue

            listener.onEvent(event)
        }
    }
}