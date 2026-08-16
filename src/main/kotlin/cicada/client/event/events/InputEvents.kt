package cicada.client.event.events

import cicada.client.event.Event
import net.minecraft.client.input.KeyEvent

// SCWGxD regrets everything he did. 30.03.2026 15:56.
object EventKey : Event {
    var action = 0
    lateinit var input: KeyEvent
}

object EventClickTiming : Event

object EventMovementInput : Event {
    var forward = false
    var backward = false
    var left = false
    var right = false
    var jump = false
    var sneak = false
    var sprint = false
}

object EventMouseTurn : Event {
    var xo = 0.0
    var yo = 0.0
}