package cicada.client.rotation.ai

import cicada.ai.Activation
import cicada.ai.LayerConfig
import cicada.ai.NeuralNetwork
import kotlinx.serialization.json.*

// Number of features captured per game tick (see Sample).
private const val SAMPLE_SIZE = 15

// The predicted (last) frame omits its own delta (2 values) to avoid target leakage.
class RotationNN(val history: Int, layerConfig: List<LayerConfig>) : NeuralNetwork(history * SAMPLE_SIZE - 2, layerConfig + LayerConfig(2, Activation.TANH)) {
    override val asJson: JsonObject
        get() = buildJsonObject {
            putJsonObject("info") {
                put("history", history)
                put("input", inputCount)

                putJsonArray("layers") {
                    layerSizes.drop(1).forEach { add(it) }
                }

                put("avgLoss", avgLoss)
            }

            putJsonArray("layers") {
                for (layer in layers) {
                    addJsonObject {
                        put("neuronCount", layer.neuronCount)
                        put("activation", layer.neurons.first().activation.name)

                        putJsonArray("neurons") {
                            for (neuron in layer.neurons) {
                                addJsonObject {
                                    put("bias", neuron.bias)

                                    putJsonArray("weights") {
                                        neuron.weights.forEach { add(it) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    companion object {
        fun parseDataSet(json: JsonObject, history: Int): List<Pair<DoubleArray, DoubleArray>> {
            val samplesJson = json["samples"]
                ?.jsonArray
                ?: return emptyList()

            val samples = samplesJson.map { sampleJson ->
                val obj = sampleJson.jsonObject

                var diffX = 0.0
                var diffY = 0.0
                var diffZ = 0.0

                obj["diff"]?.jsonObject?.let {
                    diffX = it["x"]?.jsonPrimitive?.doubleOrNull ?: 0.0
                    diffY = it["y"]?.jsonPrimitive?.doubleOrNull ?: 0.0
                    diffZ = it["z"]?.jsonPrimitive?.doubleOrNull ?: 0.0
                }

                var deltaX = 0.0
                var deltaY = 0.0

                obj["delta"]?.jsonObject?.let {
                    deltaX = it["x"]?.jsonPrimitive?.doubleOrNull ?: 0.0
                    deltaY = it["y"]?.jsonPrimitive?.doubleOrNull ?: 0.0
                }

                val canCrit = obj["canCrit"]?.jsonPrimitive?.intOrNull?.toDouble() ?: 0.0
                val playerHurtTime = obj["playerHurtTime"]?.jsonPrimitive?.intOrNull?.toDouble() ?: 0.0
                val targetHurtTime = obj["targetHurtTime"]?.jsonPrimitive?.intOrNull?.toDouble() ?: 0.0

                val byteInput = obj["input"]?.jsonPrimitive?.intOrNull ?: 0
                // 7 movement keys: forward, backward, left, right, jump, shift, sprint -> ±1
                val inputs = DoubleArray(7) { bit -> if ((byteInput and (1 shl bit)) != 0) 1.0 else -1.0 }

                doubleArrayOf(
                    diffX, diffY, diffZ,
                    deltaX, deltaY,
                    canCrit,
                    playerHurtTime, targetHurtTime,
                    *inputs
                )
            }

            if (samples.size < history) return emptyList()

            return List(samples.size - history + 1) { i ->
                val input = DoubleArray(history * SAMPLE_SIZE - 2)
                val output = DoubleArray(2)

                var idx = 0
                for (j in 0..<history) {
                    val frame = samples[i + j]
                    // diff (x, y, z)
                    input[idx++] = frame[0]
                    input[idx++] = frame[1]
                    input[idx++] = frame[2]
                    // delta (x, y) — omitted for the predicted (last) frame to avoid target leakage
                    if (j != history - 1) {
                        input[idx++] = frame[3]
                        input[idx++] = frame[4]
                    }
                    // canCrit
                    input[idx++] = frame[5]
                    // hurt times (player, target)
                    input[idx++] = frame[6]
                    input[idx++] = frame[7]
                    // movement keys, incl. sprint (frame[8..14])
                    for (k in 8..14) input[idx++] = frame[k]
                }

                output[0] = samples[i + history - 1][3]
                output[1] = samples[i + history - 1][4]

                Pair(input, output)
            }
        }

        fun fromJson(json: JsonObject): RotationNN {

            val info = json["info"]?.jsonObject

            val history = info
                ?.get("history")
                ?.jsonPrimitive
                ?.int ?: 0

            val inputCount = info
                ?.get("input")
                ?.jsonPrimitive
                ?.int ?: 0

            val layersJson = json["layers"]
                ?.jsonArray ?: emptyList()

            val layerConfigs = layersJson.mapNotNull { layerElement ->

                val layerObject = layerElement.jsonObject

                val neuronCount = layerObject["neuronCount"]
                    ?.jsonPrimitive
                    ?.int ?: return@mapNotNull null

                val activation = layerObject["activation"]
                    ?.jsonPrimitive
                    ?.content
                    ?.let {
                        runCatching { Activation.valueOf(it) }.getOrNull()
                    }
                    ?: Activation.TANH

                LayerConfig(
                    neuronCount = neuronCount,
                    activation = activation
                )
            }

            // The serialized layers already include the output layer that the RotationNN
            // constructor appends, so drop it here to avoid duplicating it on round-trip.
            val network = RotationNN(history, layerConfigs.dropLast(1))

            info?.get("avgLoss")
                ?.jsonPrimitive
                ?.doubleOrNull
                ?.let {
                    network.avgLoss = it
                }

            for ((layerIndex, layerElement) in layersJson.withIndex()) {

                val layerObject = layerElement.jsonObject

                val neuronsJson = layerObject["neurons"]
                    ?.jsonArray ?: continue

                val layer = network.layers
                    .getOrNull(layerIndex)
                    ?: continue

                for ((neuronIndex, neuronElement) in neuronsJson.withIndex()) {

                    val neuronObject = neuronElement.jsonObject

                    val neuron = layer.neurons
                        .getOrNull(neuronIndex)
                        ?: continue

                    neuronObject["bias"]
                        ?.jsonPrimitive
                        ?.doubleOrNull
                        ?.let {
                            neuron.bias = it
                        }

                    neuronObject["weights"]
                        ?.jsonArray
                        ?.mapNotNull {
                            it.jsonPrimitive.doubleOrNull
                        }
                        ?.toDoubleArray()
                        ?.let {
                            neuron.weights = it
                        }
                }
            }

            return network
        }
    }
}