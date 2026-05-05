package cicada.client.utils.render.font

import com.mojang.blaze3d.vertex.VertexConsumer
import cicada.client.CicadaClient
import cicada.client.utils.mc
import net.minecraft.client.gui.render.TextureSetup
import org.joml.Matrix3x2fc

class MsdfFont private constructor(
            val texture: TextureSetup,
    private val atlasData: AtlasData,
    private val metricsData: MetricsData,
    private val glyphs: Map<Int, MsdfGlyph>
) {
    fun applyGlyphs(
        text: String,
        pose: Matrix3x2fc,
        vertexConsumer: VertexConsumer,
        x: Float,
        y: Float,
        size: Float,
        color: Int
    ) {
        var xOffset = 0f
        val baseLine = y + metricsData.ascender * size
        for (i in 0 until text.length) {
            val char = text[i].code
            val glyph = glyphs[char] ?: continue

            xOffset += glyph.apply(pose, vertexConsumer, x + xOffset, baseLine, size, color)
        }
    }

    fun width(text: String, size: Float): Float {
        var width = 0.0f
        for (i in 0 until text.length) {
            val char = text[i].code
            val glyph = this.glyphs[char] ?: continue

            width += glyph.width(size)

        }

        return width
    }

    fun height(size: Float): Float {
        return (metricsData.ascender - metricsData.descender) * size
    }

    companion object {
        fun create(fontName: String): MsdfFont {
            val abstractTexture = mc.textureManager.getTexture(CicadaClient.of("fonts/$fontName/$fontName.png"))
            val texture = TextureSetup.singleTexture(abstractTexture.textureView, abstractTexture.sampler)

            val fontData = FontData.fromJson(CicadaClient.of("fonts/$fontName/$fontName.json"))
            val width = fontData.atlas.width
            val height = fontData.atlas.height
            val glyphs: Map<Int, MsdfGlyph> = fontData.glyphs.associate { glyphData ->
                glyphData.unicode to MsdfGlyph(glyphData, width, height)
            }

            return MsdfFont(texture, fontData.atlas, fontData.metrics, glyphs)
        }
    }
}