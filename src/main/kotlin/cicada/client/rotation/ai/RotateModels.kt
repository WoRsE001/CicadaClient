package cicada.client.rotation.ai

// SCWGxD regrets everything he did. 25.05.2026 7:36.
object RotateModels : ArrayList<RotateModel>() {
    private fun readResolve(): Any = RotateModels
}