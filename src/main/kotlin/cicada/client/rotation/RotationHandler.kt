package cicada.client.rotation

import cicada.client.event.Event
import cicada.client.event.EventListener
import cicada.client.event.impl.TickEvent
import cicada.client.utils.nullCheck
import cicada.client.utils.player
import cicada.client.utils.rotation.gcd
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation

object RotationHandler : EventListener {
    private val listeners = mutableListOf<RotationListener>()

    init {
        registerToEvents()
    }

    internal operator fun plusAssign(listener: RotationListener) {
        if (listener in listeners)
            return

        listeners += listener
        // Да, это тупо сортировать каждый раз список после добавления листенера,
        // но мне было крайне лень придумывать что-либо "умнее".
        // Ну и тем более это в инициализации, где излишней оптимизацией можно пренебречь.
        listeners.sortByDescending { it.rotatePriority }
    }

    override fun onEvent(event: Event) {
        if (event !is TickEvent.Pre)
            return

        for (listener in listeners) {
            if (!listener.willRotate())
                continue

            CameraRotation.unlocked = true
            listener.rotate()
            return
        }

        if (CameraRotation.unlocked) {
            val delta = (CameraRotation - player.rotation()).wrapped()

            if (delta.length() <= gcd()) {
                CameraRotation.unlocked = false
                return
            }

            player.rotate(delta)
        }
    }

    override fun listenEvents() = nullCheck()
}