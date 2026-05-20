package cicada.client.render

import cicada.client.utils.math.Color4f
import cicada.client.utils.client.mc
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.world.phys.AABB
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.minus

// SCWGxD regrets everything he did. 17.05.2026 11:35.
object Renderer3D {
    fun box(
        bufferSource: MultiBufferSource.BufferSource,
        poseStack: PoseStack,
        renderType: RenderType,
        box: AABB,
        color: Color4f
    ) {
        val vertexConsumer = bufferSource.getBuffer(renderType)
        val matrix4f: Matrix4f = poseStack.last().pose()

        val points = listOf(
            Vector3f(box.minX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()),

            Vector3f(box.maxX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()),

            Vector3f(box.minX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()),

            Vector3f(box.minX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()),

            Vector3f(box.minX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()),

            Vector3f(box.maxX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()),
        )

        for (point in points) {
            val renderPoint = point - mc.gameRenderer.mainCamera.position().toVector3f()

            vertexConsumer.addVertex(
                matrix4f,
                renderPoint.x,
                renderPoint.y,
                renderPoint.z,
            ).setColor(color.r, color.g, color.b, color.a)
        }

        bufferSource.endBatch(renderType)
    }
}