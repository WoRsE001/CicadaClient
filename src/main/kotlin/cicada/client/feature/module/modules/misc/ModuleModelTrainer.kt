package cicada.client.feature.module.modules.misc

import cicada.client.config.types.preset.TargetFinder
import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.ai.NeuralNetwork
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.rotation.ai.RotateModel
import cicada.client.utils.client.player
import cicada.client.utils.math.coerceIn
import cicada.client.utils.math.minus
import cicada.client.utils.rotation.Rotation
import cicada.client.utils.rotation.rotation
import cicada.client.utils.rotation.rotationFromDiff
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 20.05.2026 8:04.
object ModuleModelTrainer : ClientModule("ModelTrainer", ModuleCategory.MISC) {
    private val targetFinder = tree(TargetFinder())
    private val data = mutableListOf<Data>()
    private var lastRotation = Rotation.ZERO

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            targetFinder.updateTarget()

            if (targetFinder.target != null) {
                val target = targetFinder.target!!
                val point = target.eyePosition
                val diff = point - player.eyePosition
                val rotation = rotationFromDiff(diff)
                data += Data(diff, (player.rotation() - lastRotation).wrapped(), rotation)
                lastRotation = rotation
            }
        }
    }

    fun clearData() {
        data.clear()
    }

    fun parse(model: RotateModel): List<Pair<DoubleArray, DoubleArray>> {
        return List(data.size - model.inputCount + 1) { i ->
            val input = DoubleArray(model.inputCount)
            val output = DoubleArray(2)

            for (j in 0..<model.memory) {
                input[j * 3 + 0] = data[i + j].diff.x / 6
                input[j * 3 + 1] = data[i + j].diff.y / 6
                input[j * 3 + 2] = data[i + j].diff.z / 6
            }

            val shift = model.memory * 3
            for (k in 0..<model.memory - 1) {
                input[k * 2 + 0 + shift] = data[i + k].rotation.x / 90.0
                input[k * 2 + 1 + shift] = data[i + k].rotation.y / 180.0
            }

            output[0] = data[i + model.memory].rotation.x / 90.0
            output[1] = data[i + model.memory].rotation.y / 180.0

            Pair(input, output)
        }
    }

    data class Data(val diff: Vec3, val rotation: Rotation, val bestRotation: Rotation)
}