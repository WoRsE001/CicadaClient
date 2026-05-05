package cicada.client.module.impl.combat.antikb

import cicada.client.event.Event
import cicada.client.event.impl.PacketEvent
import cicada.client.setting.ChoiceValue
import cicada.client.utils.player
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket

// SCWGxD regrets everything he did. 17.04.2026 11:50.
object AntiKBMotionMode : ChoiceValue.Choice("Motion") {
    private val motionXZ by float("Motion XZ", 0f, -1f..1f)
    private val motionY by float("Motion Y", 0f, -1f..1f)

    override fun onEvent(event: Event) {
        if (event !is PacketEvent.Receive) return

        if (event.packet is ClientboundSetEntityMotionPacket && event.packet.id == player.id) {
            event.cancel()

            val diff = event.packet.movement.subtract(player.deltaMovement).multiply(
                motionXZ.toDouble(),
                motionY.toDouble(),
                motionXZ.toDouble()
            )

            player.deltaMovement = player.deltaMovement.add(diff)
        }
    }
}