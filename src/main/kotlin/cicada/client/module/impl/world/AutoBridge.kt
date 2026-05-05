package cicada.client.module.impl.world

import cicada.client.event.Event
import cicada.client.event.impl.LegitClickTimingEvent
import cicada.client.event.impl.RenderEvent
import cicada.client.event.impl.TickEvent
import cicada.client.mixin.accessors.AccessorMinecraft
import cicada.client.module.Category
import cicada.client.module.Module
import cicada.client.module.impl.world.autobridge.pitch
import cicada.client.module.impl.world.autobridge.pitchsort.*
import cicada.client.module.impl.world.autobridge.yaw
import cicada.client.rotation.CameraRotation
import cicada.client.rotation.RotationListener
import cicada.client.utils.level
import cicada.client.utils.math.Color4f
import cicada.client.utils.mc
import cicada.client.utils.nullCheck
import cicada.client.utils.player
import cicada.client.utils.player.rayCast
import cicada.client.utils.render.FILLED_QUAD
import cicada.client.utils.render.Render3D
import cicada.client.utils.rotation.Rotation
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.phys.AABB

// SCWGxD regrets everything he did. 01.05.2026 12:19.
object AutoBridge : Module("AutoBridge", Category.WORLD), RotationListener {
    private val blockSearch = group("Block search")
        private val searchRange by blockSearch.float("Search range", 4.5f, 0f..6f)
        private val heightCheck by blockSearch.boolean("Height check", true)
    private val rotation = group("Rotation")
        private val pitch = rotation.group("Pitch")
            private val pitchSpeed by pitch.float("Speed", 180f, 0f..180f)
            private val pitchesSort = pitch.choice("Pitches sort").apply {
                choice(PitchesSortNearestMode).select()
                choice(PitchesSortLowestMode)
                choice(PitchesSortHighestMode)
            }
        private val yaw = rotation.group("Yaw")
            private val yawSpeed by yaw.float("Speed", 180f, 0f..180f)
            private val yawRound by yaw.float("Round", 45f, 0f..90f)

    private var target: BlockPos? = null

    override val rotatePriority = 0

    init {
        registerToRotations()
    }

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            target = blockSearch()
        }

        target?.let {
            if (event is RenderEvent.World) {
                val box = AABB(target!!.x.toDouble(),
                    target!!.y.toDouble(), target!!.z.toDouble(), (target!!.x + 1).toDouble(),
                    (target!!.y + 1).toDouble(), (target!!.z + 1).toDouble()
                )
                Render3D.drawBox(event.poseStack, event.bufferSource, FILLED_QUAD, box, Color4f(1f, 1f, 1f, 1f))
            }

            if (event is LegitClickTimingEvent) {
                val rayCast = player.rayCast(player.rotation(), 4.5f)
                if (rayCast != null && rayCast.blockPos.equals(target!!) && rayCast.direction != Direction.UP) {
                    (mc as AccessorMinecraft).invokeStartUseItem()
                }
            }
        }
    }

    override fun rotate() {
        val yaw = yaw(CameraRotation.y, yawRound)
        val pitch = pitch(heightCheck, yaw, target!!, (pitchesSort.inner as PitchesSortMode))
        val delta = (Rotation(pitch, yaw) - player.rotation()).wrapped().clamped(pitchSpeed, yawSpeed)

        player.rotate(delta)
    }

    override fun willRotate() = toggled && nullCheck() && target != null

    fun blockSearch(): BlockPos? {
        var endBlockPos: BlockPos? = null

        val xRange = (player.eyePosition.x - searchRange).toInt()..(player.eyePosition.x + searchRange).toInt()
        var yRange = (player.eyePosition.y - searchRange).toInt()..(player.eyePosition.y + searchRange).toInt()
        if (heightCheck) yRange = yRange.first..yRange.last.coerceAtMost((player.eyePosition.y - 2).toInt())
        val zRange = (player.eyePosition.z - searchRange).toInt()..(player.eyePosition.z + searchRange).toInt()

        for (x in xRange) { for (y in yRange) { for (z in zRange) {
            if (level.getBlockState(BlockPos(x, y, z)).isAir) continue
            if (
                endBlockPos == null ||
                player.distanceToSqr(x + 0.5, y + 0.5, z + 0.5) < player.distanceToSqr(endBlockPos.x + 0.5, endBlockPos.y + 0.5, endBlockPos.z + 0.5)
            ) endBlockPos = BlockPos(x, y, z)
        } } }

        return endBlockPos
    }
}