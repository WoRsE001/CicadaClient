package cicada.client.setting.preset

import cicada.client.event.Event
import cicada.client.event.impl.MovementInputEvent
import cicada.client.event.impl.RelativeMoveEvent
import cicada.client.rotation.CameraRotation
import cicada.client.setting.value.Configurable
import cicada.client.utils.client.player
import net.minecraft.util.Mth
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class MovementCorrector : Configurable("Movement corrector") {
    private val free by boolean("Free", true)
    private val legit by boolean("Legit", true).visible { free }

    fun onEvent(event: Event) {
        if (free) {
            if (legit) {
                if (event is MovementInputEvent) {
                    val z = if (event.forward == event.backward) 0f else (if (event.forward) 1f else -1f)
                    val x = if (event.left == event.right) 0f else (if (event.left) 1f else -1f)

                    val deltaYaw = Mth.wrapDegrees(CameraRotation.y - player.yRot)

                    val newX = x * cos((deltaYaw * Mth.DEG_TO_RAD).toDouble()) - z *
                            sin((deltaYaw * Mth.DEG_TO_RAD).toDouble())
                    val newZ = z * cos((deltaYaw * Mth.DEG_TO_RAD).toDouble()) + x *
                            sin((deltaYaw * Mth.DEG_TO_RAD).toDouble())

                    val movementSideways = newX.roundToInt()
                    val movementForward = newZ.roundToInt()

                    event.forward = movementForward > 0
                    event.backward = movementForward < 0
                    event.left = movementSideways > 0
                    event.right = movementSideways < 0
                }
            } else {
                if (event is RelativeMoveEvent) {
                    event.yaw = CameraRotation.y
                }
            }
        }
    }
}