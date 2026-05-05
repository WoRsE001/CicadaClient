package cicada.client.event

import cicada.client.event.impl.*

@Suppress("UNUSED_EXPRESSION")
object EventCaller {
    private val listeners = mutableSetOf<EventListener>()

    init {
        TickEvent.Pre
        TickEvent.Post

        GameLoopEvent.Pre
        GameLoopEvent.Post

        ChatMessageEvent.Send
        ChatMessageEvent.Receive

        KeyEvent

        PlayerStateUpdateEvent.Pre
        PlayerStateUpdateEvent.Post

        RenderEvent.Gui.Pre
        RenderEvent.Gui.Post
        RenderEvent.World

        MovementInputEvent

        SendPosEvent.Pre
        SendPosEvent.Post
    }

    internal operator fun plusAssign(listener: EventListener) {
        if (listener !in listeners)
            listeners += listener
    }

    internal fun call(event: Event) {
        if (event is CancelableEvent)
            event.reset()

        for (listener in listeners) {
            if (!listener.listenEvents())
                continue

            listener.onEvent(event)
        }
    }
}