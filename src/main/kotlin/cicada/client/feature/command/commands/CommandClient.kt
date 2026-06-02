package cicada.client.feature.command.commands

import cicada.client.connection.Client
import cicada.client.feature.command.Command

// SCWGxD regrets everything he did. 02.06.2026 16:43.
object CommandClient : Command(
    "Client",
    "",
    "",
    "client"
) {
    override fun execute(args: List<String>) {
        if (args[0] == "connect") {
            Client.connect(args[1], args[2].toIntOrNull() ?: 8080)
        } else if (args[0] == "disconnect") {
            Client.disconnect()
        } else if (args[0] == "send") {
            Client.sendMessage(args[1])
        }
    }
}