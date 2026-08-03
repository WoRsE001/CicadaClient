package cicada.client.feature.module.modules.visual.druns

import cicada.client.render.Renderer3D
import cicada.client.render.engine.FILLED_DEPTH_QUAD_TYPE
import cicada.client.render.engine.FILLED_QUAD_TYPE
import cicada.client.utils.math.Color4f
import cicada.client.utils.math.gazLarpit
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.phys.AABB
import org.joml.Vector3f
import org.joml.minus
import org.joml.plusAssign

class Drun {
    val lastPosition = Vector3f(0f, 0f, 0f)
    val position = Vector3f(0f, 0f, 0f)
    val size = 0.2f

    fun goto(target: Vector3f, speed: Float) {
        lastPosition.set(position)
        var delta = target - position
        if (delta.length() == 0f) return
        val deltaLength = minOf(speed, delta.length())
        delta = delta.normalize(deltaLength)
        position += delta
    }

    fun draw(bufferSource: MultiBufferSource.BufferSource, poseStack: PoseStack, partialTicks: Float) {
        val smoothPos = Vector3f(
            gazLarpit(partialTicks, lastPosition.x, position.x),
            gazLarpit(partialTicks, lastPosition.y, position.y),
            gazLarpit(partialTicks, lastPosition.z, position.z),
        )

        Renderer3D.box(
            bufferSource, poseStack, FILLED_DEPTH_QUAD_TYPE,
            AABB(
                (smoothPos.x - size / 2).toDouble(),
                (smoothPos.y - size / 2).toDouble(),
                (smoothPos.z - size / 2).toDouble(),
                (smoothPos.x + size / 2).toDouble(),
                (smoothPos.y + size / 2).toDouble(),
                (smoothPos.z + size / 2).toDouble()
            ),
            Color4f.BLACK
        )
    }
}