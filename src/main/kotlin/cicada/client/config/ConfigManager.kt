package cicada.client.config

import cicada.client.CicadaClient
import cicada.client.feature.hud.HUDs
import cicada.client.feature.module.Modules
import kotlinx.io.files.FileNotFoundException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import java.io.File

// SCWGxD regrets everything he did. 19.06.2026 15:11.
object ConfigManager {
    val configsFolder = File(CicadaClient.rootFolder, "configs").apply { if (!exists()) mkdirs() }
    private val defaultConfig = File(configsFolder, "default.ccc")

    init {
        if (defaultConfig.exists()) {
            loadDefault()
        } else {
            saveDefault()
        }
    }

    fun create(file: File) {
        if (file.exists()) file.createNewFile()
    }

    fun save(file: File) {
        if (!file.exists()) {
            create(file)
        }

        val configJson = buildJsonObject {
            put("modules", Modules.serializeTo())
            put("HUDs", HUDs.serializeTo())
        }

        file.writeText(Json.encodeToString(configJson))
    }

    fun saveDefault() {
        save(defaultConfig)
    }

    fun load(file: File) {
        if (!file.exists()) {
            throw FileNotFoundException(file.absolutePath)
        }

        val configJson = Json.parseToJsonElement(file.readText()).jsonObject

        configJson["modules"]?.let { Modules.deserializeFrom(it.jsonObject) }
        configJson["HUDs"]?.let { HUDs.deserializeFrom(it.jsonObject) }
    }

    fun loadDefault() {
        load(defaultConfig)
    }
}