package net.ccbluex.liquidbounce.features.command.builder

import cicada.client.feature.command.Command
import cicada.client.feature.command.Parameter

class CommandBuilder(private val name: String) {

    private var aliases = listOf<String>()
    private var parameters = mutableListOf<Parameter>()
    private var subcommands = mutableListOf<Command>()
    private var handler: Command.Handler? = null
    private var executable = true

    companion object {
        fun begin(name: String) = CommandBuilder(name)
    }

    fun alias(vararg aliases: String) = apply {
        this.aliases = aliases.toList()
    }

    fun parameter(param: Parameter) = apply {
        parameters.add(param)
    }

    fun subcommand(sub: Command) = apply {
        subcommands.add(sub)
    }

    fun subcommand(factory: Command.Factory) = subcommand(factory.createCommand())

    fun handler(handler: Command.Handler) = apply {
        this.handler = handler
    }

    fun hub() = apply {
        executable = false
    }

    fun build(): Command {
        if (executable) {
            require(handler != null) { "У исполняемой команды '$name' нет обработчика" }
        } else {
            require(handler == null) { "Хаб-команда '$name' не должна иметь обработчик" }
        }

        val command = Command(name, aliases, parameters, subcommands, executable, handler)

        for (sub in subcommands) {
            sub.parent = command
        }

        return command
    }
}
