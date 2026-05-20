package cicada.client.font

import net.minecraft.client.gui.render.TextureSetup

class FontData(
    val name: String,
    val texture: TextureSetup,
    val atlas: AtlasData,
    val metrics: MetricsData,
    val glyphs: Map<Int, MsdfGlyph>
)

data class RawFontData(
    val atlas: AtlasData,
    val metrics: MetricsData,
    val glyphs: List<GlyphData>,
    val kernings: List<KerningData>
)

data class AtlasData(
    val range: Float,
    val width: Float,
    val height: Float
)

data class MetricsData(
    val lineHeight: Float,
    val ascender: Float,
    val descender: Float
)

data class GlyphData(
    val unicode: Int,
    val advance: Float,
    val planeBounds: BoundsData?,
    val atlasBounds: BoundsData?
)

data class BoundsData(
    val left: Float,
    val bottom: Float,
    val right: Float,
    val top: Float
)

data class KerningData(
    val leftChar: Int,
    val rightChar: Int,
    val advance: Float
)

class MsdfGlyph(
    data: GlyphData,
    atlasWidth: Float,
    atlasHeight: Float
) {
    val advance = data.advance

    val minU = (data.atlasBounds?.left ?: 0f) / atlasWidth
    val maxU = (data.atlasBounds?.right ?: 0f) / atlasWidth
    val minV = 1f - ((data.atlasBounds?.top ?: 0f) / atlasHeight)
    val maxV = 1f - ((data.atlasBounds?.bottom ?: 0f) / atlasHeight)

    val planeLeft = data.planeBounds?.left ?: 0f
    val planeRight = data.planeBounds?.right ?: 0f
    val planeTop = data.planeBounds?.top ?: 0f
    val planeBottom = data.planeBounds?.bottom ?: 0f
}