package cicada.client.feature.module.modules.world.autobridge

import cicada.client.rotation.CameraRotation
import cicada.client.rotation.Rotation
import cicada.client.rotation.Rotator
import cicada.client.setting.value.Configurable
import cicada.client.utils.client.player
import cicada.client.utils.math.roundTo
import cicada.client.utils.player.utilAirTick
import cicada.client.utils.player.utilGroundTick
import cicada.client.utils.raycast.rayCast
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import kotlin.math.abs

object AutoBridgeAimer : Configurable("Aimer"), Rotator {
    private val pitch = group("Pitch")
        private val pitchesSelection = PitchSelection.NEAREST
        private val pitchSpeed by pitch.float("Speed", 180f, 0f..180f)
    private val yaw = group("Yaw")
        private val yawOffset by yaw.float("Offset", 45f, 0f..50f)
        private val yawRounding by yaw.boolean("Rounding", true)
        private val yawRound by yaw.int("Round", 45, 1..90).visible { yawRounding }
        private val yawNearest by yaw.boolean("Nearest", true)
        private val yawSpeed by yaw.float("Speed", 180f, 0f..180f)
        private val telly = yaw.toggleableGroup("Telly", true)
            private val tellyGroundTicks by telly.int("GroundTicks", 0, 0..10)
            private val tellyAirTicks by telly.int("AirTicks", 3, 0..10)
            private val tellyYawOffset by telly.float("TellyYawOffset", 0f, 0f..50f)
            private val tellyYawSpeed by telly.float("TellyYawSpeed", 180f, 0f..180f)

    val target: BlockPos?
        get() = ModuleAutoBridge.target

    override val rotatePriority = 0

    init {
        registerToRotations()
    }

    override fun willRotate() = ModuleAutoBridge.toggled && target != null

    override fun rotate() {
        var yaw = CameraRotation.y
        if (!isTelly()) yaw -= 180f
        if (yawRounding) yaw = yaw.roundTo(yawRound.toFloat())
        if (yaw.roundTo(45f) % 90 == 0f && !isTelly()) yaw += yawOffset
        if (isTelly()) yaw += tellyYawOffset
        yaw = Mth.wrapDegrees(yaw)
        var pitch: Float
        if (yawNearest && !isTelly()) {
            val nearestRotation = nearestRotation(yaw) ?: return
            pitch = nearestRotation.x
            yaw = nearestRotation.y
        } else {
            pitch = pitch(yaw) ?: player.xRot
        }
        val rotation = Rotation(pitch, yaw)
        val delta = (rotation - player.rotation()).wrapped()
        delta.clamp(pitchSpeed, if (isTelly()) tellyYawSpeed else yawSpeed)
        player.rotate(delta)
    }

    private fun isTelly() = telly.toggled &&
            if (player.onGround())
                utilGroundTick >= tellyGroundTicks
            else
                utilAirTick < tellyAirTicks

    fun nearestRotation(guessYaw: Float): Rotation? {
        var nearestYaw: Float? = null
        var bestPitch: Float? = null

        for (i in -180..180) {
            val yaw = i.toFloat()
            val pitch = pitch(yaw) ?: continue
            if (nearestYaw == null || abs(yaw - guessYaw) < abs(yaw - nearestYaw)) {
                nearestYaw = yaw
                bestPitch = pitch
            }
        }

        return if (nearestYaw == null || bestPitch == null) null else Rotation(bestPitch, nearestYaw)
    }

    fun pitch(yaw: Float): Float? {
        val pitches = validPitches(yaw)
        return if (pitches.isEmpty()) null else pitchesSelection.select(pitches)
    }

    fun validPitches(yaw: Float): List<Float> {
        val pitches = mutableListOf<Float>()

        for (pitch in 0..90) {
            val rayCast = player.rayCast(Rotation(pitch.toFloat(), yaw), 4.5f)
            if (rayCast.blockPos != target || rayCast.direction == Direction.UP) continue
            pitches += pitch.toFloat()
        }

        return pitches
    }

    private enum class PitchSelection(val select: (List<Float>) -> Float) {
        HIGHEST({ it.max() }),
        LOWEST({ it.min() }),
        NEAREST({ pitches -> pitches.minBy{ abs(it - player.xRot) } }),
    }
}