package cicada.client.feature.command.commands.client

import cicada.client.feature.command.Command
import cicada.client.feature.command.builder.ParameterBuilder
import cicada.client.utils.player.Friends
import net.ccbluex.liquidbounce.features.command.builder.CommandBuilder

object CommandFriend : Command.Factory {
    override fun createCommand() = CommandBuilder.begin("friend")
        .hub()
        .subcommand {
            CommandBuilder.begin("add")
                .parameter(
                    ParameterBuilder<String>("name")
                        .required()
                        .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                        .build()
                )
                .handler {
                    val name = args[0] as String
                    Friends += name
                }
                .build()
        }
        .subcommand {
            CommandBuilder.begin("remove")
                .parameter(
                    ParameterBuilder<String>("name")
                        .required()
                        .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                        .build()
                )
                .handler {
                    val name = args[0] as String
                    Friends -= name
                }
                .build()
        }
        .build()
}