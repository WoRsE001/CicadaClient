package cicada.client.feature.command.commands

import cicada.client.feature.command.Command
import cicada.client.config.ConfigManager
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc

object CommandConfig : Command(
    "Config",
    "Save and load configs",
    ".config <create/save/load> <config name>, .config list",
    "config", "cfg"
) {
    override fun execute(args: List<String>) {
        if (args.size == 1) {
            if (args[0] == "list") {
                val configNames = ConfigManager.getConfigNames()

                if (configNames.isEmpty()) {
                    mc.displayMessage("Configs not found.")
                } else {
                    val content = StringBuilder("Configs: ")

                    for (configName in configNames) {
                        content.append("$configName, ")
                    }
                    content.dropLast(2)
                    content.append(".")

                    mc.displayMessage(content)
                }

                return
            }
        } else if (args.size == 2) {
            when(args[0]) {
                "create" -> {
                    val configName = args[1]

                    if (ConfigManager.createConfig(configName)) {
                        mc.displayMessage("Config \"$configName\" successfully created.")
                    } else {
                        mc.displayMessage("Config \"$configName\" already exist.")
                    }

                    return
                }

                "save" -> {
                    val configName = args[1]

                    ConfigManager.saveConfig(configName)
                    mc.displayMessage("Config \"$configName\" successfully saved.")

                    return
                }

                "load" -> {
                    val configName = args[1]

                    if (ConfigManager.loadConfig(configName)) {
                        mc.displayMessage("Config \"$configName\" successfully loaded.")
                    } else {
                        mc.displayMessage("Config \"$configName\" not found.")
                    }

                    return
                }
            }
            if (args[0] == "create") {
                ConfigManager.createConfig(args[1])
                return
            } else if (args[0] == "save") {
                val configName = args[1]

                ConfigManager.saveConfig(configName)
                mc.displayMessage("Config: $configName successfully saved.")
                return
            } else if (args[0] == "load") {
                val configName = args[1]

                try {
                    ConfigManager.loadConfig(configName)
                    mc.displayMessage("Config: $configName successfully saved.")
                } catch (_: IllegalArgumentException) {
                    mc.displayMessage("Config: $configName wasn't found.")
                }

                return
            }
        }

        mc.displayMessage(usage)
    }
}