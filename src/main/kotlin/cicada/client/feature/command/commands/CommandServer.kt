package cicada.client.feature.command.commands

import cicada.client.connection.Server
import cicada.client.feature.command.Command
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc

// SCWGxD regrets everything he did. 02.06.2026 16:20.
object CommandServer : Command(
    "Server",
    "",
    "",
    "server"
) {
    override fun execute(args: List<String>) {
        if (args[0] == "start") {
            try {
                Server.start(args[1].toIntOrNull() ?: 8080)
            } catch (ex: Exception) {
                mc.displayMessage("Server is not started.")
                mc.displayMessage(ex.message)
            }

            mc.displayMessage("Server is started.")
        } else if (args[0] == "stop") {
            Server.stop()
            mc.displayMessage("Server is stopped.")
        } else if (args[0] == "send") {
            Server.sendMessage(args[1].toIntOrNull() ?: 0, args[2])
        } else if (args[0] == "sendAll") {
            Server.broadcastMessage(args[1])
        }
    }
}