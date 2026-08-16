package cicada.client.rotation

import cicada.client.event.Event
import cicada.client.event.EventListener
import cicada.client.event.events.EventTick
import cicada.client.utils.client.nullCheck
import cicada.client.utils.client.player
import cicada.client.utils.rotation.gcd
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation

object RotationHandler : EventListener {
    private val listeners = mutableListOf<Rotator>()

    init {
        registerToEvents()
    }

    internal operator fun plusAssign(listener: Rotator) {
        if (listener in listeners)
            return

        listeners += listener
        listeners.sortByDescending { it.rotatePriority }
    }

    override fun onEvent(event: Event) {
        if (event !is EventTick.Pre)
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

    override fun shouldListenEvents() = nullCheck()
}