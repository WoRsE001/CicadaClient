package cicada.client.feature.module.modules.movement.speed.mode

import cicada.client.event.Event
import cicada.client.event.impl.EventSendInput
import cicada.client.event.impl.PlayerStateUpdateEvent
import cicada.client.setting.value.ChoiceValue
import cicada.client.utils.client.level
import cicada.client.utils.client.player
import net.minecraft.world.entity.player.Input

// SCWGxD regrets everything he did. 24.06.2026 0:08.
object SpeedIntave : ChoiceValue.Choice("Intave") {
    override fun onEvent(event: Event) {
        if (event is EventSendInput) {
            if (level.getBlockState(player.blockPosition().offset(0, -1, 0)).isAir) {
                event.input = Input(
                    event.input.forward,
                    event.input.backward,
                    event.input.left,
                    event.input.right,
                    event.input.jump,
                    false,
                    event.input.sprint,
                )
            }
        }

        if (event is PlayerStateUpdateEvent.Pre) {
            if (level.getBlockState(player.blockPosition().offset(0, -1, 0)).isAir) {
                player.deltaMovement = player.deltaMovement.multiply(1.043, 1.0, 1.043)
            }
        }
    }
}