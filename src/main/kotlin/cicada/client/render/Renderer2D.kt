package cicada.client.render

import cicada.client.utils.math.normalize
import cicada.client.utils.client.mc
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.resources.Identifier
import org.joml.Matrix3x2f
import org.joml.Matrix3x2fc

// SCWGxD regrets everything he did. 17.05.2026 10:22.
fun GuiGraphicsExtractor.triangle(
    pose: Matrix3x2fc,
    x0: Float, y0: Float,
    x1: Float, y1: Float,
    x2: Float, y2: Float,
    c0: Int, c1: Int, c2: Int
) {
    guiRenderState.addGuiElement(
        ElementToDraw(
            pose,
            TRIANGLE_PIPELINE,
            listOf(x0, x1, x2).min(), listOf(y0, y1, y2).min(),
            listOf(x0, x1, x2).max(), listOf(y0, y1, y2).max(),
            { consumer ->
                consumer.addVertexWith2DPose(pose, x0, y0).setColor(c0)
                consumer.addVertexWith2DPose(pose, x1, y1).setColor(c1)
                consumer.addVertexWith2DPose(pose, x2, y2).setColor(c2)
            },
            scissorStack.peek()
        )
    )
}

fun GuiGraphicsExtractor.rect(
    pose: Matrix3x2fc,
    x: Float, y: Float, w: Float, h: Float,
    c0: Int, c1: Int, c2: Int, c3: Int,
    r0: Float, r1: Float, r2: Float, r3: Float
) {
    guiRenderState.addGuiElement(
        ElementToDraw(
            pose,
            RECT_PIPELINE,
            x, y, w, h,
            { consumer ->
                consumer.addVertexWith2DPose(pose, x    , y    ).setColor(c0).setRounding(r0, r1, r2,r3).setDimensions(0f, 0f, w, h)
                consumer.addVertexWith2DPose(pose, x    , y + h).setColor(c1).setRounding(r0, r1, r2,r3).setDimensions(0f, h , w, h)
                consumer.addVertexWith2DPose(pose, x + w, y + h).setColor(c2).setRounding(r0, r1, r2,r3).setDimensions(w , h , w, h)
                consumer.addVertexWith2DPose(pose, x + w, y    ).setColor(c3).setRounding(r0, r1, r2,r3).setDimensions(w , 0f, w, h)
            },
            scissorStack.peek()
        )
    )
}

fun GuiGraphicsExtractor.rect(
    x: Float, y: Float, w: Float, h: Float,
    c0: Int = -1, c1: Int = -1, c2: Int = -1, c3: Int = -1,
    r0: Float = 0f, r1: Float = 0f, r2: Float = 0f, r3: Float = 0f
) = rect(Matrix3x2f(pose()), x, y, w, h, c0, c1, c2, c3, r0, r1, r2, r3)

fun GuiGraphicsExtractor.rect(
    x: Float, y: Float, w: Float, h: Float,
    c: Int = -1, r: Float = 0f
) = rect(Matrix3x2f(pose()), x, y, w, h, c, c, c, c, r, r, r, r)

fun GuiGraphicsExtractor.image(
    textureLocation: Identifier,
    pose: Matrix3x2fc,
    x: Float, y: Float, w: Float, h: Float,
    c0: Int, c1: Int, c2: Int, c3: Int,
    r0: Float, r1: Float, r2: Float, r3: Float
) {
    val texture = mc.textureManager.getTexture(textureLocation)

    guiRenderState.addGuiElement(
        TexturedElementToDraw(
            pose,
            IMAGE_PIPELINE,
            x, y, w, h,
            { consumer ->
                consumer.addVertexWith2DPose(pose, x    , y    ).setUv(0f, 0f).setColor(c0).setRounding(r0, r1, r2,r3).setDimensions(0f, 0f, w, h)
                consumer.addVertexWith2DPose(pose, x    , y + h).setUv(0f, 1f).setColor(c1).setRounding(r0, r1, r2,r3).setDimensions(0f, h , w, h)
                consumer.addVertexWith2DPose(pose, x + w, y + h).setUv(1f, 1f).setColor(c2).setRounding(r0, r1, r2,r3).setDimensions(w , h , w, h)
                consumer.addVertexWith2DPose(pose, x + w, y    ).setUv(1f, 0f).setColor(c3).setRounding(r0, r1, r2,r3).setDimensions(w , 0f, w, h)
            },
            scissorStack.peek(),
            TextureSetup.singleTexture(texture.textureView, texture.sampler)
        )
    )
}

fun GuiGraphicsExtractor.image(
    textureLocation: Identifier,
    x: Float, y: Float, w: Float, h: Float,
    c0: Int = -1, c1: Int = -1, c2: Int = -1, c3: Int = -1,
    r0: Float = 0f, r1: Float = 0f, r2: Float = 0f, r3: Float = 0f
) = image(textureLocation, Matrix3x2f(pose()), x, y, w, h, c0, c1, c2, c3, r0, r1, r2, r3)

fun GuiGraphicsExtractor.image(
    textureLocation: Identifier,
    x: Float, y: Float, w: Float, h: Float,
    c: Int = -1, r: Float = 0f
) = image(textureLocation, Matrix3x2f(pose()), x, y, w, h, c, c, c, c, r, r, r, r)

fun GuiGraphicsExtractor.sprite(
    textureLocation: Identifier,
    pose: Matrix3x2fc,
    xTexture: Float, yTexture: Float, wTexture: Float, hTexture: Float,
    x: Float, y: Float, w: Float, h: Float,
    c0: Int, c1: Int, c2: Int, c3: Int,
    r0: Float, r1: Float, r2: Float, r3: Float
) {
    val texture = mc.textureManager.getTexture(textureLocation)

    val u0 = xTexture.normalize(0f, texture.texture.getWidth(0).toFloat())
    val v0 = yTexture.normalize(0f, texture.texture.getHeight(0).toFloat())
    val u1 = u0 + wTexture.normalize(0f, texture.texture.getWidth(0).toFloat())
    val v1 = v0 + hTexture.normalize(0f, texture.texture.getHeight(0).toFloat())

    guiRenderState.addGuiElement(
        TexturedElementToDraw(
            pose,
            IMAGE_PIPELINE,
            x, y, w, h,
            { consumer ->
                consumer.addVertexWith2DPose(pose, x    , y    ).setUv(u0, v0).setColor(c0).setRounding(r0, r1, r2,r3).setDimensions(0f, 0f, w, h)
                consumer.addVertexWith2DPose(pose, x    , y + h).setUv(u0, v1).setColor(c1).setRounding(r0, r1, r2,r3).setDimensions(0f, h , w, h)
                consumer.addVertexWith2DPose(pose, x + w, y + h).setUv(u1, v1).setColor(c2).setRounding(r0, r1, r2,r3).setDimensions(w , h , w, h)
                consumer.addVertexWith2DPose(pose, x + w, y    ).setUv(u1, v0).setColor(c3).setRounding(r0, r1, r2,r3).setDimensions(w , 0f, w, h)
            },
            scissorStack.peek(),
            TextureSetup.singleTexture(texture.textureView, texture.sampler)
        )
    )
}

fun GuiGraphicsExtractor.sprite(
    textureLocation: Identifier,
    xTexture: Float, yTexture: Float, wTexture: Float, hTexture: Float,
    x: Float, y: Float, w: Float, h: Float,
    c0: Int = -1, c1: Int = -1, c2: Int = -1, c3: Int = -1,
    r0: Float = 0f, r1: Float = 0f, r2: Float = 0f, r3: Float = 0f
) = sprite(
    textureLocation,
    Matrix3x2f(pose()),
    xTexture, yTexture, wTexture, hTexture,
    x, y, w, h,
    c0, c1, c2, c3,
    r0, r1, r2, r3
)

fun GuiGraphicsExtractor.sprite(
    textureLocation: Identifier,
    xTexture: Float, yTexture: Float, wTexture: Float, hTexture: Float,
    x: Float, y: Float, w: Float, h: Float,
    c: Int = -1, r: Float = 0f
) = sprite(
    textureLocation,
    Matrix3x2f(pose()),
    xTexture, yTexture, wTexture, hTexture,
    x, y, w, h,
    c, c, c, c,
    r, r, r, r
)

fun GuiGraphicsExtractor.cut(x: Float, y: Float, w: Float, h: Float, block: () -> Unit) {
    enableScissor(x.toInt(), y.toInt(), (x + w).toInt(), (y + h).toInt())
    block()
    disableScissor()
}