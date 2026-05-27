package cicada.client.rotation.ai

import cicada.ai.Activation
import cicada.ai.LayerConfig
import cicada.ai.NeuralNetwork

// SCWGxD regrets everything he did. 25.05.2026 7:38.
class RotateModel(val name: String, val memory: Int, hiddenLayers: List<LayerConfig>) : NeuralNetwork(
    memory * 5 - 2,
    hiddenLayers + LayerConfig(2, Activation.TANH)
) {
}