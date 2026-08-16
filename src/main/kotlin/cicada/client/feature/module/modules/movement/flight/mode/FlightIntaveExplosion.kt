package cicada.client.feature.module.modules.movement.flight.mode

import cicada.client.event.Event
import cicada.client.event.events.EventPacket
import cicada.client.event.events.EventTick
import cicada.client.setting.value.ChoiceValue
import cicada.client.utils.client.player
import net.minecraft.network.protocol.game.ClientboundExplodePacket
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

object FlightIntaveExplosion : ChoiceValue.Choice("IntaveExplosion") {
    var boosting = false

    override fun onDisable() {
        boosting = false
    }

    override fun onEvent(event: Event) {
        if (boosting && event is EventTick.Pre) {
            player.deltaMovement = Vec3(
                cos(player.yRot) * 5.0,
                -5.0,
                -sin(player.yRot) * 5.0
            )

            boosting = false
        }

        if (event is EventPacket.Receive) {
            val packet = event.packet

            if (packet is ClientboundExplodePacket) {
                boosting = true
            }
        }
    }
}