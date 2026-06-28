package cicada.client.rotation.ai

import cicada.client.rotation.Rotation
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import net.minecraft.world.entity.player.Input
import net.minecraft.world.phys.Vec3

// SCWGxD regrets everything he did. 25.06.2026 18:54.
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
            put("x", diff.x / 6.0)
            put("y", diff.y / 6.0)
            put("z", diff.z / 6.0)
        })

        put("delta", buildJsonObject {
            put("x", delta.x / 90.0)
            put("y", delta.y / 180.0)
        })

        put("canCrit", if (canCrit) 1 else 0)

        put("playerHurtTime", playerHurtTime / 10.0)
        put("targetHurtTime", targetHurtTime / 10.0)

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