package cicada.client.module.impl.world.autobridge

import cicada.client.module.impl.world.autobridge.pitchsort.PitchesSortMode
import cicada.client.utils.math.roundTo
import cicada.client.utils.player
import cicada.client.utils.player.rayCast
import cicada.client.utils.rotation.Rotation
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction

// SCWGxD regrets everything he did. 01.05.2026 13:02.
fun yaw(cameraYaw: Float, round: Float): Float {
    var yaw = cameraYaw - 180f
    yaw = yaw.roundTo(round)
    return yaw
}

fun validPitches(heightCheck: Boolean, yaw: Float, target: BlockPos): List<Float> {
    val pitches = mutableListOf<Float>()

    for (pitch in (if (heightCheck) 0 else -90)..90) {
        val rayCast = player.rayCast(Rotation(pitch.toFloat(), yaw), 4.5f)
        if (rayCast == null || !rayCast.blockPos.equals(target) || rayCast.direction == Direction.UP) continue
        pitches += pitch.toFloat()
    }

    return pitches
}

fun pitch(heightCheck: Boolean, yaw: Float, target: BlockPos, pitchesSortMode: PitchesSortMode): Float {
    val pitches = validPitches(heightCheck, yaw, target)
    if (pitches.isEmpty()) return player.xRot
    return pitchesSortMode.sort(pitches)[0]
}