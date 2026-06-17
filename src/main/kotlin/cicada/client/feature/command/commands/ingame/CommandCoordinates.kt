package cicada.client.feature.command.commands.ingame

import cicada.client.feature.command.Command
import cicada.client.utils.client.connection
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc
import cicada.client.utils.client.player
import net.ccbluex.liquidbounce.features.command.builder.CommandBuilder
import net.ccbluex.liquidbounce.features.command.builder.ParameterBuilder
import net.minecraft.core.Vec3i

// SCWGxD regrets everything he did. 17.06.2026 5:42.
object CommandCoordinates : Command.Factory {
    override fun createCommand() = CommandBuilder.begin("coordinates")
        .alias("coords", "position", "pos")
        .hub()
        .subcommand(
            CommandBuilder.begin("copy")
                .handler {
                    val position: Vec3i = player.blockPosition()
                    mc.keyboardHandler.clipboard = "${position.x} ${position.y} ${position.z}"
                    mc.displayMessage("Coordinates has copied to clipboard")
                }
                .build()
        )
        .subcommand(
            CommandBuilder.begin("send")
                .parameter(
                    ParameterBuilder<String>("player")
                        .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                        .required()
                        .build()
                )
                .handler {
                    val playerName = args[0] as String
                    val position: Vec3i = player.blockPosition()
                    connection.sendCommand("msg $playerName x: ${position.x} y: ${position.y} z: ${position.z}")
                }
                .build()
        )
        .build()
}