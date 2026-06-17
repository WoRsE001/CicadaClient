package cicada.client

import cicada.client.feature.command.CommandManager
import cicada.client.event.EventCaller
import cicada.client.key.KeyCaller
import cicada.client.feature.module.ModuleManager
import cicada.client.font.Fonts
import cicada.client.rotation.RotationHandler
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.system.measureTimeMillis

@Suppress("UNUSED_EXPRESSION")
object CicadaClient {
	const val NAME = "Cicada-client"
	const val MOD_ID = "cicada-client"

	val rootFolder = File(NAME).apply { if (!exists()) mkdirs() }

	private val logger = LoggerFactory.getLogger(MOD_ID)

	fun initialize() {
		logger.info("Loading...")

		val startTime = measureTimeMillis {
			EventCaller
			KeyCaller
			RotationHandler

			ModuleManager
			CommandManager

			Fonts
		}

		logger.info("Successful loaded at $startTime ms.")
	}

	fun of(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
