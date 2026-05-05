package cicada.client.utils.render

import cicada.client.utils.math.normalize
import cicada.client.utils.mc
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import org.joml.Matrix3x2f
import org.joml.Matrix3x2fc
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW

// SCWGxD regrets everything he did. 31.03.2026 16:32.
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
            FILLED_TRIANGLES_PIPELINE,
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

fun GuiGraphicsExtractor.shaderRect(
    pose: Matrix3x2fc,
    x: Float, y: Float, w: Float, h: Float,
    c0: Int, c1: Int, c2: Int, c3: Int,
) {
    val time = GLFW.glfwGetTime().toFloat()

    guiRenderState.addGuiElement(
        ElementToDraw(
            pose,
            SHADER_RECT_PIPELINE,
            x, y,
            w, h,
            { consumer ->
                consumer.addVertexWith2DPose(pose, x, y)            .setColor(c0).setTime(time)
                consumer.addVertexWith2DPose(pose, x, (y + h))      .setColor(c1).setTime(time)
                consumer.addVertexWith2DPose(pose, (x + w), (y + h)).setColor(c2).setTime(time)
                consumer.addVertexWith2DPose(pose, (x + w), y)      .setColor(c3).setTime(time)
            },
            scissorStack.peek()
        )
    )
}

fun GuiGraphicsExtractor.shaderRect(
    x: Float, y: Float, w: Float, h: Float,
    c: Int
) = shaderRect(Matrix3x2f(pose()), x, y, w, h, c, c, c, c)

fun GuiGraphicsExtractor.rect(
    pose: Matrix3x2fc,
    x: Float, y: Float, w: Float, h: Float,
    c0: Int, c1: Int, c2: Int, c3: Int,
    r0: Float, r1: Float, r2: Float, r3: Float
) {
    guiRenderState.addGuiElement(
        ElementToDraw(
            pose,
            ROUNDED_RECT_PIPELINE,
            x, y,
            w, h,
            { consumer ->
                consumer.addVertexWith2DPose(pose, x, y)            .setColor(c0).setRounding(r0, r1, r2,r3).setDimensions(0f, 0f, w, h)
                consumer.addVertexWith2DPose(pose, x, (y + h))      .setColor(c1).setRounding(r0, r1, r2,r3).setDimensions(0f, h, w, h)
                consumer.addVertexWith2DPose(pose, (x + w), (y + h)).setColor(c2).setRounding(r0, r1, r2,r3).setDimensions(w, h, w, h)
                consumer.addVertexWith2DPose(pose, (x + w), y)      .setColor(c3).setRounding(r0, r1, r2,r3).setDimensions(w, 0f, w, h)
            },
            scissorStack.peek()
        )
    )
}

fun GuiGraphicsExtractor.rect(
    x: Float, y: Float, w: Float, h: Float,
    c1: Int, c2: Int, c3: Int, c4: Int,
    r1: Float, r2: Float, r3: Float, r4: Float
) = rect(Matrix3x2f(pose()), x, y, w, h, c1, c2, c3, c4, r1, r2, r3, r4)

fun GuiGraphicsExtractor.rect(
    x: Float, y: Float, w: Float, h: Float,
    c1: Int, c2: Int, c3: Int, c4: Int,
) = rect(Matrix3x2f(pose()), x, y, w, h, c1, c2, c3, c4, 0f, 0f, 0f, 0f)

fun GuiGraphicsExtractor.rect(
    x: Float, y: Float,
    w: Float, h: Float,
    c: Int,
    r: Float
) = rect(x, y, w, h, c, c, c, c, r, r, r, r)

fun GuiGraphicsExtractor.rect(
    x: Float, y: Float,
    w: Float, h: Float,
    c: Int
) = rect(x, y, w, h, c, c, c, c, 0f, 0f, 0f, 0f)

fun GuiGraphicsExtractor.image(
    pose: Matrix3x2fc,
    x: Float, y: Float, w: Float, h: Float,
    c0: Int, c1: Int, c2: Int, c3: Int,
    r0: Float, r1: Float, r2: Float, r3: Float,
    textureLocation: Identifier
) {
    val texture = mc.textureManager.getTexture(textureLocation)

    guiRenderState.addGuiElement(
        TexturedElementToDraw(
            pose,
            RenderPipelines.GUI_TEXTURED,
            x, y, w, h,
            { consumer ->
                consumer.addVertexWith2DPose(pose, x, y)            .setUv(0f, 0f).setColor(c0)
                consumer.addVertexWith2DPose(pose, x, (y + h))      .setUv(0f, 1f).setColor(c1)
                consumer.addVertexWith2DPose(pose, (x + w), (y + h)).setUv(1f, 1f).setColor(c2)
                consumer.addVertexWith2DPose(pose, (x + w), y)      .setUv(1f, 0f).setColor(c3)
            },
            scissorStack.peek(),
            TextureSetup.singleTexture(texture.textureView, texture.sampler)
        )
    )
}

fun GuiGraphicsExtractor.image(
    x: Float, y: Float,
    w: Float, h: Float,
    r: Float,
    textureLocation: Identifier
) = image(Matrix3x2f(pose()), x, y, w, h, -1, -1, -1, -1, r, r, r, r, textureLocation)

fun GuiGraphicsExtractor.image(
    x: Float, y: Float,
    w: Float, h: Float,
    textureLocation: Identifier
) = image(x, y, w, h, 0f, textureLocation)

fun GuiGraphicsExtractor.sprite(
    pose: Matrix3x2fc,
    xTexture: Float, yTexture: Float, wTexture: Float, hTexture: Float,
    x: Float, y: Float, w: Float, h: Float,
    c0: Int, c1: Int, c2: Int, c3: Int,
    r0: Float, r1: Float, r2: Float, r3: Float,
    textureLocation: Identifier
) {
    val texture = mc.textureManager.getTexture(textureLocation)

    val u0 = xTexture.normalize(0f, texture.texture.getWidth(0).toFloat())
    val v0 = yTexture.normalize(0f, texture.texture.getHeight(0).toFloat())
    val u1 = u0 + wTexture.normalize(0f, texture.texture.getWidth(0).toFloat())
    val v1 = v0 + hTexture.normalize(0f, texture.texture.getHeight(0).toFloat())

    guiRenderState.addGuiElement(
        TexturedElementToDraw(
            pose,
            ROUNDED_IMAGE_PIPELINE,
            x, y, w, h,
            { consumer ->
                consumer.addVertexWith2DPose(pose, x, y)            .setUv(u0, v0).setColor(c0).setRounding(r0, r1, r2,r3).setDimensions(0f, 0f, w, h)
                consumer.addVertexWith2DPose(pose, x, (y + h))      .setUv(u0, v1).setColor(c1).setRounding(r0, r1, r2,r3).setDimensions(0f, h, w, h)
                consumer.addVertexWith2DPose(pose, (x + w), (y + h)).setUv(u1, v1).setColor(c2).setRounding(r0, r1, r2,r3).setDimensions(w, h, w, h)
                consumer.addVertexWith2DPose(pose, (x + w), y)      .setUv(u1, v0).setColor(c3).setRounding(r0, r1, r2,r3).setDimensions(w, 0f, w, h)
            },
            scissorStack.peek(),
            TextureSetup.singleTexture(texture.textureView, texture.sampler)
        )
    )
}

fun GuiGraphicsExtractor.sprite(
    xTexture: Float, yTexture: Float,
    wTexture: Float, hTexture: Float,
    x: Float, y: Float,
    w: Float, h: Float,
    r: Float,
    textureLocation: Identifier
) = sprite(
    Matrix3x2f(pose()), xTexture, yTexture, wTexture, hTexture,
    x, y, w, h,
    -1, -1, -1, -1,
    r, r, r, r,
    textureLocation
)

fun GuiGraphicsExtractor.isCollide(px: Float, py: Float, x: Float, y: Float, w: Float, h: Float) = px in x..x + w && py in y..y + h

fun GuiGraphicsExtractor.isCollide(p: Vector2f, x: Float, y: Float, w: Float, h: Float) = isCollide(p.x, p.y, x, y, w, h)
