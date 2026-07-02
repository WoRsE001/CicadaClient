package cicada.client.feature.command.commands.client.ai

import cicada.ai.Activation
import cicada.ai.LayerConfig
import cicada.client.CicadaClient
import cicada.client.feature.command.Command
import cicada.client.rotation.ai.RotationNN
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import net.ccbluex.liquidbounce.features.command.builder.CommandBuilder
import cicada.client.feature.command.builder.ParameterBuilder
import cicada.client.feature.module.modules.combat.ModuleAIAttackAura
import java.io.File
import kotlin.concurrent.thread

// SCWGxD regrets everything he did. 25.06.2026 18:43.
object CommandModel : Command.Factory {
    override fun createCommand() = CommandBuilder.begin("model")
        .hub()
        .subcommand {
            CommandBuilder.begin("create")
                .parameter(
                    ParameterBuilder<String>("name")
                        .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                        .required()
                        .build()
                )
                .parameter(
                    ParameterBuilder<String>("historySize")
                        .verifiedBy(ParameterBuilder.INTEGER_VALIDATOR)
                        .required()
                        .build()
                )
                .parameter(
                    ParameterBuilder<String>("layerSizes")
                        .verifiedBy(ParameterBuilder.INTEGER_VALIDATOR)
                        .required()
                        .vararg()
                        .build()
                )
                .handler {
                    val fileName = args[0] as String
                    val historySize = args[1] as Int
                    val layerSizes = (args[2] as Array<*>).filterIsInstance<Int>()
                    val model = RotationNN(historySize, layerSizes.map { LayerConfig(it, Activation.TANH) })
                    val file = File(CicadaClient.rootFolder, "models/$fileName.cam").apply { createNewFile() }
                    file.writeText(Json { prettyPrint = true }.encodeToString(JsonObject.serializer(), model.asJson))
                }
                .build()
        }
        .subcommand {
            CommandBuilder.begin("train")
                .parameter(
                    ParameterBuilder<String>("modelName")
                        .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                        .required()
                        .build()
                )
                .parameter(
                    ParameterBuilder<String>("epochs")
                        .verifiedBy(ParameterBuilder.INTEGER_VALIDATOR)
                        .required()
                        .build()
                )
                .parameter(
                    ParameterBuilder<String>("dataSetNames")
                        .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                        .required()
                        .vararg()
                        .build()
                )
                .handler {
                    val modelName = args[0] as String
                    val epochs = args[1] as Int
                    val dataSetNames = (args[2] as Array<*>).map { it.toString() }
                    val modelFile = File(CicadaClient.rootFolder, "models/$modelName.cam")
                    val dataSetFiles = dataSetNames.map { File(CicadaClient.rootFolder, "datasets/$it.cads") }
                    val model = RotationNN.fromJson(Json.decodeFromString(modelFile.readText()))
                    val dataSet = dataSetFiles.flatMap { RotationNN.parseDataSet(Json.decodeFromString(it.readText()), model.history) }

                    thread {
                        runCatching {
                            model.train(dataSet, epochs, whileTrain = { epochs: Int, loss: Double -> println(epochs) })
                            // Persist the trained weights (and avgLoss) back to disk.
                            modelFile.writeText(Json { prettyPrint = true }.encodeToString(JsonObject.serializer(), model.asJson))
                        }.onFailure { it.printStackTrace() }
                    }
                }
                .build()
        }
        .subcommand {
            CommandBuilder.begin("use")
                .parameter(
                    ParameterBuilder<String>("name")
                        .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                        .required()
                        .build()
                )
                .handler {
                    val name = args[0] as String
                    val file = File(CicadaClient.rootFolder, "models/$name.cam")

                    runCatching {
                        ModuleAIAttackAura.model = RotationNN.fromJson(Json.decodeFromString(file.readText()))
                    }.onSuccess {
                        mc.displayMessage("Model '$name' is now active")
                    }.onFailure {
                        mc.displayMessage("Failed to load model '$name': ${it.message}")
                    }
                }
                .build()
        }
        .build()
}