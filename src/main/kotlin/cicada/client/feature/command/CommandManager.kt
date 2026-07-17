package cicada.client.feature.command

import cicada.client.feature.command.commands.client.CommandBind
import cicada.client.feature.command.commands.client.CommandConfig
import cicada.client.feature.command.commands.client.CommandToggle
import cicada.client.feature.command.commands.client.ai.CommandDataSet
import cicada.client.feature.command.commands.client.ai.CommandModel
import cicada.client.feature.command.commands.ingame.CommandCoordinates
import net.minecraft.network.chat.Component

object CommandManager : HashMap<String, Command>() {
    var prefix: String = "."

    init {
        CommandExecutor

        val commands = arrayListOf(
            CommandModel,
            CommandDataSet,
            CommandBind,
            CommandConfig,
            CommandToggle,
            CommandCoordinates
        )

        commands.forEach { addCommand(it.createCommand()) }
    }

    fun addCommand(command: Command) {
        this[command.name] = command
        for (alias in command.aliases) {
            this[alias] = command
        }
    }

    fun removeCommand(command: Command) {
        this.remove(command.name)
        for (alias in command.aliases) {
            this.remove(alias)
        }
    }

    fun execute(text: String) {
        val args = tokenize(text)
        if (args.isEmpty()) return

        val (command, commandIdx) = findCommand(args)
            ?: throw CommandException(
                Component.literal("Неизвестная команда: ${args[0]}")
            )

        if (!command.executable) {
            throw CommandException(
                Component.literal("Используйте подкоманду для '${command.name}'"),
                command.usage()
            )
        }

        val rawArgs = args.drop(commandIdx + 1)

        if (command.parameters.isEmpty() && rawArgs.isNotEmpty()) {
            throw CommandException(
                Component.literal("Команда '${command.name}' не принимает параметры"),
                command.usage()
            )
        }

        for (i in command.parameters.indices) {
            val param = command.parameters[i]
            if (param.required && i >= rawArgs.size) {
                throw CommandException(
                    Component.literal("Не хватает обязательного параметра: <${param.name}>"),
                    command.usage()
                )
            }
        }

        val parsedArgs = arrayOfNulls<Any>(rawArgs.size)
        for (i in rawArgs.indices) {
            if (i >= command.parameters.size) {
                throw CommandException(
                    Component.literal("Лишний аргумент: '${rawArgs[i]}'"),
                    command.usage()
                )
            }

            val param = command.parameters[i]

            if (param.vararg) {
                val varargValues = rawArgs.drop(i).map { parseArg(command, it, param) }.toTypedArray()
                parsedArgs[i] = varargValues
                break
            } else {
                parsedArgs[i] = parseArg(command, rawArgs[i], param)
            }
        }

        val ctx = Command.Handler.Context(command, parsedArgs.filterNotNull().toTypedArray())
        with(command.handler!!) { ctx.invoke() }
    }

    private fun findCommand(args: List<String>): Pair<Command, Int>? {
        val root = this[args[0]] ?: return null
        var current = root
        var currentIdx = 0

        for (i in 1 until args.size) {
            val sub = current.subcommandMap[args[i]] ?: break
            current = sub
            currentIdx = i
        }

        return Pair(current, currentIdx)
    }

    private fun parseArg(command: Command, input: String, param: Parameter): Any {
        val verifier = param.verifier ?: return input

        return when (val result = verifier.verify(input)) {
            is Parameter.Verifier.Ok -> result.value
            is Parameter.Verifier.Error -> throw CommandException(
                Component.literal(
                    "Неверное значение для <${param.name}>: '$input' — ${result.message}"
                ),
                command.usage()
            )
        }
    }

    fun tokenize(line: String): List<String> {
        val tokens = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var escaped = false

        for (c in line) {
            when {
                escaped -> {
                    current.append(c)
                    escaped = false
                }
                c == '\\' -> escaped = true
                c == '"' -> inQuotes = !inQuotes
                c == ' ' && !inQuotes -> {
                    if (current.isNotBlank()) {
                        tokens.add(current.toString().trim())
                        current.clear()
                    }
                }
                else -> current.append(c)
            }
        }

        if (current.isNotBlank()) {
            tokens.add(current.toString().trim())
        }

        return tokens
    }

    private fun readResolve(): Any = CommandManager
}
