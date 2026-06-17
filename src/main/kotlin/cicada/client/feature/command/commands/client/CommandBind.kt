package cicada.client.feature.command.commands.client

import cicada.client.feature.command.Command
import cicada.client.feature.module.ClientModule
import cicada.client.key.Keybind
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc
import cicada.client.utils.input.inputByName
import net.ccbluex.liquidbounce.features.command.builder.CommandBuilder
import net.ccbluex.liquidbounce.features.command.builder.ParameterBuilder

// SCWGxD regrets everything he did. 17.06.2026 6:29.
object CommandBind : Command.Factory {
    override fun createCommand() = CommandBuilder.begin("bind")
        .alias("b")
        .parameter(
            ParameterBuilder<String>("module")
                .verifiedBy(ParameterBuilder.MODULE_VALIDATOR)
                .required()
                .build()
        )
        .parameter(
            ParameterBuilder<String>("key")
                .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                .required()
                .build()
        )
        .parameter(
            ParameterBuilder<String>("hold")
                .verifiedBy(ParameterBuilder.BOOLEAN_VALIDATOR)
                .optional()
                .build()
        )
        .handler {
            val module = args[0] as ClientModule
            val key = inputByName(args[1] as String).value
            val hold = args[2] as Boolean?

            module.keybind = Keybind(key, hold ?: false)

            mc.displayMessage("Module ${module.name} has bound to ${args[1] as String}")
        }
        .build()
}