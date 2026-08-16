package cicada.client.feature.module.modules.world.autobridge

// SCWGxD regrets everything he did. 01.05.2026 13:02.
/*
fun validPitches(heightCheck: Boolean, yaw: Float, target: BlockPos): List<Float> {
    val pitches = mutableListOf<Float>()

    for (pitch in (if (heightCheck) 0 else -90)..90) {
        val rayCast = player.rayCast(Rotation(pitch.toFloat(), yaw), 4.5f)
        if (rayCast.blockPos != target || rayCast.direction == Direction.UP) continue
        pitches += pitch.toFloat()
    }

    return pitches
}

fun pitch(heightCheck: Boolean, yaw: Float, target: BlockPos, pitchesSortMode: PitchesSortMode): Float? {
    val pitches = validPitches(heightCheck, yaw, target)
    return pitchesSortMode.sort(pitches).firstOrNull()
}

fun nearestRotation(heightCheck: Boolean, target: BlockPos, pitchesSortMode: PitchesSortMode, guessYaw: Float): Rotation? {
    var nearestYaw: Float? = null
    var bestPitch: Float? = null

    for (i in -180..180) {
        val yaw = i.toFloat()
        val pitch = pitch(heightCheck, yaw, target, pitchesSortMode) ?: continue
        if (nearestYaw == null || abs(yaw - guessYaw) < abs(yaw - nearestYaw)) {
            nearestYaw = yaw
            bestPitch = pitch
        }
    }

    return if (nearestYaw == null || bestPitch == null) null else Rotation(bestPitch, nearestYaw)
}*/
