package cicada.client.module.impl.world.autobridge.pitchsort

import cicada.client.utils.player
import kotlin.math.abs

// SCWGxD regrets everything he did. 01.05.2026 13:10.
object PitchesSortNearestMode : PitchesSortMode("Nearest") {
    override fun sort(pitches: List<Float>): List<Float> {
        return pitches.sortedBy { abs(it - player.xRot) }
    }
}

object PitchesSortLowestMode : PitchesSortMode("Lowest") {
    override fun sort(pitches: List<Float>): List<Float> {
        return pitches.sortedBy { -it }
    }
}

object PitchesSortHighestMode : PitchesSortMode("Highest") {
    override fun sort(pitches: List<Float>): List<Float> {
        return pitches.sorted()
    }
}