package cicada.client.command

import cicada.client.command.impl.CommandBind
import cicada.client.event.Event
import cicada.client.event.EventListener
import cicada.client.event.impl.ChatMessageEvent
import cicada.client.utils.nullCheck

// SCWGxD regrets everything he did. 10.04.2026 7:27.
@Suppress("UNUSED_EXPRESSION")
object Commands : EventListener {
    private val mutableCommands = mutableListOf<Command>()
    private var prefix = "."

    val commands: List<Command>
        get() = mutableCommands

    init {
        registerToEvents()

        CommandBind
    }

    operator fun plusAssign(command: Command) {
        mutableCommands.add(command)
    }

    override fun onEvent(event: Event) {
        if (event is ChatMessageEvent.Send) {
            val content = event.content

            if (!content.startsWith(prefix))
                return

            val args = content.removePrefix(prefix).split(" ")

            for (command in mutableCommands) {
                if (args[0] !in command.aliases)
                    continue

                println("llalaa")

                command.execute(args.drop(1))
                event.cancel()
                break
            }
        }
    }

    override fun listenEvents() = nullCheck()
}