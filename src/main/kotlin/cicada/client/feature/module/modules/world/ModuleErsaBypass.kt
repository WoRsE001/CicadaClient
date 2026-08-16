package cicada.client.feature.module.modules.world

import cicada.client.event.Event
import cicada.client.event.events.EventClickTiming
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.rotation.CameraRotation
import cicada.client.rotation.Rotation
import cicada.client.rotation.Rotator
import cicada.client.utils.client.gameMode
import cicada.client.utils.client.level
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.math.minus
import cicada.client.utils.math.plus
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

object ModuleErsaBypass : ClientModule("ErsaBypass", ModuleCategory.WORLD), Rotator {
    var y = 0

    override val rotatePriority = 0

    init {
        registerToRotations()
    }

    override fun onEnable() {
        y = player.blockY
    }

    override fun onEvent(event: Event) {
        if (event is EventClickTiming) {
            val hitResult = mc.hitResult as? BlockHitResult ?: return
            if (!level.getBlockState(hitResult.blockPos).isAir) return
            val targetBlock = BlockPos(player.blockX, y - 1, player.blockY)
            val location = player.eyePosition + (Vec3(targetBlock.x.toDouble(), targetBlock.y.toDouble() - 1, targetBlock.z.toDouble()) - player.eyePosition)
            val newHitResult = BlockHitResult(
                location,
                hitResult.direction,
                targetBlock,
                hitResult.isInside
            )
            gameMode.useItemOn(player, InteractionHand.MAIN_HAND, newHitResult)
        }
    }

    override fun willRotate() = shouldListenEvents()

    override fun rotate() {
        val delta = (Rotation(90f, CameraRotation.y) - player.rotation()).wrapped()
        player.rotate(delta)
    }
}