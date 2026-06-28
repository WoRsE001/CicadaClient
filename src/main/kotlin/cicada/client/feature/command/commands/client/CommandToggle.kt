package cicada.client.feature.command.commands.client

import cicada.client.feature.command.Command
import cicada.client.feature.module.ClientModule
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc
import net.ccbluex.liquidbounce.features.command.builder.CommandBuilder
import cicada.client.feature.command.builder.ParameterBuilder

// SCWGxD regrets everything he did. 17.06.2026 11:40.
object CommandToggle : Command.Factory {
    override fun createCommand() = CommandBuilder.begin("toggle")
        .alias("tog", "t")
        .parameter(
            ParameterBuilder<String>("module")
                .verifiedBy(ParameterBuilder.MODULE_VALIDATOR)
                .required()
                .build()
        )
        .handler {
            val module = args[0] as ClientModule
            module.toggle()
            mc.displayMessage("Module ${module.name} has ${if (module.toggled) "enabled" else "disabled"}")
        }
        .build()
}