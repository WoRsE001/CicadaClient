package cicada.client.feature.module.modules.misc

import cicada.client.event.Event
import cicada.client.event.impl.TickEvent
import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.setting.preset.TargetFinder
import cicada.client.utils.client.player
import cicada.client.utils.math.minus
import cicada.client.utils.player.canCrit
import cicada.client.utils.rotation.Rotation
import cicada.client.utils.rotation.rotation
import kotlinx.serialization.json.*
import net.minecraft.world.entity.player.Input
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 24.06.2026 12:45.
object ModuleRatkaEXE : ClientModule("RatkaEXE", ModuleCategory.MISC) {
    private val targetFinder = tree(TargetFinder())

    val dataSet = mutableListOf<Sample>()
    private var lastRotation = Rotation(0f, 0f)

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            targetFinder.updateTarget()

            if (targetFinder.target != null) {
                val target = targetFinder.target!!

                dataSet += Sample(
                    target.position() - player.position(),
                    player.rotation() - lastRotation,
                    player.canCrit(),
                    player.hurtTime,
                    target.hurtTime,
                    player.input.keyPresses
                )
            }

            lastRotation = player.rotation()
        }
    }

    data class Sample(
        val diff: Vec3,
        val delta: Rotation,
        val canCrit: Boolean,
        val playerHurtTime: Int,
        val targetHurtTime: Int,
        val input: Input
    ) {
        fun parse(): JsonObject = buildJsonObject {
            put("diff", buildJsonObject {
                put("x", diff.x / 6)
                put("y", diff.y / 6)
                put("z", diff.z / 6)
            })

            put("delta", buildJsonObject {
                put("x", delta.x / 90)
                put("y", delta.y / 180)
            })

            put("canCrit", if (canCrit) 1 else -1)

            put("playerHurtTime", playerHurtTime / 10)
            put("targetHurtTime", targetHurtTime / 10)

            var byteInput = 0
            if (input.forward)  byteInput = byteInput or (1 shl 0)
            if (input.backward) byteInput = byteInput or (1 shl 1)
            if (input.left)     byteInput = byteInput or (1 shl 2)
            if (input.right)    byteInput = byteInput or (1 shl 3)
            if (input.jump)     byteInput = byteInput or (1 shl 4)
            if (input.shift)    byteInput = byteInput or (1 shl 5)
            if (input.sprint)   byteInput = byteInput or (1 shl 6)

            put("input", byteInput)
        }
    }
}