package cicada.client.feature.module.modules.misc

import cicada.client.event.Event
import cicada.client.event.events.EventTick
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.utils.client.connection
import cicada.client.utils.client.player
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

object ModuleKTLeave : ClientModule("KTLeave", ModuleCategory.MISC) {
    private val speed    by float("Speed", 20f, 0f..40f)
    private val packets  by int("Packets", 30, 0..50)
    private val interval by int("Interval", 1, 0..10)

    private var tickCounter = 0

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            val rad = Math.toRadians(player.yRot.toDouble())
            val dx  = -sin(rad)
            val dz  = cos(rad)
            player.deltaMovement = Vec3(dx * speed, player.deltaMovement.y, dz * speed)
            player.setSprinting(true)


            tickCounter++
            if (tickCounter < interval) return
            tickCounter = 0

            applyBoost()
        }
    }

    private fun applyBoost() {
        val rad = Math.toRadians(player.yRot.toDouble())
        val dx = -sin(rad)
        val dz = cos(rad)
        val x = player.x
        val y = player.y
        val z = player.z
        val yaw = player.yRot
        val pitch = player.xRot

        for (i in 1..packets) {
            val step = (speed * i) / packets
            val ground = (i % 2 == 0)
            connection.send(
                ServerboundMovePlayerPacket.PosRot(
                    x + dx * step,
                    if (ground) y else y + 0.0625,
                    z + dz * step,
                    yaw, pitch,
                    ground, false
                )
            )
        }

        connection.send(
            ServerboundMovePlayerPacket.PosRot(
                x + dx * speed, y, z + dz * speed,
                yaw, pitch, true, false
            )
        )
    }
}