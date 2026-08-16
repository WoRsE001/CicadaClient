package cicada.client.feature.module.modules.world.autobridge

import cicada.client.event.Event
import cicada.client.event.events.EventClickTiming
import cicada.client.event.events.EventRender
import cicada.client.event.events.EventTick
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.render.Renderer3D
import cicada.client.render.engine.FILLED_QUAD_TYPE
import cicada.client.setting.preset.MovementCorrector
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import cicada.client.utils.math.Color4f
import cicada.client.utils.raycast.rayCast
import cicada.client.utils.rotation.rotation
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.phys.AABB

// SCWGxD regrets everything he did. 01.05.2026 12:19.
object ModuleAutoBridge : ClientModule("AutoBridge", ModuleCategory.WORLD) {
    private val targetFinder = tree(AutoBridgeTargetFinder)
    private val aimer = tree(AutoBridgeAimer)
    private val movementCorrector = tree(MovementCorrector())

    val target: BlockPos?
        get() = targetFinder.target

    override fun onDisable() {
        targetFinder.resetTarget()
    }

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            targetFinder.updateTarget()
        }

        target?.let {
            if (event is EventClickTiming) {
                val rayCast = player.rayCast(player.rotation(), 4.5f)
                if (rayCast.blockPos == target!! && rayCast.direction != Direction.UP) {
                    mc.startUseItem()
                }
            }

            /*if (event is EventMovementInput) {
                if (telly.toggled && utilGroundTick > tellyGroundTicks)
                    event.jump = true
            }*/

            movementCorrector.onEvent(event)

            if (event is EventRender.World) {
                val box = AABB(
                    target!!.x.toDouble(),
                    target!!.y.toDouble(), target!!.z.toDouble(), (target!!.x + 1).toDouble(),
                    (target!!.y + 1).toDouble(), (target!!.z + 1).toDouble()
                )
                Renderer3D.box(event.bufferSource, event.poseStack, FILLED_QUAD_TYPE, box, Color4f(1f, 1f, 1f, 1f))
            }
        }
    }

    /*override fun rotate() {
        var yaw = CameraRotation.y
        if (!isTelly()) yaw -= 180f
        yaw += if (isTelly()) tellyYawOffset else yawOffset
        if (!isTelly() && yawNearest)
            yaw = nearestRotation(true, target!!, pitchesSort.inner as PitchesSortMode, Mth.wrapDegrees(yaw))?.y ?: player.yRot
        val pitch = pitch(true, yaw, target!!, pitchesSort.inner as PitchesSortMode) ?: player.xRot
        val delta = (Rotation(pitch, yaw) - player.rotation()).apply { wrap() }
        delta.clampX(pitchSpeed)
        delta.clampY(if (isTelly()) tellyYawSpeed else yawSpeed)
        player.rotate(delta)
    }

    override fun willRotate() = toggled && nullCheck() && target != null



    private fun isTelly() = telly.toggled &&
            if (player.onGround())
                utilGroundTick >= tellyGroundTicks
            else
                utilAirTick < tellyAirTicks*/
}