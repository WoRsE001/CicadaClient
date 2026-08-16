package cicada.client.font

import cicada.client.CicadaClient
import cicada.client.utils.client.mc
import com.google.gson.Gson
import net.minecraft.client.gui.render.TextureSetup
import java.io.File
import java.nio.charset.StandardCharsets

// SCWGxD regrets everything he did. 17.05.2026 11:53.
object Fonts : LinkedHashMap<String, FontData>() {
    val fontFolder = File(
        CicadaClient.rootFolder, "fonts"
    ).apply {
        if (!exists()) {
            mkdir()
        }
    }

    val localFontFolder = CicadaClient.of("fonts")

    private val GSON = Gson()

    init {
        update()
    }

    fun update() {
        this.clear()

        val jsonResources = mc.resourceManager.listResources("fonts") { id ->
            id.path.endsWith(".json")
        }

        for ((id, resource) in jsonResources) {
            try {
                val fileName = id.path.substringAfterLast("/")
                val fontName = fileName.substringBeforeLast(".json")

                val textureId = CicadaClient.of("fonts/$fontName/$fontName.png")
                val abstractTexture = mc.textureManager.getTexture(textureId)
                val texture = TextureSetup.singleTexture(abstractTexture.textureView, abstractTexture.sampler)

                val rawFontData = resource.open()
                    .bufferedReader(StandardCharsets.UTF_8)
                    .use { reader -> GSON.fromJson(reader, RawFontData::class.java) }

                val width = rawFontData.atlas.width
                val height = rawFontData.atlas.height

                val glyphs: Map<Int, MSDFGlyph> = rawFontData.glyphs.associate { glyphData ->
                    glyphData.unicode to MSDFGlyph(glyphData, width, height)
                }

                val kernings: Map<Long, Float> = rawFontData.kernings
                    ?.associate { k -> kerningKey(k.leftChar, k.rightChar) to k.advance }
                    ?: emptyMap()

                this[fontName] = FontData(
                    fontName, texture, rawFontData.atlas, rawFontData.metrics, glyphs, kernings
                )
            } catch (e: Exception) {
                println("Failed to load font from resource: ${id.path}")
                e.printStackTrace()
            }
        }
    }

    private fun readResolve(): Any = Fonts
}