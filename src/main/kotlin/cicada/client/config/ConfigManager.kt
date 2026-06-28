package cicada.client.config

import cicada.client.CicadaClient
import cicada.client.feature.hud.HUDManager
import cicada.client.feature.module.ModuleManager
import kotlinx.io.files.FileNotFoundException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import java.io.File

// SCWGxD regrets everything he did. 19.06.2026 15:11.
object ConfigManager {
    val configsFolder = File(CicadaClient.rootFolder, "configs").apply { if (!exists()) mkdirs() }
    val defaultConfig = File(configsFolder, "default.ccc")

    init {
        if (defaultConfig.exists()) {
            load(defaultConfig)
        } else {
            save(defaultConfig)
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
            put("modules", ModuleManager.serializeTo())
            //put("HUDs", HUDManager.serializeTo())
        }

        file.writeText(Json.encodeToString(configJson))
    }

    fun load(file: File) {
        if (!file.exists()) {
            throw FileNotFoundException(file.absolutePath)
        }

        val configJson = Json.parseToJsonElement(file.readText()).jsonObject

        configJson["modules"]?.let { ModuleManager.deserializeFrom(it.jsonObject) }
        //configJson["HUDs"]?.let { HUDManager.deserializeFrom(it.jsonObject) }


    }
}