package cicada.client.render

import cicada.client.font.FontData
import cicada.client.font.kerningKey
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.GuiGraphicsExtractor
import org.joml.Matrix3x2fc
import kotlin.code

// SCWGxD regrets everything he did. 18.05.2026 7:20.
private fun FontData.addVertices(vertexConsumer: VertexConsumer, pose: Matrix3x2fc, text: String, x: Float, y: Float, size: Float, color: Int) {
    var xOffset = 0f
    val baseLine = y + metrics.ascender * size

    for (i in text.indices) {
        val charCode = text[i].code

        if (i > 0) {
            val prevCode = text[i - 1].code
            val kern = kernings[kerningKey(prevCode, charCode)]
            if (kern != null) xOffset += kern * size
        }

        val glyph = glyphs[charCode] ?: continue

        val currentX = x + xOffset

        val x0 = currentX + glyph.planeLeft * size
        val x1 = currentX + glyph.planeRight * size
        val y0 = baseLine - glyph.planeTop * size
        val y1 = baseLine - glyph.planeBottom * size

        vertexConsumer.addVertexWith2DPose(pose, x0, y1).setUv(glyph.minU, glyph.maxV).setColor(color)
        vertexConsumer.addVertexWith2DPose(pose, x1, y1).setUv(glyph.maxU, glyph.maxV).setColor(color)
        vertexConsumer.addVertexWith2DPose(pose, x1, y0).setUv(glyph.maxU, glyph.minV).setColor(color)
        vertexConsumer.addVertexWith2DPose(pose, x0, y0).setUv(glyph.minU, glyph.minV).setColor(color)

        xOffset += glyph.advance * size
    }
}

fun GuiGraphicsExtractor.text(pose: Matrix3x2fc, fontData: FontData?, text: String, x: Float, y: Float, size: Float, color: Int) {
    if (fontData == null) return

    guiRenderState.addGuiElement(
        TexturedElementToDraw(
            pose,
            MSDF_IMAGE_PIPELINE,
            x, y, fontData.width(text, size), fontData.height(size),
            { vertexConsumer ->
                fontData.addVertices(vertexConsumer, pose, text, x, y, size, color)
            },
            scissorStack.peek(),
            fontData.texture
        )
    )
}

fun GuiGraphicsExtractor.text(fontData: FontData?, text: String, x: Float, y: Float, size: Float, color: Int = -1) =
    text(pose(), fontData, text, x, y, size, color)

fun GuiGraphicsExtractor.centeredText(pose: Matrix3x2fc, fontData: FontData?, text: String, x: Float, y: Float, size: Float, color: Int) {
    if (fontData == null) return

    text(pose, fontData, text, x + fontData.width(text, size) / 2, y + fontData.height(size) / 2, size, color)
}

fun GuiGraphicsExtractor.centeredText(fontData: FontData?, text: String, x: Float, y: Float, size: Float, color: Int = -1) {
    if (fontData == null) return

    text(pose(), fontData, text, x - fontData.width(text, size) / 2, y - fontData.height(size) / 2, size, color)
}

fun FontData.width(text: String, size: Float): Float {
    var w = 0.0f
    for (i in text.indices) {
        val charCode = text[i].code
        val glyph = glyphs[charCode] ?: continue
        if (i > 0) {
            val prevCode = text[i - 1].code
            w += kernings[kerningKey(prevCode, charCode)] ?: 0f
        }
        w += glyph.advance * size
    }
    return w
}

fun FontData.height(size: Float): Float {
    return (metrics.ascender - metrics.descender) * size
}