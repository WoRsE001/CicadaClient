package cicada.client.feature.module.modules.combat

import cicada.client.config.types.preset.TargetFinder
import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.rotation.ai.RotateModel
import cicada.client.rotation.ai.RotateModels
import cicada.client.utils.client.player
import cicada.client.utils.math.coerceIn
import cicada.client.utils.math.minus
import cicada.client.utils.rotation.Rotation
import cicada.client.utils.rotation.rotate
import cicada.client.utils.rotation.rotation
import cicada.client.utils.rotation.rotationFromDiff
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 25.05.2026 7:08.
object ModuleKillAuraAI : ClientModule("KillAuraAI", ModuleCategory.COMBAT) {
    private val targetFinder = tree(TargetFinder())
    private val data = mutableListOf<Data>()
    private var lastRotation = Rotation.ZERO
    var model: RotateModel? = null

    override fun onEnable() {
        model = RotateModels[0]

        data.clear()

        repeat(model!!.inputCount) {
            data += Data(Vec3(0.0, 0.0, 0.0), Rotation(0f, 0f), Rotation(67f, 69f))
        }
    }

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            targetFinder.updateTarget()

            if (targetFinder.target != null) {
                val target = targetFinder.target!!
                val point = target.eyePosition
                val diff = point - player.eyePosition
                val rotation = rotationFromDiff(diff)
                data += Data(diff, (player.rotation() - lastRotation).wrapped(), rotation)
                data.removeFirst()

                if (model != null) {
                    val input = DoubleArray(model!!.inputCount)

                    for (j in 0..<model!!.memory) {
                        input[j * 3 + 0] = data[j].diff.x / 6
                        input[j * 3 + 1] = data[j].diff.y / 6
                        input[j * 3 + 2] = data[j].diff.z / 6
                    }

                    val shift = model!!.memory * 3
                    for (j in 0..<model!!.memory - 1) {
                        input[j * 2 + 0 + shift] = data[j].rotation.x / 90.0
                        input[j * 2 + 1 + shift] = data[j].rotation.y / 180.0
                    }

                    val output = RotateModels[0].predict(input)
                    player.rotate(Rotation(output[0].toFloat() * 90, output[1].toFloat() * 180))
                }
            }

            lastRotation = player.rotation()
        }
    }

    data class Data(val diff: Vec3, val rotation: Rotation, val bestRotation: Rotation)
}