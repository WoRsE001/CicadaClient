package cicada.client.feature.module.modules.combat

import cicada.client.setting.preset.TargetFinder
import cicada.client.event.Event
import cicada.client.event.impl.RenderEvent
import cicada.client.event.impl.TickEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.mixin.accessors.AccessorBlockBehaviour
import cicada.client.render.FILLED_QUAD_TYPE
import cicada.client.render.Renderer3D
import cicada.client.utils.client.connection
import cicada.client.utils.client.gameMode
import cicada.client.utils.client.level
import cicada.client.utils.client.player
import cicada.client.utils.math.AStar
import cicada.client.utils.math.Color4f
import cicada.client.utils.math.unaryMinus
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.world.InteractionHand
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 21.05.2026 12:16.
object ModuleTeleportAura : ClientModule("TeleportAura", ModuleCategory.COMBAT) {
    val targetFinder = tree(TargetFinder())
    private val attackRange by float("Attack range", 6f, 0f..6f)
    private val tpStep by float("TP step", 9f, 0f..20f)

    var path: List<Vec3>? = listOf()

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            targetFinder.updateTarget()

            if (targetFinder.target != null && targetFinder.target!!.hurtTime <= 0) {
                val target = targetFinder.target!!

                path = AStar.findPath(
                    player.position(),
                    target.position(),
                    tpStep.toDouble(),
                    attackRange.toDouble(),
                    { x, y, z ->
                        !(level.getBlockState(BlockPos(x, y, z)).block as AccessorBlockBehaviour).hasCollision
                    }
                )

                if (path != null) {
                    for (position in path!!) {
                        connection.send(ServerboundMovePlayerPacket.Pos(position, true, true))
                    }

                    gameMode.attack(player, targetFinder.target!!)
                    player.swing(InteractionHand.MAIN_HAND)

                    for (position in path!!.reversed()) {
                        connection.send(ServerboundMovePlayerPacket.Pos(position, true, true))
                    }
                }
            }
        }

        if (event is RenderEvent.World) {
            if (path != null) {
                for (position in path!!) {
                    Renderer3D.box(
                        event.bufferSource, event.poseStack, FILLED_QUAD_TYPE,
                        player.boundingBox.move(player.position().unaryMinus()).move(position),
                        Color4f.WHITE
                    )
                }
            }
        }
    }
}