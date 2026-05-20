package cicada.client.font

import cicada.client.CicadaClient
import cicada.client.utils.client.mc
import com.google.gson.Gson
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.Resource
import java.io.File
import java.nio.charset.StandardCharsets

// SCWGxD regrets everything he did. 17.05.2026 11:53.
object Fonts : LinkedHashMap<String, FontData>() {
    val rootFolder = File(
        mc.gameDirectory, CicadaClient.NAME
    ).apply {
        if (!exists()) {
            mkdir()
        }
    }

    val fontFolder = File(
        rootFolder, "fonts"
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
        // 1. Очищаем старые шрифты перед обновлением
        this.clear()

        // 2. Ищем все .json файлы в папке fonts через ResourceManager
        val jsonResources = mc.resourceManager.listResources("fonts") { id ->
            id.path.endsWith(".json")
        }

        // 3. Проходимся по каждому найденному файлу
        for ((id, resource) in jsonResources) {
            try {
                // Извлекаем имя шрифта (например, из "fonts/inter-bold/inter-bold.json" получаем "inter-bold")
                val fileName = id.path.substringAfterLast("/")
                val fontName = fileName.substringBeforeLast(".json")

                // Получаем текстуру
                val textureId = CicadaClient.of("fonts/$fontName/$fontName.png")
                val abstractTexture = mc.textureManager.getTexture(textureId)
                val texture = TextureSetup.singleTexture(abstractTexture.textureView, abstractTexture.sampler)

                // Парсим JSON в RawFontData (так как в JSON нет TextureSetup и готовых MsdfGlyph)
                val rawFontData = resource.open()
                    .bufferedReader(StandardCharsets.UTF_8)
                    .use { reader -> GSON.fromJson(reader, RawFontData::class.java) }

                val width = rawFontData.atlas.width
                val height = rawFontData.atlas.height

                // Конвертируем GlyphData в MsdfGlyph
                val glyphs: Map<Int, MsdfGlyph> = rawFontData.glyphs.associate { glyphData ->
                    glyphData.unicode to MsdfGlyph(glyphData, width, height)
                }

                println(fontName)

                // 4. Создаем финальный FontData и кладем его в нашу мапу
                this[fontName] = FontData(
                    name = fontName,
                    texture = texture,
                    atlas = rawFontData.atlas,
                    metrics = rawFontData.metrics,
                    glyphs = glyphs
                )

                // Раскомментируй для дебага:
                // println("Successfully loaded font: $fontName")

            } catch (e: Exception) {
                println("Failed to load font from resource: ${id.path}")
                e.printStackTrace()
            }
        }
    }

    private fun readResolve(): Any = Fonts
}