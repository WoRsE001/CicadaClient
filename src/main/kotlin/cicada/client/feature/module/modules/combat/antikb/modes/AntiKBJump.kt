package cicada.client.feature.module.modules.combat.antikb.modes

import cicada.client.event.Event
import cicada.client.event.events.EventMovementInput
import cicada.client.event.events.EventPacket
import cicada.client.utils.client.player
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket

// SCWGxD regrets everything he did. 27.04.2026 9:58.
object AntiKBJump : AntiKBMode("Jump") {
    private val hurtTime by intRange("Hurt time", 6..10, 0..10)
    private val ignoreFallDamage by boolean("Ignore fall damage", true)
    private var shouldJump = false

    override fun onEvent(event: Event) {
        if (shouldJump && event is EventMovementInput && player.hurtTime in hurtTime) {
            event.jump = true
            shouldJump = false
        }

        if (event is EventPacket.Receive) {
            val packet = event.packet

            if (packet is ClientboundSetEntityMotionPacket && packet.id == player.id) {
                shouldJump = Math.random() <= chance && (player.fallDistance < 4 || !ignoreFallDamage)
            }
        }
    }
}