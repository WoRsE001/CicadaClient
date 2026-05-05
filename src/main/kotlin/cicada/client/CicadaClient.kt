package cicada.client

import cicada.client.command.Commands
import cicada.client.event.EventCaller
import cicada.client.key.KeyCaller
import cicada.client.module.Modules
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
		info("loading...")

		val startTime = measureTimeMillis {
			EventCaller
			KeyCaller
			RotationHandler

			Modules
			Commands
		}

		info("successful loaded at $startTime ms.")
	}

	fun info(s: String) = logger.info(s)
	fun warn(s: String) = logger.warn(s)
	fun error(s: String) = logger.error(s)

	fun of(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
