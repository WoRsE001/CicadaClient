package cicada.client.feature.module.modules.movement.flight.mode

import cicada.client.event.Event
import cicada.client.event.impl.PacketEvent
import cicada.client.event.impl.RelativeMoveEvent
import cicada.client.feature.module.modules.movement.flight.ModuleFlight
import cicada.client.rotation.CameraRotation
import cicada.client.rotation.Rotator
import cicada.client.setting.value.ChoiceValue
import cicada.client.utils.client.nullCheck
import cicada.client.utils.client.player
import cicada.client.utils.math.roundTo
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
import kotlin.math.roundToInt

// SCWGxD regrets everything he did. 23.06.2026 16:49.
object FlightIntave : ChoiceValue.Choice("Intave"), Rotator {
    private var needRotate = false

    override val rotatePriority = 67

    init {
        registerToRotations()
    }

    override fun onDisable() {
        needRotate = false
    }

    override fun onEvent(event: Event) {
        if (event is RelativeMoveEvent) {
            if (needRotate) {
                val rotation = floatArrayOf(-88.3f, 1.9f, 92.6f, 178.1f)
                val index = Math.floorMod((CameraRotation.y.roundTo(90f) / 90f + 1).roundToInt(), 4)
                event.yaw = rotation[index]
            } else {
                event.yaw = CameraRotation.y.roundTo(90f) + 30
            }
        }

        if (event is PacketEvent.Receive) {
            val packet = event.packet

            if (packet is ClientboundPlayerPositionPacket) {
                needRotate = true
            }
        }
    }

    override fun rotate() {
        val delta = (CameraRotation.rounded(90f, 90f) - player.rotation()).wrapped()
        player.rotate(delta)
    }

    override fun willRotate() =
        nullCheck() && selected() && ModuleFlight.toggled
}