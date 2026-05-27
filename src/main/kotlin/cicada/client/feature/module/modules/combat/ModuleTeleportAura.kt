package cicada.client.feature.module.modules.combat

import cicada.client.config.types.preset.TargetFinder
import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.utils.client.connection
import cicada.client.utils.client.gameMode
import cicada.client.utils.client.player
import cicada.client.utils.math.minus
import cicada.client.utils.math.plus
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.world.InteractionHand
import net.minecraft.world.phys.Vec3
import kotlin.math.min

// SCWGxD regrets everything he did. 21.05.2026 12:16.
object ModuleTeleportAura : ClientModule("TeleportAura", ModuleCategory.COMBAT) {
    val targetFinder = tree(TargetFinder())
    private val attackRange by float("Attack range", 6f, 0f..6f)
    private val tpStep by float("TP step", 9f, 0f..20f)

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            targetFinder.updateTarget()

            if (targetFinder.target != null && targetFinder.target!!.hurtTime <= 0) {
                for (point in getPath(player.position(), targetFinder.target!!.position(), attackRange, tpStep)) {
                    connection.send(ServerboundMovePlayerPacket.PosRot(point, player.yRot, player.xRot, true, true))
                }

                gameMode.attack(player, targetFinder.target!!)
                player.swing(InteractionHand.MAIN_HAND)
            }
        }
    }

    private fun getPath(point1: Vec3, point2: Vec3, attackRange: Float, stepLength: Float): List<Vec3> {
        val path = mutableListOf<Vec3>()
        val diff = point2 - point1
        var point = point1
        while (point.distanceTo(point2) > attackRange) {
            point += diff.normalize().multiply(
                min(point.distanceTo(point2), stepLength.toDouble()),
                min(point.distanceTo(point2), stepLength.toDouble()),
                min(point.distanceTo(point2), stepLength.toDouble()),
            )

            path.add(point)
        }

        return path
    }
}