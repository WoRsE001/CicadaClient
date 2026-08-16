package cicada.client.packethandle

import cicada.client.utils.client.connection
import cicada.client.utils.client.mc
import net.fabricmc.loader.impl.lib.sat4j.core.Vec
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.world.phys.Vec3
import java.util.concurrent.CopyOnWriteArrayList

// SCWGxD regrets everything he did. 06.07.2026 11:13.
object PacketHandler {
    val packets = CopyOnWriteArrayList<Pair<Packet<*>, Long>>()
    private val packetDelayers = mutableListOf<PacketDelayer>()

    var delay = 0
    var serverPos = Vec3(0.0, 0.0, 0.0)

    fun registerPacketKAKA(packetDelayer: PacketDelayer) {
        packetDelayers += packetDelayer
    }

    fun handlePacket(packet: Packet<*>) {
        packets.add(Pair(packet, System.currentTimeMillis()))
    }

    fun recalcDelay() {
        delay = 0

        for (packetKAKA in packetDelayers) {
            if (!packetKAKA.shouldDetain()) continue
            delay += packetKAKA.getDelay()
        }
    }

    fun handlePackets() {
        if (mc.connection == null)
            return

        packets.removeIf {
            val packet = it.first
            val packetTime = it.second

            if (System.currentTimeMillis() - packetTime >= delay) {
                connection.connection.send(packet, null)

                if (packet is ServerboundMovePlayerPacket && packet.hasPosition()) {
                    serverPos = Vec3(
                        packet.getX(0.0),
                        packet.getY(0.0),
                        packet.getZ(0.0)
                    )
                }

                return@removeIf true
            }

            return@removeIf false
        }
    }

    fun handle() {
        recalcDelay()
        handlePackets()
    }

    fun handleWithDelay(packetDelayer: PacketDelayer, delay: Int) {
        this.delay = 0

        for (packetKAKA1 in packetDelayers) {
            if (!packetKAKA1.shouldDetain()) continue
            if (packetKAKA1 == packetDelayer) {
                this.delay += delay
                continue
            }
            this.delay += packetDelayer.getDelay()
        }

        handlePackets()
    }
}