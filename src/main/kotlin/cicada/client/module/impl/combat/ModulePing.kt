package cicada.client.module.impl.combat

import com.google.common.collect.Queues
import cicada.client.event.Event
import cicada.client.event.impl.AttackEvent
import cicada.client.event.impl.GameLoopEvent
import cicada.client.event.impl.PacketEvent
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.utils.connection
import cicada.client.utils.network.sendInvisiblePacket
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket

// SCWGxD regrets everything he did. 29.04.2026 14:34.
object ModulePing : Module("Ping", Category.COMBAT) {
    private val delay by int("Delay", 100, 0..1000)
    private val flushes = multiChoice("Flushes")
    private val flushAttack = flushes.choice("Attack")
    private val flushKnockback = flushes.choice("Knockback")
    private val flushInvAct = flushes.choice("Inventory action")

    private val packetQueue = Queues.newArrayDeque<Pair<Packet<*>, Long>>()

    override fun onDisable() {
        flush()
    }

    override fun onEvent(event: Event) {
        if (event is GameLoopEvent.Pre) {
            packetQueue.removeIf {
                if (System.currentTimeMillis() - it.second >= delay) {
                    connection.sendInvisiblePacket(it.first)
                    return@removeIf true
                }

                return@removeIf false
            }
        }

        if (event is AttackEvent.Pre) {
            if (flushAttack.toggled) {
                flush()
            }
        }

        if (event is PacketEvent.Receive) {
            val packet = event.packet
            if (packet is ClientboundSetEntityMotionPacket) {
                if (flushKnockback.toggled) {
                    flush()
                }
            }
        }

        if (event is PacketEvent.Send) {
            val packet = event.packet

            packetQueue += Pair(packet, System.currentTimeMillis())
            event.cancel()

            if (packet is ServerboundContainerClickPacket) {
                if (flushInvAct.toggled) {
                    flush()
                }
            }
        }
    }

    private fun flush() {
        for (pair in packetQueue) {
            connection.sendInvisiblePacket(pair.first)
        }

        packetQueue.clear()
    }
}