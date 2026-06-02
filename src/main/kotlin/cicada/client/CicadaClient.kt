package cicada.client

import cicada.client.feature.command.CommandManager
import cicada.client.config.ConfigSystem
import cicada.client.connection.Client
import cicada.client.connection.Server
import cicada.client.event.EventCaller
import cicada.client.key.KeyCaller
import cicada.client.feature.module.ModuleManager
import cicada.client.font.Fonts
import cicada.client.rotation.RotationHandler
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc
import net.fabricmc.api.ModInitializer
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
		logger.info("loading...")

		val startTime = measureTimeMillis {
			EventCaller
			KeyCaller
			RotationHandler

			ModuleManager
			CommandManager
			ConfigSystem
			Fonts

			Server
			Server.onMessage = { clientId, message -> mc.displayMessage("Client#$clientId: $message") }
			Server.onClientConnected = { clientId, ip -> mc.displayMessage("Client#$clientId (ip:$ip) has connected.") }
			Server.onClientDisconnected = { clientId -> mc.displayMessage("Client#$clientId has disconnected.") }

			Client
			Client.onMessage = { message -> mc.displayMessage("Server: $message") }
			Client.onConnected = { ip, port -> mc.displayMessage("Client has connected to server: $ip:$port.") }
			Client.onDisconnected = { mc.displayMessage("Client disconnected.") }
		}

		logger.info("successful loaded at $startTime ms.")
	}

	fun of(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
