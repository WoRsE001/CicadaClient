package cicada.client.feature.module.modules.movement.noslowdown.mode.swordblock

import cicada.client.event.Event
import cicada.client.event.events.EventPacket
import cicada.client.setting.value.ChoiceValue
import cicada.client.utils.client.connection
import cicada.client.utils.client.player
import cicada.client.utils.player.isMoving
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
import net.minecraft.world.phys.BlockHitResult

class SwordBlockIntaveMode : ChoiceValue.Choice("Intave") {
    override fun onEvent(event: Event) {
        if (event is EventPacket.Send) {
            val packet = event.packet

            if (packet is ServerboundUseItemOnPacket) {
                if (player.isUsingItem && player.input.isMoving()) {
                    connection.send(
                        ServerboundUseItemOnPacket(
                            packet.hand, BlockHitResult(
                                packet.hitResult.blockPos.center,
                                packet.hitResult.direction,
                                packet.hitResult.blockPos,
                                packet.hitResult.isInside
                            ), packet.sequence
                        )
                    )
                }
            }
        }
    }
}