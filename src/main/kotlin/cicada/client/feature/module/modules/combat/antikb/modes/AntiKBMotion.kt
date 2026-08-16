package cicada.client.feature.module.modules.combat.antikb.modes

import cicada.client.event.Event
import cicada.client.event.events.EventPacket
import cicada.client.utils.client.player
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket

// SCWGxD regrets everything he did. 17.04.2026 11:50.
object AntiKBMotion : AntiKBMode("Motion") {
    private val motionXZ by float("Motion XZ", 0f, -1f..1f)
    private val motionY by float("Motion Y", 0f, -1f..1f)

    override fun onEvent(event: Event) {
        if (event !is EventPacket.Receive) return

        val packet = event.packet

        if (packet is ClientboundSetEntityMotionPacket && packet.id == player.id && Math.random() <= chance) {
            event.cancel()

            val diff = packet.movement.subtract(player.deltaMovement).multiply(
                motionXZ.toDouble(),
                motionY.toDouble(),
                motionXZ.toDouble()
            )

            player.deltaMovement = player.deltaMovement.add(diff)
        }
    }
}