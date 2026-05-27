package cicada.client.feature.command

import cicada.client.feature.command.commands.CommandBind
import cicada.client.feature.command.commands.CommandConfig
import cicada.client.event.Event
import cicada.client.event.EventListener
import cicada.client.event.impl.ChatMessageEvent
import cicada.client.feature.command.commands.CommandAI
import cicada.client.utils.client.nullCheck

// SCWGxD regrets everything he did. 10.04.2026 7:27.
@Suppress("UNUSED_EXPRESSION")
object CommandManager : EventListener {
    private val _commands = mutableListOf<Command>()
    val commands: List<Command>
        get() = _commands

    private var prefix = "."

    init {
        registerToEvents()

        CommandAI
        CommandBind
        CommandConfig
    }

    operator fun plusAssign(command: Command) {
        _commands.add(command)
    }

    override fun onEvent(event: Event) {
        if (event is ChatMessageEvent.Send) {
            val content = event.content

            if (!content.startsWith(prefix))
                return

            val args = content.removePrefix(prefix).split(" ")

            for (command in _commands) {
                if (args[0] !in command.aliases)
                    continue

                command.execute(args.drop(1))
                event.cancel()
                break
            }
        }
    }

    override fun listenEvents() = nullCheck()
}