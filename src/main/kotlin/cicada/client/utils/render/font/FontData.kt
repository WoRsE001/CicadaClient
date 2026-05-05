package cicada.client.utils.render.font

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import cicada.client.utils.mc
import net.minecraft.resources.Identifier
import java.nio.charset.StandardCharsets

class FontData(
    val atlas: AtlasData,
    val metrics: MetricsData,
    val glyphs: List<GlyphData>,
    val kernings: List<KerningData>,
) {
    companion object {
        private val GSON = Gson()

        fun fromJson(path: Identifier): FontData {
            val resource = mc.resourceManager.getResource(path)
                .orElseThrow { RuntimeException("Font resource not found: $path") }

            return resource.open()
                .bufferedReader(StandardCharsets.UTF_8)
                .use { reader -> GSON.fromJson(reader, FontData::class.java) }
        }
    }
}

data class AtlasData(
    @SerializedName("distanceRange")
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
    val planeBounds: BoundsData,
    val atlasBounds: BoundsData
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