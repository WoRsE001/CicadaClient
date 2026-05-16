package cicada.client

import cicada.client.feature.command.CommandManager
import cicada.client.config.ConfigSystem
import cicada.client.event.EventCaller
import cicada.client.key.KeyCaller
import cicada.client.feature.module.ModuleManager
import cicada.client.rotation.RotationHandler
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

@Suppress("UNUSED_EXPRESSION")
object CicadaClient : ModInitializer {
	const val MOD_ID = "cicada-client"
	const val NAME = "Cicada"

	private val logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		logger.info("loading...")

		val startTime = measureTimeMillis {
			EventCaller
			KeyCaller
			RotationHandler

			ModuleManager
			CommandManager
			ConfigSystem
		}

		logger.info("successful loaded at $startTime ms.")
	}

	fun of(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
