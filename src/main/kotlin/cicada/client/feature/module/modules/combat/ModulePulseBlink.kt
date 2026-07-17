package cicada.client.feature.module.modules.combat

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.packethandle.PacketHandler
import cicada.client.packethandle.PacketKAKA

// SCWGxD regrets everything he did. 16.07.2026 14:11.
object ModulePulseBlink : ClientModule("PulseBlink", ModuleCategory.COMBAT), PacketKAKA {
    init {
        PacketHandler.registerPacketKAKA(this)
    }

    override fun getDelay(): Int {
        return if (System.currentTimeMillis() % 500 == 0L) 0 else 1000000
    }

    override fun shouldDetain(): Boolean {
        return listenEvents()
    }
}