package cicada.client.feature.command.commands.client.ai

import cicada.client.CicadaClient
import cicada.client.feature.command.Command
import cicada.client.feature.command.builder.ParameterBuilder
import cicada.client.feature.module.modules.misc.ModuleRatkaEXE
import kotlinx.serialization.json.*
import net.ccbluex.liquidbounce.features.command.builder.CommandBuilder
import java.io.File

// SCWGxD regrets everything he did. 24.06.2026 13:20.
object CommandDataSet : Command.Factory {
    override fun createCommand() = CommandBuilder.begin("dataset")
        .hub()
        .subcommand {
            CommandBuilder.begin("save")
                .parameter(
                    ParameterBuilder<String>("name")
                        .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                        .required()
                        .build()
                )
                .handler {
                    val fileName = args[0] as String
                    val file = File(CicadaClient.rootFolder, "datasets/$fileName.cads")
                    file.writeText(Json { prettyPrint = true }.encodeToString(JsonObject.serializer(), buildJsonObject {
                        put("count samples", ModuleRatkaEXE.dataSet.size)

                        putJsonArray("samples") {
                            for (sample in ModuleRatkaEXE.dataSet) {
                                add(sample.parse())
                            }
                        }
                    }))
                }
                .build()
        }
        .build()
}