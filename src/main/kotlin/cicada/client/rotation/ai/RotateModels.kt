package cicada.client.rotation.ai

import cicada.client.CicadaClient
import java.io.File

// SCWGxD regrets everything he did. 25.05.2026 7:36.
object RotateModels : ArrayList<RotateModel>() {
    val modelsFolder = File(CicadaClient.rootFolder, "models").apply { if (!exists()) mkdirs() }

    private fun readResolve(): Any = RotateModels
}