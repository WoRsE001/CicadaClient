package cicada.client.event

interface Event {
    fun call() = EventCaller.call(this)
}