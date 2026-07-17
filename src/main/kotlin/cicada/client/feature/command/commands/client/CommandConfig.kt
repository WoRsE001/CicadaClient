package cicada.client.feature.command.commands.client

import cicada.client.config.ConfigManager
import cicada.client.feature.command.Command
import cicada.client.feature.command.builder.ParameterBuilder
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc
import net.ccbluex.liquidbounce.features.command.builder.CommandBuilder
import java.io.File

// SCWGxD regrets everything he did. 19.06.2026 15:30.
object CommandConfig : Command.Factory {
    override fun createCommand() = CommandBuilder.begin("config")
        .alias("settings")
        .hub()
        .subcommand {
            CommandBuilder.begin("create")
                .parameter(
                    ParameterBuilder<String>("name")
                        .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                        .required()
                        .build()
                )
                .handler {
                    val configName = args[0] as String
                    ConfigManager.create(getFileByName(configName))
                    mc.displayMessage("Config has been created.")
                }
                .build()
        }
        .subcommand {
            CommandBuilder.begin("save")
                .parameter(
                    ParameterBuilder<String>("name")
                        .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                        .required()
                        .build()
                )
                .handler {
                    val configName = args[0] as String
                    ConfigManager.save(getFileByName(configName))
                    mc.displayMessage("Config has been saved.")
                }
                .build()
        }
        .subcommand {
            CommandBuilder.begin("load")
                .parameter(
                    ParameterBuilder<String>("name")
                        .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                        .required()
                        .build()
                )
                .handler {
                    val configName = args[0] as String
                    ConfigManager.load(getFileByName(configName))
                    mc.displayMessage("Config has been loaded.")
                }
                .build()
        }
        .subcommand {
            CommandBuilder.begin("list")
                .handler {
                    val listFile = ConfigManager.configsFolder.listFiles()

                    if (listFile.isEmpty()) {
                        mc.displayMessage("You don't have any configs.")
                    } else {
                        mc.displayMessage("configs")
                        printConfigsFromFolder(ConfigManager.configsFolder, 0)
                    }
                }
                .build()
        }
        .build()

    fun getFileByName(name: String): File {
        return File(ConfigManager.configsFolder,
            if (name.split("/").last().contains(".")) {
                name
            } else {
                "$name.ccc"
            }
        )
    }

    fun printConfigsFromFolder(folder: File, index: Int) {
        for (file in folder.listFiles()) {
            mc.displayMessage("${"| ".repeat(index)}|-${file.name}")

            if (file.isDirectory) {
                printConfigsFromFolder(file, index + 1)
            }
        }
    }
}