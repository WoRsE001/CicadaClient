package cicada.client.feature.module.modules.player.nofalldamage.mode

import cicada.client.event.Event
import cicada.client.event.events.EventPlayerStateUpdate
import cicada.client.event.events.EventSendPos
import cicada.client.event.events.EventTick
import cicada.client.setting.value.ChoiceValue
import cicada.client.utils.client.connection
import cicada.client.utils.client.gameSpeed
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.world.phys.Vec3

object NoFallDamageIntave : ChoiceValue.Choice("Intave") {
    private var tick = 0

    override fun onDisable() {
        mc.gameSpeed = 1f
    }

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            if (tick > 0) {
                if (--tick == 0) {
                    mc.gameSpeed = 1f
                }
            }
        }

        if (event is EventPlayerStateUpdate.Pre) {
            if (tick > 0) {
                player.deltaMovement = Vec3(0.0, 0.0, 0.0)
            }
        }

        if (event is EventSendPos.Post && player.fallDistance > 2.5) {
            connection.send(ServerboundMovePlayerPacket.StatusOnly(true, player.horizontalCollision))
            player.fallDistance = 0.0
            mc.gameSpeed = 0.1f
            tick = 1
        }
    }
}