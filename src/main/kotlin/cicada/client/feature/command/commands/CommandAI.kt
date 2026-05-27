package cicada.client.feature.command.commands

import cicada.client.CicadaClient
import cicada.client.ai.LayerConfig
import cicada.client.feature.command.Command
import cicada.client.feature.module.modules.misc.ModuleModelTrainer
import cicada.client.rotation.ai.RotateModel
import cicada.client.rotation.ai.RotateModels
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc
import java.io.File

// SCWGxD regrets everything he did. 25.05.2026 8:01.
object CommandAI : Command(
    "AI",
    "",
    ".ai <create/train> <name>; .ai update",
    "ai"
) {
    val modelsFolder = File(CicadaClient.NAME, "models").apply {
        if (!exists()) {
            mkdirs()
        }
    }

    override fun execute(args: List<String>) {
        if (args.size == 1) {
            if (args[0] == "update") {

            }
        } else if (args.size == 3) {
            if (args[0] == "train") {
                val model = RotateModels.firstOrNull { it.name == args[1] }
                if (model == null) {
                    mc.displayMessage("Model not exists")
                    return
                }
                model.train(ModuleModelTrainer.parse(model), args[2].toInt())
            }
        } else if (args.size == 4) {
            if (args[0] == "create") {
                val modelFile = File(modelsFolder, "${args[1]}.cam").apply {
                    if (exists()) {
                        mc.displayMessage("Model exists already")
                        return
                    } else {
                        createNewFile()
                    }
                }

                if (RotateModels.firstOrNull { it.name == args[1] } != null) {
                    mc.displayMessage("Model exists already")
                    return
                } else {
                    val model = RotateModel(args[1], args[2].toInt(), args[3].split(",").map { LayerConfig(it.toInt()) })
                    RotateModels += model
                    modelFile.writeText(model.asJson().toString())
                    mc.displayMessage("Model created!!")
                    return
                }
            }
        }

        mc.displayMessage(usage)
    }
}