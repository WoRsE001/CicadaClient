package cicada.client.command.impl

import cicada.client.command.Command
import cicada.client.module.Modules
import cicada.client.utils.displayMessage
import cicada.client.utils.input.inputByName
import cicada.client.utils.mc

// SCWGxD regrets everything he did. 10.04.2026 7:42.
object CommandBind : Command(
    "Bind",
    "Bind modules",
    ".bind <module> <key>",
    "b", "bind"
) {
    override fun execute(args: List<String>) {
        if (args.size !in 2..3) {
            mc.displayMessage(usage)
            return
        }

        val module = Modules.getModule(args[0])
        requireNotNull(module) { mc.displayMessage("Module ${args[0]} don't exists"); return }

        val key = inputByName(args[1].uppercase())
        module.keybind.key = key.value

        if (args.size > 2)
            module.keybind.hold = args[2] == "true" || args[2] == "1"

        mc.displayMessage("$module bound to ${key.displayName} (hold=${module.keybind.hold})")
    }
}