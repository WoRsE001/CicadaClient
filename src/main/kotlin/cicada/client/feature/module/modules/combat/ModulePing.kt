package cicada.client.feature.module.modules.combat

import cicada.client.event.Event
import cicada.client.event.events.EventAttack
import cicada.client.event.events.EventGameLoop
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.combat.attackaura.ModuleAttackAura
import cicada.client.packethandle.PacketHandler
import cicada.client.packethandle.PacketDelayer
import cicada.client.utils.client.mc
import cicada.client.utils.client.player

// SCWGxD regrets everything he did. 16.07.2026 14:09.
object ModulePing : ClientModule("Ping", ModuleCategory.COMBAT), PacketDelayer {
    private val delay11 by int("Delay", 500, 0..1000, "ms")
    private val flushCondition = multiChoice("FlushCondition")
    private val attack = flushCondition.choice("Attack", true)
    private val distance = flushCondition.choice("Distance", true)
    private val openScreen = flushCondition.choice("OpenScreen", true)

    init {
        PacketHandler.registerPacketKAKA(this)
    }

    override fun onEvent(event: Event) {
        if (openScreen.toggled && mc.screen != null) {
            handleWithDelay(0)
        }

        if (event is EventAttack.Post && attack.toggled) {
            handleWithDelay(0)
        }

        if (event is EventGameLoop.Pre) {
            val target = ModuleAttackAura.target

            if (target != null) {
                if (distance.toggled && player.position().distanceTo(target.position()) > PacketHandler.serverPos.distanceTo(target.position())) {
                    handleWithDelay(0)
                }
            }
        }
    }

    override fun getDelay(): Int {
        return delay11
    }

    override fun shouldDetain(): Boolean {
        return shouldListenEvents()
    }
}