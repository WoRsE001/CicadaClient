package cicada.client.feature.module.modules.world.autobridge

import cicada.client.event.Event
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.event.impl.RenderEvent
import cicada.client.event.impl.EventTick
import cicada.client.event.impl.MovementInputEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.modules.world.autobridge.pitchsort.PitchesSortHighestMode
import cicada.client.feature.module.modules.world.autobridge.pitchsort.PitchesSortLowestMode
import cicada.client.feature.module.modules.world.autobridge.pitchsort.PitchesSortMode
import cicada.client.feature.module.modules.world.autobridge.pitchsort.PitchesSortNearestMode
import cicada.client.mixin.accessors.AccessorMinecraft
import cicada.client.render.Renderer3D
import cicada.client.render.engine.FILLED_QUAD_TYPE
import cicada.client.rotation.CameraRotation
import cicada.client.rotation.Rotation
import cicada.client.rotation.Rotator
import cicada.client.setting.preset.MovementCorrector
import cicada.client.utils.client.level
import cicada.client.utils.client.mc
import cicada.client.utils.client.nullCheck
import cicada.client.utils.client.player
import cicada.client.utils.math.Color4f
import cicada.client.utils.math.roundTo
import cicada.client.utils.player.airTick
import cicada.client.utils.player.groundTick
import cicada.client.utils.player.rayCast
import cicada.client.utils.player.utilAirTick
import cicada.client.utils.player.utilGroundTick
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.phys.AABB

// SCWGxD regrets everything he did. 01.05.2026 12:19.
object ModuleAutoBridge : ClientModule("AutoBridge", ModuleCategory.WORLD), Rotator {
    private val blockSearch = group("Block search")
    private val searchRange by blockSearch.float("Search range", 4.5f, 0f..6f)
    private val heightCheck by blockSearch.boolean("Height check", true)
    private val rotation = group("Rotation")
    private val pitch = rotation.group("Pitch")
    private val pitchesSort = pitch.choice("Pitches sort").apply {
        choice(PitchesSortNearestMode).select()
        choice(PitchesSortLowestMode)
        choice(PitchesSortHighestMode)
    }
    private val pitchSpeed by pitch.float("Speed", 180f, 0f..180f)
    private val yaw = rotation.group("Yaw")
    private val yawOffset by yaw.float("Offset", 45f, 0f..50f)
    private val yawNearest by yaw.boolean("Nearest", true)
    private val yawSpeed by yaw.float("Speed", 70f, 0f..180f)
    private val telly = yaw.toggleableGroup("Telly", true)
    private val tellyGroundTicks by telly.int("Ground ticks", 3, 0..10)
    private val tellyAirTicks by telly.int("Air ticks", 3, 0..10)
    private val tellyYawOffset by telly.float("Telly yaw offset", 0f, 0f..50f)
    private val tellyYawSpeed by telly.float("Telly yaw speed", 180f, 0f..180f)
    private val movementCorrector = tree(MovementCorrector())

    private var target: BlockPos? = null

    override val rotatePriority = 0

    init {
        registerToRotations()
    }

    override fun onDisable() {
        target = null
    }

    override fun onEvent(event: Event) {
        if (event is EventTick.Pre) {
            target = blockSearch()
        }

        target?.let {
            if (event is LegitClickTimingEvent) {
                val rayCast = player.rayCast(player.rotation(), 4.5f)
                if (rayCast != null && rayCast.blockPos == target!! && rayCast.direction != Direction.UP) {
                    (mc as AccessorMinecraft).invokeStartUseItem()
                }
            }

            if (event is MovementInputEvent) {
                if (telly.toggled && utilGroundTick > tellyGroundTicks)
                    event.jump = true
            }

            movementCorrector.onEvent(event)

            if (event is RenderEvent.World) {
                val box = AABB(
                    target!!.x.toDouble(),
                    target!!.y.toDouble(), target!!.z.toDouble(), (target!!.x + 1).toDouble(),
                    (target!!.y + 1).toDouble(), (target!!.z + 1).toDouble()
                )
                Renderer3D.box(event.bufferSource, event.poseStack, FILLED_QUAD_TYPE, box, Color4f(1f, 1f, 1f, 1f))
            }
        }
    }

    override fun rotate() {
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

    fun blockSearch(): BlockPos? {
        var endBlockPos: BlockPos? = null

        val xRange = (player.eyePosition.x - searchRange).toInt()..(player.eyePosition.x + searchRange).toInt()
        var yRange = (player.eyePosition.y - searchRange).toInt()..(player.eyePosition.y + searchRange).toInt()
        if (heightCheck) yRange = yRange.first..yRange.last.coerceAtMost((player.eyePosition.y - 2).toInt())
        val zRange = (player.eyePosition.z - searchRange).toInt()..(player.eyePosition.z + searchRange).toInt()

        for (x in xRange) {
            for (y in yRange) {
                for (z in zRange) {
                    if (level.getBlockState(BlockPos(x, y, z)).isAir) continue
                    if (
                        endBlockPos == null ||
                        player.distanceToSqr(x + 0.5, y + 0.5, z + 0.5) < player.distanceToSqr(
                            endBlockPos.x + 0.5,
                            endBlockPos.y + 0.5,
                            endBlockPos.z + 0.5
                        )
                    ) endBlockPos = BlockPos(x, y, z)
                }
            }
        }

        return endBlockPos
    }

    private fun isTelly() = telly.toggled &&
            if (player.onGround())
                utilGroundTick >= tellyGroundTicks
            else
                utilAirTick < tellyAirTicks
}