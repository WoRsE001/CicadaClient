package cicada.client.feature.module.modules.combat

import cicada.client.event.Event
import cicada.client.event.impl.AttackEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.packethandle.PacketHandler
import cicada.client.packethandle.PacketKAKA

// SCWGxD regrets everything he did. 16.07.2026 14:09.
object ModulePing : ClientModule("Ping", ModuleCategory.COMBAT), PacketKAKA {
    private val delay11 by int("Delay", 500, 0..1000, "ms")

    init {
        PacketHandler.registerPacketKAKA(this)
    }

    override fun onEvent(event: Event) {
        if (event is AttackEvent.Post) {
            PacketHandler.handleWithDelay(this, 0)
        }
    }

    override fun getDelay(): Int {
        return delay11
    }

    override fun shouldDetain(): Boolean {
        return listenEvents()
    }
}