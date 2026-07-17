package cicada.client.packethandle

import cicada.client.event.Event
import cicada.client.event.EventListener
import cicada.client.event.impl.RenderEvent
import cicada.client.render.Renderer3D
import cicada.client.render.engine.FILLED_QUAD_TYPE
import cicada.client.utils.client.connection
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.math.Color4f
import cicada.client.utils.math.unaryMinus
import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.configuration.ClientConfigurationPacketListener
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 06.07.2026 11:13.
object PacketHandler {
    private val packets = mutableListOf<Pair<Packet<*>, Long>>()
    private val packetKAKAs = mutableListOf<PacketKAKA>()

    var delay = 0

    fun registerPacketKAKA(packetKAKA: PacketKAKA) {
        packetKAKAs += packetKAKA
    }

    fun handlePacket(packet: Packet<*>) {
        packets.add(Pair(packet, System.currentTimeMillis()))
    }

    fun recalcDelay() {
        delay = 0

        for (packetKAKA in packetKAKAs) {
            if (!packetKAKA.shouldDetain()) continue
            delay += packetKAKA.getDelay()
        }
    }

    fun handlePackets() {
        if (mc.connection == null)
            return

        packets.removeIf {
            if (System.currentTimeMillis() - it.second >= delay) {
                connection.connection.send(it.first, null)
                return@removeIf true
            }

            return@removeIf false
        }
    }

    fun handle() {
        recalcDelay()
        handlePackets()
    }

    fun handleWithDelay(packetKAKA: PacketKAKA, delay: Int) {
        this.delay = 0

        for (packetKAKA1 in packetKAKAs) {
            if (!packetKAKA1.shouldDetain()) continue
            if (packetKAKA1 == packetKAKA) {
                this.delay += delay
                continue
            }
            this.delay += packetKAKA.getDelay()
        }

        handlePackets()
    }
}