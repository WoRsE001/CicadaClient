package cicada.client.feature.module.modules.player.phase.mode

import cicada.client.event.Event
import cicada.client.event.impl.EventBlockShape
import cicada.client.event.impl.MovementInputEvent
import cicada.client.event.impl.TickEvent
import cicada.client.setting.value.ChoiceValue
import cicada.client.utils.client.connection
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.world.phys.shapes.Shapes

// SCWGxD regrets everything he did. 28.06.2026 7:22.
object PhaseIntave : ChoiceValue.Choice("Intave") {
    private val packetCount by int("Packet count", 5, 0..10)

    override fun onEnable() {

    }

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            if (mc.options.keyAttack.isDown && player.xRot > 80) {
                connection.send(
                    ServerboundPlayerActionPacket(
                        ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                        player.blockPosition().offset(0, -1, 0), Direction.UP
                    )
                )

                repeat(packetCount) {
                    connection.send(
                        ServerboundMovePlayerPacket.Pos(
                            player.position(), player.onGround(), player.horizontalCollision
                        )
                    )
                }
            }
        }

        if (event is MovementInputEvent) {
            /*event.sneak = true
            event.jump = false*/
        }

        if (event is EventBlockShape) {
            //if (event.pos.y >= player.y || mc.options.keyShift.isDown && player.onGround()) {
            EventBlockShape.shape = Shapes.empty()
            //}
        }
    }
}