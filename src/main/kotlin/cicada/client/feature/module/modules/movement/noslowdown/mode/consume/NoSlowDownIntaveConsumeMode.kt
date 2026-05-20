package cicada.client.feature.module.modules.movement.noslowdown.mode.consume

import cicada.client.event.Event
import cicada.client.event.impl.PlayerStateUpdateEvent
import cicada.client.config.types.ChoiceValue
import cicada.client.utils.client.connection
import cicada.client.utils.client.player
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket

// SCWGxD regrets everything he did. 17.04.2026 14:36.
class NoSlowDownIntaveConsumeMode : ChoiceValue.Choice("Intave") {
    override fun onEvent(event: Event) {
        if (event is PlayerStateUpdateEvent.Pre) {
            connection.send(ServerboundPlayerActionPacket(
                    ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM,
                    player.blockPosition(),
                    Direction.UP
            ))
        }
    }
}