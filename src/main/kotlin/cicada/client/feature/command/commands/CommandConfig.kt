package cicada.client.feature.command.commands

import cicada.client.feature.command.Command
import cicada.client.config.ConfigSystem
import cicada.client.utils.displayMessage
import cicada.client.utils.mc

object CommandConfig : Command(
    "Config",
    "Save and load configs",
    ".config <create/save/load> <config name>, .config list",
    "config", "cfg"
) {
    override fun execute(args: List<String>) {
        if (args.size == 1) {
            if (args[0] == "list") {
                val configNames = ConfigSystem.getConfigNames()

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

                    if (ConfigSystem.createConfig(configName)) {
                        mc.displayMessage("Config \"$configName\" successfully created.")
                    } else {
                        mc.displayMessage("Config \"$configName\" already exist.")
                    }

                    return
                }

                "save" -> {
                    val configName = args[1]

                    ConfigSystem.saveConfig(configName)
                    mc.displayMessage("Config \"$configName\" successfully saved.")

                    return
                }

                "load" -> {
                    val configName = args[1]

                    if (ConfigSystem.loadConfig(configName)) {
                        mc.displayMessage("Config \"$configName\" successfully loaded.")
                    } else {
                        mc.displayMessage("Config \"$configName\" not found.")
                    }

                    return
                }
            }
            if (args[0] == "create") {
                ConfigSystem.createConfig(args[1])
                return
            } else if (args[0] == "save") {
                val configName = args[1]

                ConfigSystem.saveConfig(configName)
                mc.displayMessage("Config: $configName successfully saved.")
                return
            } else if (args[0] == "load") {
                val configName = args[1]

                try {
                    ConfigSystem.loadConfig(configName)
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