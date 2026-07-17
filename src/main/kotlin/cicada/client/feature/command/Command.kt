package cicada.client.feature.command

import net.minecraft.network.chat.Component
import java.util.*

class Command(
    val name: String,
    val aliases: List<String>,
    val parameters: List<Parameter>,
    val subcommands: List<Command>,
    val executable: Boolean,
    val handler: Handler?,
) {
    val subcommandMap: MutableMap<String, Command> = TreeMap<String, Command>(String.CASE_INSENSITIVE_ORDER)

    init {
        for (sub in subcommands) {
            subcommandMap[sub.name] = sub
            for (alias in sub.aliases) {
                subcommandMap[alias] = sub
            }
        }
    }

    fun usage(): List<Component> {
        val result = mutableListOf<Component>()

        if (executable) {
            val parts = buildList {
                var cmd: Command? = this@Command
                while (cmd != null) {
                    add(0, cmd.name)
                    cmd = cmd.parent
                }

                for (param in parameters) {
                    add(if (param.required) "<${param.name}>" else "[<${param.name}>]")
                }
            }
            result.add(Component.literal(parts.joinToString(" ")))
        }

        for (sub in subcommands) {
            result.addAll(sub.usage())
        }

        return result
    }

    var parent: Command? = null

    fun interface Handler {
        fun Context.invoke()

        class Context(val command: Command, val args: Array<out Any>)
    }

    fun interface Factory {
        fun createCommand(): Command
    }
}
