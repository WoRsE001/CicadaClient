package cicada.client.feature.module.modules.misc

import cicada.client.config.types.preset.TargetFinder
import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.rotation.ai.Data
import cicada.client.utils.client.player
import cicada.client.utils.math.minus
import cicada.client.utils.rotation.Rotation
import cicada.client.utils.rotation.rotation
import cicada.client.utils.rotation.rotationFromDiff

// SCWGxD regrets everything he did. 20.05.2026 8:04.
object ModuleDataRecorder : ClientModule("DataRecorder", ModuleCategory.MISC) {
    private val targetFinder = tree(TargetFinder())
    val dataSet = mutableListOf<Data>()
    private var lastRotation = Rotation.ZERO

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            targetFinder.updateTarget()

            if (targetFinder.target != null) {
                val target = targetFinder.target!!
                val point = target.eyePosition
                val diff = point - player.eyePosition
                val rotation = rotationFromDiff(diff)
                dataSet += Data(diff, (player.rotation() - lastRotation).wrapped(), rotation)
                lastRotation = rotation
            }
        }
    }

    fun parse(): List<Pair<DoubleArray, DoubleArray>> {
        return List(dataSet.size) { i ->
            val input = doubleArrayOf(dataSet[i].diff.x / 6, dataSet[i].diff.y / 6, dataSet[i].diff.z / 6)
            val output = doubleArrayOf(dataSet[i].rotation.x / 90.0, dataSet[i].rotation.y / 180.0)
            Pair(input, output)
        }
    }
}