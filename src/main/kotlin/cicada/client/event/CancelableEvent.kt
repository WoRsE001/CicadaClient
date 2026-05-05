package cicada.client.event

open class CancelableEvent : Event {
    var canceled = false
        private set

    fun cancel() {
        canceled = true
    }

    internal fun reset() {
        canceled = false
    }
}