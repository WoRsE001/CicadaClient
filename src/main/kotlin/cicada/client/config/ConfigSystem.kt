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

object ConfigSystem {
    private val logger = LogManager.getLogger("${CicadaClient.NAME}/config-system")

    val rootFolder = File(
        mc.gameDirectory, CicadaClient.NAME
    ).apply {
        if (!exists()) {
            mkdir()
        }
    }

    val configFolder = File(
        rootFolder, "configs"
    ).apply {
        if (!exists()) {
            mkdir()
        }
    }

    fun getConfigFileFromName(name: String) = File(configFolder, "$name.cc")

    fun createConfig(name: String): Boolean {
        val config = getConfigFileFromName(name)

        if (config.createNewFile()) {
            logger.info("Created new config: ${config.path}.")
            return true
        } else {
            logger.warn("Config already exist: ${config.path}.")
            return false
        }
    }

    fun saveConfig(name: String) {
        val config = getConfigFileFromName(name)

        if (!config.exists()) {
            logger.warn("Config not found, create new config ${config.path}.")
            createConfig(name)
        }

        val jsonConfig = buildJsonObject {
            put("lastTimeUpdate", LocalDateTime.now().toString())
            put("modules", ModuleManager.serializeTo())
        }

        config.writeText(jsonConfig.toString())
        logger.info("Config created.")
    }

    fun loadConfig(name: String): Boolean {
        val config = getConfigFileFromName(name)

        if (!config.exists()) {
            logger.error("File not found: ${config.path}.")
            return false
        }

        val stringConfig = config.readText()
        val jsonConfig = Json.parseToJsonElement(stringConfig).jsonObject

        val lastTimeUpdate = jsonConfig["lastTimeUpdate"]?.jsonPrimitive?.content ?: "Unknown"
        jsonConfig["modules"]?.jsonObject?.let { ModuleManager.deserializeFrom(it) } ?: logger.warn("While loading \"modules\" object wasn't found.")

        return true
    }

    fun getConfigNames() =
        configFolder.listFiles().filter { it.isFile && it.extension == ".cc" }.map { it.nameWithoutExtension }
}