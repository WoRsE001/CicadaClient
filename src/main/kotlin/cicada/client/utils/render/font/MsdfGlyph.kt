package cicada.client.utils.render.font

import com.mojang.blaze3d.vertex.VertexConsumer
import org.joml.Matrix3x2fc

class MsdfGlyph(
    data: GlyphData,
    atlasWidth: Float,
    atlasHeight: Float
) {
    private val unicode = data.unicode
    private val advance = data.advance

    private val minU =      data.atlasBounds.left   / atlasWidth
    private val maxU =      data.atlasBounds.right  / atlasWidth
    private val minV = 1f - data.atlasBounds.top    / atlasHeight
    private val maxV = 1f - data.atlasBounds.bottom / atlasHeight

    private val planeLeft = data.planeBounds.left
    private val planeRight = data.planeBounds.right
    private val planeTop = data.planeBounds.top
    private val planeBottom = data.planeBounds.bottom

    fun apply(pose: Matrix3x2fc, vertexConsumer: VertexConsumer, x: Float, y: Float, size: Float, color: Int): Float {
        val x0 = x + planeLeft * size
        val x1 = x + planeRight * size

        val y0 = y - planeTop * size
        val y1 = y - planeBottom * size

        vertexConsumer.addVertexWith2DPose(pose, x0, y1).setUv(minU, maxV).setColor(color)
        vertexConsumer.addVertexWith2DPose(pose, x1, y1).setUv(maxU, maxV).setColor(color)
        vertexConsumer.addVertexWith2DPose(pose, x1, y0).setUv(maxU, minV).setColor(color)
        vertexConsumer.addVertexWith2DPose(pose, x0, y0).setUv(minU, minV).setColor(color)

        return advance * size
    }

    fun width(size: Float): Float {
        return advance * size
    }
}