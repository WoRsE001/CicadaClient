package cicada.client.config

import cicada.client.CicadaClient
import cicada.client.feature.module.ModuleManager
import cicada.client.utils.client.mc
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import org.apache.logging.log4j.LogManager
import java.io.File
import java.time.LocalDateTime

object ConfigManager {
    val configFolder = File(
        CicadaClient.rootFolder, "configs"
    ).apply {
        if (!exists()) {
            mkdir()
        }
    }

    val defaultConfigFile = File(configFolder, "default.ccc")

    fun createConfig(name: String) {
        val configFile = File(configFolder, "$name.ccc")

        if (!configFile.createNewFile()) {
            error("Config ${configFile.path} already exist.")
        }
    }

    fun saveConfig(name: String) {
        val configFile = File(configFolder, "$name.ccc")

        if (!configFile.exists()) {
            createConfig(name)
        }

        val jsonConfig = buildJsonObject {
            put("lastTimeUpdate", LocalDateTime.now().toString())
            put("modules", ModuleManager.serializeTo())
        }

        configFile.writeText(jsonConfig.toString())
    }

    fun loadConfig(name: String) {
        val config = File(configFolder, "$name.ccc")

        if (!config.exists()) {
            error("File ${config.path} not found.")
        }

        val stringConfig = config.readText()
        val jsonConfig = Json.parseToJsonElement(stringConfig).jsonObject

        val lastTimeUpdate = jsonConfig["lastTimeUpdate"]?.jsonPrimitive?.content ?: "Unknown"
        jsonConfig["modules"]?.jsonObject?.let { ModuleManager.deserializeFrom(it) }
    }

    fun getConfigNames() =
        configFolder.listFiles().filter { it.isFile && it.extension == ".ccc" }.map { it.nameWithoutExtension }
}