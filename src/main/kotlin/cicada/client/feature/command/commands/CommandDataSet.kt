package cicada.client.feature.command.commands

import cicada.client.feature.command.Command
import cicada.client.feature.module.modules.misc.ModuleDataRecorder
import cicada.client.rotation.ai.dataSetFolder
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc
import cicada.client.utils.file.createNextFile
import kotlinx.serialization.json.*
import java.io.File

// SCWGxD regrets everything he did. 27.05.2026 7:52.
object CommandDataSet : Command(
    "DataSet",
    "",
    ".dataset save <name>",
    "dataset"
) {
    override fun execute(args: List<String>) {
        if (args[0] == "save") {
            if (args.size in 1..2) {
                val dataSetFile = if (args.size == 1) {
                    createNextFile(dataSetFolder, "data-set", ".cads")
                } else {
                    val file = File(dataSetFolder, "${args[1]}.cads")

                    if (!file.exists())
                        file.createNewFile()

                    file
                }

                val dataSet = ModuleDataRecorder.parse()

                val dataSetJson = buildJsonObject {
                    put("samples-count", dataSet.size)

                    putJsonArray("samples") {
                        dataSet.forEach { (input, output) ->
                            addJsonObject {
                                putJsonArray("input") {
                                    input.forEach { value ->
                                        add(value)
                                    }
                                }

                                putJsonArray("output") {
                                    output.forEach { value ->
                                        add(value)
                                    }
                                }
                            }
                        }
                    }
                }

                dataSetFile.writeText(Json { prettyPrint = true }.encodeToString(JsonObject.serializer(), dataSetJson))

                return
            }
        }

        mc.displayMessage(usage)
    }
}