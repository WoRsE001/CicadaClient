package cicada.client.feature.command.commands

import cicada.ai.Activation
import cicada.ai.LayerConfig
import cicada.client.feature.command.Command
import cicada.client.rotation.ai.RotateModel
import cicada.client.rotation.ai.RotateModels
import cicada.client.rotation.ai.RotateModels.modelsFolder
import cicada.client.rotation.ai.dataSetFolder
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

// SCWGxD regrets everything he did. 25.05.2026 8:01.
object CommandRotateAI : Command(
    "RotateAI",
    "",
    ".rotateAI create <name> <memory> <layers>\n.rotateAI train <name> <data-sets> <epochs>\n.rotateAI update",
    "rotateAI"
) {
    override fun execute(args: List<String>) {
        if (args[0] == "create") {
            val modelFile = File(modelsFolder, "${args[1]}.carm")

            if (modelFile.exists() || RotateModels.firstOrNull { args[1] == it.name } != null) {
                mc.displayMessage("Model already exists.")
                return
            }

            modelFile.createNewFile()

            val memory = args[2].toIntOrNull() ?: run {
                mc.displayMessage("Illegal argument.")
                return
            }

            val layerConfigs = args[3].split(",").map {
                val neuronCount = it.toIntOrNull() ?: run {
                    mc.displayMessage("Illegal argument.")
                    return
                }

                LayerConfig(neuronCount, Activation.TANH)
            }

            val model = RotateModel(args[1], memory, layerConfigs)
            RotateModels += model

            modelFile.writeText(Json { prettyPrint = true }.encodeToString(JsonObject.serializer(), model.asJson))
        } else if (args[0] == "train") {
            val model = RotateModels.firstOrNull { args[1] == it.name } ?: run {
                mc.displayMessage("Model does not exist.")
                return
            }

            val dataSetFiles = args[2].split(",").map {
                val file = File(dataSetFolder, "${it}.cads")
                if (!file.exists()) {
                    mc.displayMessage("DataSet $it does not exist.")
                    return
                }
                file
            }

            val dataSets = dataSetFiles.map { file ->
                val dataSetJson = Json.parseToJsonElement(file.readText()).jsonObject

                val samplesJson = dataSetJson["samples"]
                    ?.jsonArray
                    ?: return@map emptyList()

                samplesJson.map { sampleJson ->
                    val sampleJsonObject = sampleJson.jsonObject

                    val input = sampleJsonObject["input"]
                        ?.jsonArray
                        ?.map {
                            it.jsonPrimitive.doubleOrNull ?: 0.0
                        }
                        ?.toDoubleArray()
                        ?: doubleArrayOf()

                    val output = sampleJsonObject["output"]
                        ?.jsonArray
                        ?.map {
                            it.jsonPrimitive.doubleOrNull ?: 0.0
                        }
                        ?.toDoubleArray()
                        ?: doubleArrayOf()

                    Pair(input, output)
                }
            }

            val dataSet = parse(model.memory, dataSets.flatten())

            val epochs = args[3].toIntOrNull() ?: run {
                mc.displayMessage("Illegal argument.")
                return
            }

            model.train(dataSet, epochs)
        }

        mc.displayMessage(usage)
    }

    fun parse(
        memory: Int,
        data: List<Pair<DoubleArray, DoubleArray>>
    ): List<Pair<DoubleArray, DoubleArray>> {
        return List(data.size - memory + 1) { i ->
            val input = DoubleArray(memory * 5 - 2)
            val output = DoubleArray(2)

            for (j in 0..<memory) {
                input[j * 3 + 0] = data[i + j].first[0]
                input[j * 3 + 1] = data[i + j].first[1]
                input[j * 3 + 2] = data[i + j].first[1]
            }

            val shift = memory * 3
            for (k in 0..<memory - 1) {
                input[k * 2 + 0 + shift] = data[i + k].second[0]
                input[k * 2 + 1 + shift] = data[i + k].second[1]
            }

            output[0] = data[i + memory - 1].second[0]
            output[1] = data[i + memory - 1].second[1]

            Pair(input, output)
        }
    }
}