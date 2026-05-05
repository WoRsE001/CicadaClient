package cicada.client.utils.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.renderer.state.gui.GuiElementRenderState
import org.joml.Matrix3x2fc

open class ElementToDraw(
    val pose: Matrix3x2fc,
    val pipeline: RenderPipeline,
    val x: Float, val y: Float,
    val w: Float, val h: Float,
    val verticesBuilder: (VertexConsumer) -> Unit,
    val scissorArea: ScreenRectangle?,
    val bounds: ScreenRectangle? = createBounds(pose, x.toInt(), y.toInt(), w.toInt(), h.toInt(), scissorArea)
) : GuiElementRenderState {
    override fun buildVertices(vertexConsumer: VertexConsumer) = verticesBuilder(vertexConsumer)

    override fun pipeline() = pipeline
    override fun textureSetup(): TextureSetup = TextureSetup.noTexture()
    override fun scissorArea() = scissorArea
    override fun bounds() = bounds
}

class TexturedElementToDraw(
    pose: Matrix3x2fc,
    pipeline: RenderPipeline,
    x: Float, y: Float,
    w: Float, h: Float,
    verticesBuilder: (VertexConsumer) -> Unit,
    scissorArea: ScreenRectangle?,
    private val textureSetup: TextureSetup = TextureSetup.noTexture(),
    bounds: ScreenRectangle? = createBounds(pose, x.toInt(), y.toInt(), w.toInt(), h.toInt(), scissorArea)
) : ElementToDraw(pose, pipeline, x, y, w, h, verticesBuilder, scissorArea, bounds) {
    override fun textureSetup() = textureSetup
}

fun createBounds(
    pose: Matrix3x2fc,
    x: Int, y: Int,
    w: Int, h: Int,
    scissorArea: ScreenRectangle?
): ScreenRectangle? {
    val bounds = ScreenRectangle(x, y, w, h).transformMaxBounds(pose)
    return if (scissorArea != null) scissorArea.intersection(bounds) else bounds
}
