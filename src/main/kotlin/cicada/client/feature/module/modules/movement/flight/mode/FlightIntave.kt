package cicada.client.feature.module.modules.movement.flight.mode

import cicada.client.event.Event
import cicada.client.event.events.EventPacket
import cicada.client.event.events.EventPlayerStateUpdate
import cicada.client.event.events.EventSendPos
import cicada.client.event.events.EventTick
import cicada.client.setting.value.ChoiceValue
import cicada.client.utils.client.player
import cicada.client.utils.player.velocityY
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket

object FlightIntave : ChoiceValue.Choice("Intave") {
    var needJump = false
    var sendPos = false
    var flag = false

    var y = 0.0

    override fun onEnable() {
        y = player.y
    }

    override fun onDisable() {
        needJump = false
        sendPos = false
        flag = false
    }

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            if (player.y <= y && !needJump) {
                needJump = true
            }
        }

        if (needJump) {
            if (event is EventSendPos.Pre && !sendPos && !flag) {
                event.y = 0.0
                sendPos = true
            }

            if (event is EventPacket.Receive && sendPos && !flag) {
                val packet = event.packet

                if (packet is ClientboundPlayerPositionPacket) {
                    flag = true
                }
            }

            if (event is EventPlayerStateUpdate.Pre) {
                if (sendPos && flag) {
                    player.velocityY = 0.42

                    needJump = false
                    sendPos = false
                    flag = false
                }
            }
        }
    }
}