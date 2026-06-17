package cicada.client.feature.command

import net.minecraft.network.chat.Component

class CommandException(
    val component: Component,
    val usageInfo: List<Component> = emptyList()
) : Exception(component.string)
